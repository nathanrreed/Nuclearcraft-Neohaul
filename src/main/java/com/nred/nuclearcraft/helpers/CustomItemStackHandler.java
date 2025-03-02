package com.nred.nuclearcraft.helpers;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CustomItemStackHandler extends ItemStackHandler {
    private final boolean allowInput;
    private final boolean allowOutput;
    public ArrayList<ItemOutputSetting> outputSettings;

    public enum ItemOutputSetting {
        DEFAULT, VOID_EXCESS, VOID;
    }

    public CustomItemStackHandler(int size, boolean allowInput, boolean allowOutput) {
        super(size);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
        this.outputSettings = new ArrayList<>(Collections.nCopies(size, ItemOutputSetting.DEFAULT));
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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = super.serializeNBT(provider);
        tag.putIntArray("output_settings", outputSettings.stream().map(Enum::ordinal).toList());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
        this.outputSettings = new ArrayList<>(Arrays.stream(nbt.getIntArray("output_settings")).mapToObj(i -> ItemOutputSetting.values()[i]).toList());
    }

    public void outputSetting(int index, boolean left) {
        this.outputSettings.set(index, next(!left, this.outputSettings.get(index)));
        onContentsChanged(index);
    }

    public ItemOutputSetting getOutputSetting(int slot) {
        return outputSettings.get(slot);
    }

    public ItemOutputSetting next(boolean reverse, ItemOutputSetting prev) {
        if (reverse) {
            return switch (prev) {
                case ItemOutputSetting.DEFAULT -> ItemOutputSetting.VOID;
                case ItemOutputSetting.VOID_EXCESS -> ItemOutputSetting.DEFAULT;
                case ItemOutputSetting.VOID -> ItemOutputSetting.VOID_EXCESS;
            };
        } else {
            return switch (prev) {
                case ItemOutputSetting.DEFAULT -> ItemOutputSetting.VOID_EXCESS;
                case ItemOutputSetting.VOID_EXCESS -> ItemOutputSetting.VOID;
                case ItemOutputSetting.VOID -> ItemOutputSetting.DEFAULT;
            };
        }
    }
}