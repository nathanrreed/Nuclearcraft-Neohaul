package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.passive.ITilePassive;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
import com.nred.nuclearcraft.util.InventoryStackList;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

public class MachineReservoirPortEntity extends AbstractMachineEntity implements ITickable, ITileInventory, ITileFluid {
    public final @Nonnull NonNullList<ItemStack> backupStacks = InventoryStackList.EMPTY_LIST;
    public final @Nonnull List<Tank> backupTanks = Collections.emptyList();

    public @Nonnull InventoryConnection[] backupInventoryConnections = ITileInventory.inventoryConnectionAll(ItemSorption.IN);
    public @Nonnull FluidConnection[] backupFluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public MachineReservoirPortEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("reservoir_port").get(), position, blockState);
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
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public void update() {
        if (!getTileWorld().isClientSide()) {
            MachineLogic logic = getLogic();
            Optional<Direction> facing = getPartPosition().getDirection();
            if (logic != null && facing.isPresent()) {
                logic.pushReservoirPortItemToSide(this, facing.get());
                logic.pushReservoirPortFluidToSide(this, facing.get());
            }
        }
    }

    // Inventory

    @Override
    public ItemStack getItem(int slot) {
        MachineLogic logic = getLogic();

        if (logic != null) {
            return logic.getReservoirPortStackInSlot(this, slot);
        } else if (getInventoryStacks().isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            return ITileInventory.super.getItem(slot);
        }
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getReservoirPortInventoryStacks(this) : backupStacks;
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getReservoirPortInventoryConnections(this) : backupInventoryConnections;
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
        backupInventoryConnections = connections;
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
        MachineLogic logic = getLogic();
        return logic != null ? logic.decrReservoirPortStackSize(this, slot, amount) : ITileInventory.super.removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        MachineLogic logic = getLogic();
        return logic != null ? logic.removeReservoirPortStackFromSlot(this, slot) : ITileInventory.super.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        MachineLogic logic = getLogic();
        if (logic != null) {
            logic.setReservoirPortInventorySlotContents(this, slot, stack);
        } else {
            ITileInventory.super.setItem(slot, stack);
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        MachineLogic logic = getLogic();
        return logic != null && logic.isReservoirPortItemValid(this, slot, stack);
    }

    @Override
    public boolean hasConfigurableInventoryConnections() {
        return true;
    }

    @Override
    public void pushStacksToSide(@Nonnull Direction side) {
        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }

        if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushItemsTo()) {
            return;
        }

        IItemHandler adjInv = level.getCapability(Capabilities.ItemHandler.BLOCK, tile.getBlockPos(), side.getOpposite());

        if (adjInv == null || adjInv.getSlots() < 1) {
            return;
        }

        boolean pushed = false;
        NonNullList<ItemStack> stacks = getInventoryStacks();
        for (int i = 0; i < stacks.size(); ++i) {
            pushed |= pushSlotToHandler(adjInv, stacks, side, i);
        }

        if (pushed) {
            refreshMachineActivity();
        }
    }

    @Override
    public void clearAllSlots() {
        MachineLogic logic = getLogic();
        if (logic != null) {
            logic.clearReservoirPortInventory(this);
        } else {
            ITileInventory.super.clearAllSlots();
        }
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getReservoirPortTanks(this) : backupTanks;
    }

    @Override
    @Nonnull
    public FluidConnection[] getFluidConnections() {
        MachineLogic logic = getLogic();
        return logic != null ? logic.getReservoirPortFluidConnections(this) : backupFluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        backupFluidConnections = connections;
    }

    @Override
    @Nonnull
    public FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Nonnull
    @Override
    public ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public void pushFluidToSide(@Nonnull Direction side) {
        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }

        if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo()) {
            return;
        }

        IFluidHandler adjStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, tile.getBlockPos(), side.getOpposite());
        if (adjStorage == null) {
            return;
        }

        List<Tank> tanks = getTanks();
        if (!tanks.isEmpty()) {
            Tank tank = tanks.get(0);
            onWrapperDrain(tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), SIMULATE), EXECUTE), EXECUTE), EXECUTE);
        }
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
        MachineLogic logic = getLogic();
        return logic != null && logic.isReservoirPortFluidValid(this, tankNumber, stack);
    }

    @Override
    public void clearAllTanks() {
        ITileFluid.super.clearAllTanks();

        MachineLogic logic = getLogic();
        if (logic != null) {
            logic.onReservoirPortTanksCleared(this);
        }
    }

    // IMultitoolLogic

    @Override
    public boolean onUseMultitool(ItemStack multitool, ServerPlayer player, Level level, Direction facing, BlockPos hitPos) {
        if (!player.isCrouching()) {
            if (getMultiblockController().isPresent()) {
                if (getItemSorption(facing, 0) != ItemSorption.IN || getTankSorption(facing, 0) != TankSorption.IN) {
                    for (Direction side : Direction.values()) {
                        setItemSorption(side, 0, ItemSorption.IN);
                        setTankSorption(side, 0, TankSorption.IN);
                    }
                    setActivity(false);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.in_config").withStyle(ChatFormatting.DARK_AQUA)));
                } else {
                    for (Direction side : Direction.values()) {
                        setItemSorption(side, 0, ItemSorption.OUT);
                        setTankSorption(side, 0, TankSorption.OUT);
                    }
                    setActivity(true);
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", Component.translatable(MODID + ".tooltip.out_config").withStyle(ChatFormatting.RED)));
                }
                markDirtyAndNotify(true);
                return true;
            }
        }
        return super.onUseMultitool(multitool, player, level, facing, hitPos);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeInventoryConnections(nbt, registries);
        writeFluidConnections(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readInventoryConnections(nbt, registries);
        readFluidConnections(nbt, registries);
    }

    // Capability

    @Override
    public IItemHandler getItemSideCapability(@Nullable Direction side) {
        MachineLogic logic = getLogic();
        if (logic != null) {
            if (logic.hasReservoirPortItemCapability(this, side)) {
                return logic.getReservoirPortItemHandler(this, side);
            }
        } else if (hasInventorySideCapability(side)) {
            return getItemHandler(side);
        }
        return null;
    }

    @Override
    public FluidTileWrapper getFluidSideCapability(@Nullable Direction side) {
        MachineLogic logic = getLogic();
        if (logic != null) {
            if (logic.hasReservoirPortFluidCapability(this, side)) {
                return logic.getReservoirPortFluidHandler(this, nonNullSide(side));
            }
        } else if (hasFluidSideCapability(side)) {
            return getFluidSide(nonNullSide(side));
        }
        return null;
    }
}