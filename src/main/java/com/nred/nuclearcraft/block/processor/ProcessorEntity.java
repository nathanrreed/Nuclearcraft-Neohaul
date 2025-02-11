package com.nred.nuclearcraft.block.processor;

import com.nred.nuclearcraft.enumm.ButtonEnum;
import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import com.nred.nuclearcraft.helpers.CustomItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
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

import static com.nred.nuclearcraft.block.processor.Processor.POWERED;
import static com.nred.nuclearcraft.block.processor.Processor.PROCESSOR_ON;
import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.menu.ProcessorMenu.ENERGY;
import static com.nred.nuclearcraft.menu.ProcessorMenu.SPEED;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public abstract class ProcessorEntity extends BlockEntity implements MenuProvider, IMenuProviderExtension {
    public boolean redstoneMode = false;
    private final String typeName;

    public CustomEnergyHandler energyHandler;
    public CustomFluidStackHandler fluidHandler;
    public CustomItemStackHandler itemStackHandler;
    public int progress = 0;
    public DataSlot progressSlot;

    public ProcessorEntity(BlockPos pos, BlockState blockState, String typeName, int numStacks, int numTanks) {
        super(PROCESSOR_ENTITY_TYPE.get(typeName).get(), pos, blockState);
        this.typeName = typeName;
        energyHandler = new CustomEnergyHandler(PROCESSOR_CONFIG_MAP.get(this.typeName).capacity(), true, false) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };

        fluidHandler = new CustomFluidStackHandler(PROCESSOR_CONFIG_MAP.get(this.typeName).fluid_capacity(), numTanks, false, true) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };

        itemStackHandler = new CustomItemStackHandler(numStacks, true, true) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                if ((slot == SPEED && stack.is(UPGRADE_MAP.get("speed")) || (slot == ENERGY && stack.is(UPGRADE_MAP.get("energy"))))) {
                    return true;
                }
                return false;
            }

            @Override
            public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return super.extractItem(slot, amount, simulate);
            }

            @Override
            public void setStackInSlot(int slot, @NotNull ItemStack stack) { // TODO try to fix flicker
                super.setStackInSlot(slot, stack);
                if (slot == SPEED) {
                    energyHandler.setCapacity(Math.max(((stack.getCount() - this.getStackInSlot(ENERGY).getCount()) + 1), 1) * PROCESSOR_CONFIG_MAP.get(typeName).capacity());
                } else if (slot == ENERGY) {
                    energyHandler.setCapacity(Math.max(((this.getStackInSlot(SPEED).getCount() - stack.getCount()) + 1), 1) * PROCESSOR_CONFIG_MAP.get(typeName).capacity());
                }
            }
        };

        progressSlot = new DataSlot() {
            @Override
            public int get() {
                return progress;
            }

            @Override
            public void set(int value) {
                progress = value;
            }
        };
    }

    public ProcessorEntity(BlockPos pos, BlockState blockState, String typeName, int numStacks) {
        this(pos, blockState, typeName, numStacks, 0);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        redstoneMode = tag.getBoolean("redstoneMode");
        itemStackHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        fluidHandler.deserializeNBT(registries, tag.getCompound("fluids"));
        if (tag.contains("energy"))
            energyHandler.deserializeNBT(registries, tag.get("energy"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putBoolean("redstoneMode", redstoneMode);
        tag.put("inventory", itemStackHandler.serializeNBT(registries));
        tag.put("fluids", fluidHandler.serializeNBT(registries));
        tag.put("energy", energyHandler.serializeNBT(registries));
    }

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity entity) {
        if (!(redstoneMode && state.getValue(POWERED)) && energyHandler.getEnergyStored() >= PROCESSOR_CONFIG_MAP.get(this.typeName).processing_power()) {
            if (!state.getValue(PROCESSOR_ON)) {
                level.setBlockAndUpdate(pos, state.setValue(PROCESSOR_ON, true));
            }
            progressSlot.set((progress + 1) % 100);
            energyHandler.internalExtractEnergy(PROCESSOR_CONFIG_MAP.get(this.typeName).processing_power(), false);
        } else if (state.getValue(PROCESSOR_ON)) {
            level.setBlockAndUpdate(pos, state.setValue(PROCESSOR_ON, false));
        }
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
    public void onDataPacket(@NotNull Connection connection, @NotNull ClientboundBlockEntityDataPacket packet, HolderLookup.@NotNull Provider registries) {
        super.onDataPacket(connection, packet, registries);
    }
}