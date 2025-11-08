package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
import com.nred.nuclearcraft.property.MachinePortSorption;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.AXIS_ALL;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_PORT_SORPTION;

public class MachineProcessPortEntity extends AbstractMachineEntity implements ITickable, ITileInventory, ITileFluid {
    private final @Nonnull NonNullList<ItemStack> backupStacks = NonNullList.withSize(0, ItemStack.EMPTY);
    private final @Nonnull List<Tank> backupTanks = Collections.emptyList();

    private final @Nonnull InventoryConnection[] backupInventoryConnections = ITileInventory.inventoryConnectionAll(Collections.emptyList());
    private final @Nonnull FluidConnection[] backupFluidConnections = ITileFluid.fluidConnectionAll(Collections.emptyList());

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    private int setting = 0, slot = 0, tankIndex = -1;

    public MachineProcessPortEntity(BlockPos pos, BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("process_port").get(), pos, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(Machine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(AXIS_ALL, direction.getAxis()), 2));
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
            if (!isMachineAssembled()) {
                return;
            }

            MachineLogic logic = getLogic();
            if (logic == null) {
                return;
            }

            Direction facing = getPartPosition().getDirection().orElse(null);
            if (facing != null) {
                if (slot >= 0 && slot < logic.inventorySize()) {
                    NonNullList<ItemStack> stacks = getInventoryStacks();
                    if (!stacks.isEmpty() && !stacks.get(0).isEmpty() && getItemSorption(facing, 0).canExtract()) {
                        pushStacksToSide(facing);
                    }
                } else if (tankIndex >= 0 && tankIndex < logic.tankCount()) {
                    List<Tank> tanks = getTanks();
                    if (!tanks.isEmpty() && !tanks.get(0).isEmpty() && getTankSorption(facing, 0).canDrain()) {
                        pushFluidToSide(facing);
                    }
                }
            }
        }
    }

    public void setItemFluidData() {
        MachineLogic logic = getLogic();
        if (logic == null) {
            return;
        }

        if (tankIndex >= 0 && logic.tankCount() == 0) {
            setting = 0;
            slot = 0;
            tankIndex = -1;
        } else if (slot >= 0 && logic.inventorySize() == 0) {
            setting = 0;
            slot = -1;
            tankIndex = 0;
        }

        setProperty(MACHINE_PORT_SORPTION, getMachinePortSorption());
        markDirtyAndNotify(true);
    }

    public void incrementItemFluidData(boolean reverse) {
        Machine machine = getMultiblockController().orElse(null);
        if (machine == null) {
            return;
        }

        MachineLogic logic = getLogic();
        if (logic == null) {
            return;
        }

        int sorptionSize = logic.inventorySize() + logic.tankCount();
        if (sorptionSize == 0) {
            setting = 0;
            slot = tankIndex = -1;
            return;
        }

        if (reverse) {
            --setting;
        } else {
            ++setting;
        }
        setting = (sorptionSize + setting % sorptionSize) % sorptionSize;

        if (setting < machine.itemInputSize) {
            slot = setting;
            tankIndex = -1;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize) {
            slot = -1;
            tankIndex = setting - machine.itemInputSize;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize + machine.itemOutputSize) {
            slot = setting - machine.fluidInputSize;
            tankIndex = -1;
        } else {
            slot = -1;
            tankIndex = setting - machine.itemInputSize - machine.itemOutputSize;
        }

        setItemFluidData();
    }

    public @Nonnull MachinePortSorption getMachinePortSorption() {
        Machine machine = getMultiblockController().orElse(null);
        if (machine == null) {
            return MachinePortSorption.ITEM_IN;
        }

        if (setting < machine.itemInputSize) {
            return MachinePortSorption.ITEM_IN;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize) {
            return MachinePortSorption.FLUID_IN;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize + machine.itemOutputSize) {
            return MachinePortSorption.ITEM_OUT;
        } else {
            return MachinePortSorption.FLUID_OUT;
        }
    }

    public int getMachinePortSorptionIndex() {
        Machine machine = getMultiblockController().orElse(null);
        if (machine == null) {
            return 0;
        }

        if (setting < machine.itemInputSize) {
            return setting;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize) {
            return setting - machine.itemInputSize;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize + machine.itemOutputSize) {
            return setting - machine.itemInputSize - machine.fluidInputSize;
        } else {
            return setting - machine.itemInputSize - machine.fluidInputSize - machine.itemOutputSize;
        }
    }

    public int getMachinePortSorptionSize() {
        Machine machine = getMultiblockController().orElse(null);
        if (machine == null) {
            return 0;
        }

        if (setting < machine.itemInputSize) {
            return machine.itemInputSize;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize) {
            return machine.fluidInputSize;
        } else if (setting < machine.itemInputSize + machine.fluidInputSize + machine.itemOutputSize) {
            return machine.itemOutputSize;
        } else {
            return machine.fluidOutputSize;
        }
    }

    public Component getMachinePortSettingString() {
        Component component = switch (getMachinePortSorption()) {
            case ITEM_IN -> Component.translatable(MODID + ".tooltip.item_in_config").withStyle(ChatFormatting.BLUE);
            case FLUID_IN -> Component.translatable(MODID + ".tooltip.fluid_in_config").withStyle(ChatFormatting.DARK_AQUA);
            case ITEM_OUT -> Component.translatable(MODID + ".tooltip.item_out_config").withStyle(ChatFormatting.GOLD);
            case FLUID_OUT -> Component.translatable(MODID + ".tooltip.fluid_out_config").withStyle(ChatFormatting.RED);
        };

        if (getMachinePortSorptionSize() > 1) {
            component = Component.translatable("nc.sf.two_args", Component.translatable("nc.sf.ordinal" + (1 + getMachinePortSorptionIndex())), component);
        }

        return component;
    }

    // Inventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getProcessPortInventoryStacks(backupStacks, slot) : backupStacks;
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getProcessPortInventoryConnections(backupInventoryConnections, slot) : backupInventoryConnections;
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
    }

    @Override
    public ItemOutputSetting getItemOutputSetting(int slot) {
        return ItemOutputSetting.DEFAULT;
    }

    @Override
    public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ITileInventory.super.removeItem(slot, amount);
        if (!getTileWorld().isClientSide()) {
            if (this.slot < 0) {
                return stack;
            }

            Machine machine = getMultiblockController().orElse(null);
            if (machine == null) {
                return stack;
            }

            if (this.slot < machine.itemInputSize) {
                machine.processor.refreshRecipe(level);
                machine.processor.refreshActivity();
            } else if (this.slot < machine.itemInputSize + machine.itemOutputSize) {
                machine.processor.refreshActivity();
            }
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ITileInventory.super.setItem(slot, stack);
        if (!getTileWorld().isClientSide()) {
            if (this.slot < 0) {
                return;
            }

            Machine machine = getMultiblockController().orElse(null);
            if (machine == null) {
                return;
            }

            if (this.slot < machine.itemInputSize) {
                machine.processor.refreshRecipe(level);
                machine.processor.refreshActivity();
            } else if (this.slot < machine.itemInputSize + machine.itemOutputSize) {
                machine.processor.refreshActivity();
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (this.slot < 0) {
            return false;
        }

        Machine machine = getMultiblockController().orElse(null);
        if (machine == null) {
            return false;
        }

        if (stack.isEmpty() || (this.slot >= machine.itemInputSize && this.slot < machine.itemInputSize + machine.itemOutputSize)) {
            return false;
        }

        if (NCConfig.smart_processor_input) {
            return machine.recipeHandler.isValidItemInput(stack, this.slot, machine.processor.getItemInputs(false), machine.processor.getFluidInputs(false), machine.processor.recipeInfo);
        } else {
            return machine.recipeHandler.isValidItemInput(stack);
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return ITileInventory.super.canPlaceItemThroughFace(slot, stack, side) && canPlaceItem(slot, stack);
    }

    @Override
    public void clearAllSlots() {
        ITileInventory.super.clearAllSlots();

        getMultiblockController().ifPresent(machine -> machine.processor.refreshAll(level));
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getProcessPortTanks(backupTanks, tankIndex) : backupTanks;
    }

    @Override
    @Nonnull
    public FluidConnection[] getFluidConnections() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getProcessPortFluidConnections(backupFluidConnections, tankIndex) : backupFluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
    }

    @Override
    @Nonnull
    public FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Override
    @Nonnull
    public ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    @Override
    public boolean hasConfigurableFluidConnections() {
        return true;
    }

    @Override
    public boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
        Machine machine = getMultiblockController().orElse(null);
        if (machine == null) {
            return false;
        }

        if (stack == null || stack.getAmount() <= 0 || (this.tankIndex >= machine.fluidInputSize && this.tankIndex < machine.fluidInputSize + machine.fluidOutputSize)) {
            return false;
        }

        if (NCConfig.smart_processor_input) {
            return machine.recipeHandler.isValidFluidInput(stack, this.tankIndex, machine.processor.getFluidInputs(false), machine.processor.getItemInputs(false), machine.processor.recipeInfo);
        } else {
            return machine.recipeHandler.isValidFluidInput(stack);
        }
    }

    @Override
    public void clearAllTanks() {
        ITileFluid.super.clearAllTanks();

        getMultiblockController().ifPresent(machine -> machine.processor.refreshAll(level));
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (getMultiblockController().isPresent()) {
            incrementItemFluidData(player.isCrouching());
            player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", getMachinePortSettingString()));
            return true;
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        nbt.putInt("setting", setting);
        nbt.putInt("slot", slot);
        nbt.putInt("tankIndex", tankIndex);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        setting = nbt.getInt("setting");
        slot = nbt.getInt("slot");
        tankIndex = nbt.getInt("tankIndex");
        // setItemFluidData();
    }
}