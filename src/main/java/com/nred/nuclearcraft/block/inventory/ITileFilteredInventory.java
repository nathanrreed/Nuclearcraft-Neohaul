package com.nred.nuclearcraft.block.inventory;

import com.nred.nuclearcraft.block.ITileFiltered;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public interface ITileFilteredInventory extends ITileFiltered, ITileInventory {

    @Nonnull
    NonNullList<ItemStack> getInventoryStacksInternal();

    @Nonnull
    NonNullList<ItemStack> getFilterStacks();

    boolean isItemValidForSlotInternal(int slot, ItemStack stack);
}