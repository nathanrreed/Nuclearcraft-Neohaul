package com.nred.nuclearcraft.block_entity.inventory;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.ITile;
import com.nred.nuclearcraft.block_entity.ITilePort;
import com.nred.nuclearcraft.block_entity.hx.IHeatExchangerPart;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemHandler;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.machine.IMachinePart;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.util.BlockHelper;
import com.nred.nuclearcraft.util.NCInventoryHelper;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public interface ITileInventory extends ITile, WorldlyContainer {
    // Inventory

    @Nonnull
    NonNullList<ItemStack> getInventoryStacks();

    default void clearAllSlots() {
        @Nonnull NonNullList<ItemStack> stacks = getInventoryStacks();
        Collections.fill(stacks, ItemStack.EMPTY);
    }

    // Container

    @Override
    default boolean stillValid(Player player) {
        return ITile.super.isUsableByPlayer(player);
    }

    @Override
    default int getContainerSize() {
        return getInventoryStacks().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack stack : getInventoryStacks()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default @NotNull ItemStack getItem(int slot) {
        return getInventoryStacks().get(slot);
    }

    @Override
    default @NotNull ItemStack removeItem(int slot, int count) {
        return ContainerHelper.removeItem(getInventoryStacks(), slot, count);
    }

    @Override
    default @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(getInventoryStacks(), slot);
    }

    @Override
    default void setItem(int slot, ItemStack stack) {
        @Nonnull NonNullList<ItemStack> stacks = getInventoryStacks();
        ItemStack itemstack = stacks.get(slot);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemstack);

        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        stacks.set(slot, stack);

        if (!flag) {
            markTileDirty();
        }
    }

    @Override
    default boolean canPlaceItem(int slot, ItemStack stack) {
        return true;
    }

    @Override
    default int getMaxStackSize() {
        return 64;
    }

    @Override
    default void clearContent() {
        getInventoryStacks().clear();
    }

    @Override
    default void startOpen(Player player) {
    }

    @Override
    default void stopOpen(Player player) {
    }

    @Override
    default boolean isUsableByPlayer(Player player) {
        return ITile.super.isUsableByPlayer(player);
    }

    // WorldlyContainer

    @Override
    default int[] getSlotsForFace(Direction side) {
        return getInventoryConnection(side).getSlotsForFace();
    }

    @Override
    default boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return getItemSorption(side, slot).canReceive();
    }

    @Override
    default boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return getItemSorption(side, slot).canExtract();
    }

    // Inventory Connections

    @Nonnull
    InventoryConnection[] getInventoryConnections();

    void setInventoryConnections(@Nonnull InventoryConnection[] connections);

    default @Nonnull InventoryConnection getInventoryConnection(@Nonnull Direction side) {
        return getInventoryConnections()[side.ordinal()];
    }

    default @Nonnull ItemSorption getItemSorption(@Nonnull Direction side, int slotNumber) {
        return getInventoryConnections()[side.ordinal()].getItemSorption(slotNumber);
    }

    default void setItemSorption(@Nonnull Direction side, int slotNumber, @Nonnull ItemSorption sorption) {
        getInventoryConnections()[side.ordinal()].setItemSorption(slotNumber, sorption);
    }

    default void toggleItemSorption(@Nonnull Direction side, int slotNumber, ItemSorption.Type type, boolean reverse) {
        if (!hasConfigurableInventoryConnections()) {
            return;
        }
        getInventoryConnection(side).toggleItemSorption(slotNumber, type, reverse);
        markDirtyAndNotify(true);
    }

    default boolean canConnectInventory(@Nonnull Direction side) {
        return getInventoryConnection(side).canConnect();
    }

    static InventoryConnection[] inventoryConnectionAll(@Nonnull List<ItemSorption> sorptionList) {
        InventoryConnection[] array = new InventoryConnection[6];
        for (int i = 0; i < 6; ++i) {
            array[i] = new InventoryConnection(sorptionList);
        }
        return array;
    }

    static InventoryConnection[] inventoryConnectionAll(ItemSorption sorption) {
        return inventoryConnectionAll(Lists.newArrayList(sorption));
    }

    default boolean hasConfigurableInventoryConnections() {
        return false;
    }

    // Item Distribution

    default void pushStacks() {
        for (Direction side : Direction.values()) {
            pushStacksToSide(side);
        }
    }

    default void pushStacksToSide(@Nonnull Direction side) {
        if (!getInventoryConnection(side).canConnect()) {
            return;
        }

        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }
        Level level = getTileWorld();
        Direction oppositeSide = side.getOpposite();

        IItemHandler adjInv = level.getCapability(Capabilities.ItemHandler.BLOCK, tile.getBlockPos(), oppositeSide);
        if (adjInv == null || adjInv.getSlots() < 1) {
            return;
        }

        boolean pushed = false;

        @Nonnull NonNullList<ItemStack> stacks = getInventoryStacks();
        int stackCount = stacks.size();
        for (int i = 0; i < stackCount; ++i) {
            pushed |= pushSlotToHandler(adjInv, stacks, side, i);
        }

        if (pushed) {
            if (this instanceof IProcessor<?, ?, ?> processor) {
                processor.refreshActivity();
            } else if (this instanceof IMachinePart part) {
                part.refreshMachineActivity();
            } else if (this instanceof IHeatExchangerPart part) {
                part.refreshHeatExchangerActivity();
            }

            if (this instanceof ITilePort<?, ?, ?, ?> port) {
                port.setRefreshTargetsFlag(true);
            }
        }
    }

    default boolean pushSlotToHandler(IItemHandler handler, NonNullList<ItemStack> stacks, @Nonnull Direction side, int slot) {
        ItemStack stack;
        if (!getItemSorption(side, slot).canExtract() || (stack = stacks.get(slot)).isEmpty()) {
            return false;
        }

        ItemStack initialStack = stack.copy();
        ItemStack remaining = NCInventoryHelper.addStackToInventory(handler, initialStack);

        if (remaining.getCount() >= initialStack.getCount()) {
            return false;
        }

        stack.shrink(initialStack.getCount() - remaining.getCount());

        if (stack.getCount() <= 0) {
            stacks.set(slot, ItemStack.EMPTY);
        }

        return true;
    }

    default boolean tryPushSlot(IProcessor.HandlerPair[] adjacentHandlers, NonNullList<ItemStack> stacks, int slot, List<Direction> dirs, int dirCount, int indexOffset) {
        if (!stacks.get(slot).isEmpty()) {
            for (int i = 0; i < dirCount; ++i) {
                Direction dir = dirs.get((i + indexOffset) % dirCount);
                if (getItemSorption(dir, slot).equals(ItemSorption.AUTO_OUT)) {
                    IItemHandler handler = adjacentHandlers[dir.ordinal()].itemHandler;
                    if (handler != null && pushSlotToHandler(handler, stacks, dir, slot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // NBT

    default CompoundTag writeInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(nbt, getInventoryStacks(), registries);
        return nbt;
    }

    default void readInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        ContainerHelper.loadAllItems(nbt, getInventoryStacks(), registries);
    }

    default CompoundTag writeInventoryConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        return writeInventoryConnectionsDirectional(nbt, registries, null);
    }

    default CompoundTag writeInventoryConnectionsDirectional(CompoundTag nbt, HolderLookup.Provider registries, @Nullable Direction facing) {
        for (int i = 0; i < 6; ++i) {
            getInventoryConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).writeToNBT(nbt, Direction.from3DDataValue(i));
        }
        return nbt;
    }

    default void readInventoryConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        readInventoryConnectionsDirectional(nbt, registries, null);
    }

    default void readInventoryConnectionsDirectional(CompoundTag nbt, HolderLookup.Provider registries, @Nullable Direction facing) {
        if (!hasConfigurableInventoryConnections()) {
            return;
        }
        for (int i = 0; i < 6; ++i) {
            getInventoryConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).readFromNBT(nbt, Direction.from3DDataValue(i));
        }
    }

    default CompoundTag writeSlotSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < getContainerSize(); ++i) {
            nbt.putInt("itemOutputSetting" + i, getItemOutputSetting(i).ordinal());
        }
        return nbt;
    }

    default void readSlotSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < getContainerSize(); ++i) {
            setItemOutputSetting(i, ItemOutputSetting.values()[nbt.getInt("itemOutputSetting" + i)]);
        }
    }

    // Item Functions

    ItemOutputSetting getItemOutputSetting(int slot);

    void setItemOutputSetting(int slot, ItemOutputSetting setting);

    // Capabilities

    default boolean hasInventorySideCapability(@Nullable Direction side) {
        return side == null || getInventoryConnection(side).canConnect();
    }

    default IItemHandler getItemHandler(@Nullable Direction side) {
        return new ItemHandler<>(this, side);
    }

    default IItemHandler getItemSideCapability(@Nullable Direction side) {
        return hasInventorySideCapability(side) ? getItemHandler(side) : null;
    }
}