package com.nred.nuclearcraft.block.processor;

import com.nred.nuclearcraft.enumm.ButtonEnum;
import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.helpers.CustomItemStackHandler;
import com.nred.nuclearcraft.helpers.HandlerInfo;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

import static com.nred.nuclearcraft.block.processor.Processor.POWERED;
import static com.nred.nuclearcraft.block.processor.Processor.PROCESSOR_ON;
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.config.Config.UPGRADES_CONFIG;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.*;
import static com.nred.nuclearcraft.menu.ProcessorMenu.ENERGY;
import static com.nred.nuclearcraft.menu.ProcessorMenu.SPEED;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_TYPES;

public abstract class ProcessorEntity extends BlockEntity implements MenuProvider, IMenuProviderExtension {
    public boolean redstoneMode = false;
    private final String typeName;
    @NotNull
    private final HandlerInfo handlerInfo;

    public CustomEnergyHandler energyHandler;
    public CustomFluidStackHandler fluidHandler;
    public CustomItemStackHandler itemStackHandler;
    public int progress = 0;
    public int progressPercentage = 0;
    public DataSlot progressSlot;
    public RecipeHolder<ProcessorRecipe> recipe;

    public ProcessorEntity(BlockPos pos, BlockState blockState, String typeName, HandlerInfo handlerInfo) {
        super(PROCESSOR_ENTITY_TYPE.get(typeName).get(), pos, blockState);
        this.typeName = typeName;
        this.handlerInfo = handlerInfo;

        fluidHandler = new CustomFluidStackHandler(PROCESSOR_CONFIG_MAP.get(this.typeName).fluid_capacity(), handlerInfo.numTanks(), true, true) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }

            @Override
            public boolean isFluidValid(int tank, FluidStack stack) {
                if (tank >= handlerInfo.numFluidInputs()) { // Outputs can only be done internally so I am just going to assume it's fine
                    return false;
                } else {
                    return validFluidSlot(stack);
                }
            }

            @Override
            public boolean canOutput(int tank) {
                return tank >= handlerInfo.numFluidInputs();
            }
        };

        itemStackHandler = new CustomItemStackHandler(2 + handlerInfo.numStacks(), true, true) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return switch (slot) {
                    case SPEED -> UPGRADES_CONFIG.speed_max();
                    case ENERGY -> UPGRADES_CONFIG.energy_max();
                    default -> 64;
                };
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if ((slot == SPEED && stack.is(UPGRADE_MAP.get("speed")) || (slot == ENERGY && stack.is(UPGRADE_MAP.get("energy"))))) {
                    return true;
                } else if (slot > ENERGY + handlerInfo.numItemInputs()) { // Outputs can only be done internally so I am just going to assume it's fine
                    return true;
                } else if (slot > ENERGY) {
                    return validSlot(stack);
                }
                return false;
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                if (slot <= ENERGY) {
                    return super.extractItem(slot, amount, simulate);
                } else if (slot > ENERGY + handlerInfo.numItemInputs()) {
                    return super.extractItem(slot, amount, simulate);
                }
                return ItemStack.EMPTY;
            }

            @Override
            public void setStackInSlot(int slot, @NotNull ItemStack stack) { // TODO try to fix flicker
                super.setStackInSlot(slot, stack);
                if (slot == SPEED || slot == ENERGY) {
                    energyHandler.setCapacity(getEnergyCapacity(getSpeedMultiplier(itemStackHandler), getPowerMultiplier(itemStackHandler)));
                }
            }
        };

        energyHandler = new CustomEnergyHandler(getEnergyCapacity(getSpeedMultiplier(itemStackHandler), getPowerMultiplier(itemStackHandler)), true, false) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };

        progressSlot = new DataSlot() {
            @Override
            public int get() {
                return progressPercentage;
            }

            @Override
            public void set(int value) {
                progressPercentage = value;
            }
        };
    }

    public boolean validSlot(@NotNull ItemStack stack) {
        return level.getRecipeManager().getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(typeName).get()).stream().flatMap(holder -> holder.value().itemInputs.stream()).flatMap(a -> Arrays.stream(a.getItems())).parallel().anyMatch(itemStack -> itemStack.is(stack.getItem()));
    }

    public boolean validFluidSlot(@NotNull FluidStack stack) {
        return level.getRecipeManager().getAllRecipesFor(PROCESSOR_RECIPE_TYPES.get(typeName).get()).stream().flatMap(holder -> holder.value().fluidInputs.stream()).flatMap(a -> Arrays.stream(a.getFluids())).parallel().anyMatch(itemStack -> itemStack.is(stack.getFluid()));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        redstoneMode = tag.getBoolean("redstoneMode");
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        fluidHandler.deserializeNBT(registries, tag.getCompound("fluids"));
        if (tag.contains("energy"))
            energyHandler.deserializeNBT(registries, tag.get("energy"));
        progress = tag.getInt("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putBoolean("redstoneMode", redstoneMode);
        tag.put("inventory", itemStackHandler.serializeNBT(registries));
        tag.put("fluids", fluidHandler.serializeNBT(registries));
        tag.put("energy", energyHandler.serializeNBT(registries));
        tag.putInt("progress", progress);

        super.saveAdditional(tag, registries);
    }

    public int getEnergyCapacity(double speedMultiplier, double powerMultiplier) {
        return (int) (Math.ceil(PROCESSOR_CONFIG_MAP.get(typeName).base_time() / speedMultiplier) * Math.ceil(PROCESSOR_CONFIG_MAP.get(typeName).base_power() * powerMultiplier));
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if (!(redstoneMode && state.getValue(POWERED)) && hasRecipe() && energyHandler.getEnergyStored() >= (int) calculateEnergy(typeName, recipe.value().getPowerModifier(), itemStackHandler) && roomForOutputs()) {
            if (!state.getValue(PROCESSOR_ON)) {
                level.setBlockAndUpdate(pos, state.setValue(PROCESSOR_ON, true));
            }

            double ticksNeeded = calculateTicks(typeName, recipe.value().getTimeModifier(), itemStackHandler);
            progress++;
            progressSlot.set((int) ((progress / ticksNeeded) * 37));
            if (progress > ticksNeeded) {
                progress = 0;
                progressSlot.set(0);

                recipeOutputs();
                for (int i = 0; i < recipe.value().itemInputs.size(); i++) {
                    for (int slot = 0; slot < recipe.value().itemInputs.size(); slot++) { // Find the slot the item is in and decrease it
                        if (recipe.value().itemInputs.get(i).test(itemStackHandler.getStackInSlot(slot + ENERGY + 1))) {
                            itemStackHandler.internalExtractItem(slot + ENERGY + 1, recipe.value().itemInputs.get(i).count(), false);
                            break;
                        }
                    }
                }

                for (int i = 0; i < recipe.value().fluidInputs.size(); i++) {
                    fluidHandler.drain(i, recipe.value().fluidInputs.get(i).amount(), IFluidHandler.FluidAction.EXECUTE);
                }
            }

            energyHandler.internalExtractEnergy((int) calculateEnergy(typeName, recipe.value().getPowerModifier(), itemStackHandler), false);
        } else if (state.getValue(PROCESSOR_ON)) {
            level.setBlockAndUpdate(pos, state.setValue(PROCESSOR_ON, false));
            progress = 0;
            progressSlot.set(0);
        }
    }

    public void recipeOutputs() {
        for (int i = 0; i < recipe.value().itemResults.size(); i++) {
            itemStackHandler.internalInsertItem(i + ENERGY + handlerInfo.numItemInputs() + 1, recipe.value().itemResults.get(i).getItems()[0].copy(), false);
        }

        for (int i = 0; i < recipe.value().fluidResults.size(); i++) {
            fluidHandler.fill(handlerInfo.numFluidInputs() + i, recipe.value().fluidResults.get(i).getFluids()[0].copy(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public boolean roomForOutputs() {
        for (int i = 0; i < recipe.value().itemResults.size(); i++) {
            if (itemStackHandler.internalInsertItem(i + ENERGY + handlerInfo.numItemInputs() + 1, recipe.value().itemResults.get(i).getItems()[0].copy(), true) != ItemStack.EMPTY) {
                return false;
            }
        }

        for (int i = 0; i < recipe.value().fluidResults.size(); i++) {
            if (recipe.value().fluidResults.get(i).getFluids()[0].getAmount() != fluidHandler.fill(handlerInfo.numFluidInputs() + i, recipe.value().fluidResults.get(i).getFluids()[0].copy(), IFluidHandler.FluidAction.SIMULATE)) {
                return false;
            }
        }

        return true;
    }

    public boolean hasRecipe() {
        ArrayList<ItemStack> stacks = new ArrayList<>(handlerInfo.numItemInputs());
        for (int i = 1; i <= handlerInfo.numItemInputs(); i++) {
            stacks.add(itemStackHandler.getStackInSlot(ENERGY + i));
        }

        ArrayList<FluidStack> fluidStacks = new ArrayList<>(handlerInfo.numFluidInputs());
        for (int i = 0; i < handlerInfo.numFluidInputs(); i++) {
            fluidStacks.add(fluidHandler.getFluidInTank(i));
        }

        recipe = level.getRecipeManager().getRecipeFor(PROCESSOR_RECIPE_TYPES.get(typeName).get(), new ProcessorRecipeInput(stacks, fluidStacks), level).orElse(null);
        return recipe != null;
    }

    public IItemHandler getItemHandler(Direction side) { // TODO use side config
        return itemStackHandler;
    }

    public IEnergyStorage getEnergyHandler(Direction side) {
        return energyHandler;
    }

    public IFluidHandler getFluidHandler(Direction side) {
        return fluidHandler;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("menu.title." + typeName);
    }

    public void handleButtonPress(ButtonEnum id) {
        switch (id) {
            case REDSTONE_MODE -> redstoneMode ^= true;
        }
    }

    public void handleFluidClear(int tank) {
        fluidHandler.setFluid(tank, FluidStack.EMPTY);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, level.registryAccess());
        components.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        super.collectImplicitComponents(components);
    }

    @Override
    protected void applyImplicitComponents(DataComponentInput componentInput) {
        componentInput.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).loadInto(this, level.registryAccess());
        super.applyImplicitComponents(componentInput);
    }
}