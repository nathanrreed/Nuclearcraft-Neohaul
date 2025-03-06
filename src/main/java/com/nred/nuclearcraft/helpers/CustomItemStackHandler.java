package com.nred.nuclearcraft.helpers;

import com.nred.nuclearcraft.helpers.SideConfigEnums.OutputSetting;
import com.nred.nuclearcraft.helpers.SideConfigEnums.SideConfigSetting;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomItemStackHandler extends ItemStackHandler implements IProcessorHandlerConfigs {
    private final boolean allowInput;
    private final boolean allowOutput;
    public ArrayList<OutputSetting> outputSettings;
    public Map<Direction, List<SideConfigSetting>> sideConfig;

    public CustomItemStackHandler(int size, boolean allowInput, boolean allowOutput) {
        super(size);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
//        this.outputSettings = new ArrayList<>(Collections.nCopies(size, OutputSetting.DEFAULT));
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
        tag = OutputSetting.serializeNBT(provider, tag, outputSettings);
        tag = SideConfigSetting.serializeNBT(provider, tag, sideConfig);

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        super.deserializeNBT(provider, nbt);
        this.outputSettings = OutputSetting.deserializeNBT(provider, nbt);
        this.sideConfig = SideConfigSetting.deserializeNBT(provider, nbt);
    }

    public void outputSetting(int index, boolean left) {
        this.outputSettings.set(index, this.outputSettings.get(index).next(!left));
        onContentsChanged(index);
    }

    public void sideConfig(int index, Direction dir, int inputs, boolean left) {
        this.sideConfig.get(dir).set(index, index >= inputs + 2 ? this.sideConfig.get(dir).get(index).nextOutput(left) : this.sideConfig.get(dir).get(index).nextInput(left));
        onContentsChanged(index);
    }

    @Override
    public void createOutputSettings() {
        if (outputSettings == null) {
            this.outputSettings = new ArrayList<>(Collections.nCopies(stacks.size(), OutputSetting.DEFAULT));
        } else {
            throw new IllegalStateException("Output setting already exists");
        }
    }

    @Override
    public void createSideConfig(int inputs, int outputs) {
        if (sideConfig == null) {
            this.sideConfig = Arrays.stream(Direction.values()).collect(Collectors.toMap(Function.identity(), dir -> new ArrayList<>(Stream.of(Collections.nCopies(inputs + 2, SideConfigEnums.SideConfigSetting.INPUT), Collections.nCopies(outputs, SideConfigEnums.SideConfigSetting.OUTPUT)).flatMap(Collection::stream).toList())));
        } else {
            throw new IllegalStateException("Output setting already exists");
        }
    }

    public OutputSetting getOutputSetting(int slot) {
        return outputSettings.get(slot);
    }

    public SideConfigSetting getSideConfig(int slot, Direction direction) {
        return sideConfig.get(direction).get(slot);
    }
}