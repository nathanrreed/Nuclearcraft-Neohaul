package com.nred.nuclearcraft.helpers;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CustomItemStackHandler extends ItemStackHandler {
    private final boolean allowInput;
    private final boolean allowOutput;

    public CustomItemStackHandler(int size, boolean allowInput, boolean allowOutput) {
        super(size);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
    }

    // Stop other mods using capability from doing unexpected things
    @Override
    public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (allowInput) {
            return super.insertItem(slot, stack, simulate);
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (allowOutput) {
            return super.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    // Normal insert and extract for use within the mod
    public ItemStack internalInsertItem(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack internalExtractItem(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }
}