package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block.inventory.ITileInventory;
import com.nred.nuclearcraft.block.internal.inventory.ItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class PortItemHandler<T extends ITileInventory> extends ItemHandler<T> {

    public PortItemHandler(T tile, Direction side) {
        super(tile, side);
    }

    @Override
    protected int getStackSplitSize(ItemStack stack, int slotStackCount, int slot) {
        return getSlotLimit(slot) - slotStackCount;
    }

    @Override
    protected int getSlotStackLimit(ItemStack stackInSlot, int slot) {
        return getSlotLimit(slot);
    }
}