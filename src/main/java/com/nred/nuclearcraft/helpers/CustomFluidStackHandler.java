package com.nred.nuclearcraft.helpers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class CustomFluidStackHandler implements IFluidHandler, INBTSerializable<CompoundTag> {
    private final int capacity;
    private final int tanks;
    private final boolean allowInput;
    private final boolean allowOutput;
    protected NonNullList<FluidStack> fluids;
    public ArrayList<FluidOutputSetting> outputSettings;

    public enum FluidOutputSetting {
        DEFAULT, VOID_EXCESS, VOID;
    }

    public CustomFluidStackHandler(int capacity, int tanks, boolean allowInput, boolean allowOutput) {
        this.capacity = capacity;
        this.tanks = tanks;
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
        this.outputSettings = new ArrayList<>(Collections.nCopies(tanks, FluidOutputSetting.DEFAULT));

        setSize(tanks);
    }

    @Override
    public int getTanks() {
        return tanks;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        validateSlotIndex(tank);
        return fluids.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public abstract boolean isFluidValid(int tank, FluidStack stack);

    public abstract boolean canOutput(int tank);

    public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : fluids.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag fluidTags = tagList.getCompound(i);
            int slot = fluidTags.getInt("Slot");

            if (slot >= 0 && slot < fluids.size()) {
                FluidStack.parse(lookupProvider, fluidTags).ifPresent(fluid -> fluids.set(slot, fluid));
            }
        }
        this.outputSettings = new ArrayList<>(Arrays.stream(nbt.getIntArray("output_settings")).mapToObj(i -> FluidOutputSetting.values()[i]).toList());

        onLoad();
    }

    public void setSize(int size) {
        fluids = NonNullList.withSize(size, FluidStack.EMPTY);
        if (size > this.outputSettings.size()) {
            this.outputSettings.addAll(Collections.nCopies(size - this.outputSettings.size(), FluidOutputSetting.DEFAULT));
        } else if (size < this.outputSettings.size()) {
            this.outputSettings = new ArrayList<>(this.outputSettings.subList(0, size));
        }
        onContentsChanged();
    }

    public int getFluidAmount(int tank) {
        return fluids.get(tank).getAmount();
    }

    public void setFluid(int tank, FluidStack fluidStack) {
        validateSlotIndex(tank);
        fluids.set(tank, fluidStack);
        onContentsChanged();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.getTanks(); i++) {
            if (!fluids.get(i).isEmpty()) {
                CompoundTag fluidTag = new CompoundTag();
                fluidTag.putInt("Slot", i);
                nbtTagList.add(fluids.get(i).save(lookupProvider, fluidTag));
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", fluids.size());
        nbt.putIntArray("output_settings", outputSettings.stream().map(Enum::ordinal).toList());

        return nbt;
    }

    // Stop other mods using capability from doing unexpected things
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (allowInput) {
            return internalInsertFluid(resource, action);
        }
        return 0;
    }

    public int fill(int tank, FluidStack resource, FluidAction action) {
        if (!FluidStack.isSameFluidSameComponents(getFluidInTank(tank), resource) && !getFluidInTank(tank).isEmpty()) {
            return 0;
        }

        int amount = resource.getAmount();
        if (action.execute()) {
            setFluid(tank, resource.copyWithAmount(resource.getAmount() + getFluidAmount(tank)));
        }

        if (getFluidAmount(tank) + amount > capacity) {
            setFluid(tank, getFluidInTank(tank).copyWithAmount(capacity));
            return capacity - getFluidAmount(tank);
        }
        return amount;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (allowOutput) {
            return internalExtractFluid(resource, action);
        }
        return FluidStack.EMPTY;
    }

    public FluidStack drain(int tank, int maxDrain, FluidAction action) {
        FluidStack fluid = fluids.get(tank);
        if (fluid.isEmpty()) return FluidStack.EMPTY;
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = fluid.copyWithAmount(drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            onContentsChanged();
        }
        if (drained > 0) {
            return stack;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (allowOutput) {
            return internalExtractFluid(maxDrain, action);
        }
        return FluidStack.EMPTY;
    }

    // Normal insert and extract for use within the mod
    public int internalInsertFluid(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        }
        if (action.simulate()) {
            int amount = 0;
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (FluidStack fluid : fluids) {
                i++;
                if (!isFluidValid(i, resource)) continue;
                if (fluid.isEmpty()) {
                    amount += Math.min(capacity, resource.getAmount());
                }
                if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                    continue;
                }
                amount += Math.min(capacity - fluid.getAmount(), resource.getAmount());
            }
            return amount;
        } else {
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (FluidStack fluid : fluids) {
                i++;
                if (!isFluidValid(i, resource)) continue;
                if (fluid.isEmpty()) {
                    fluids.set(i, resource.copyWithAmount(Math.min(capacity, resource.getAmount())));
                    onContentsChanged();
                    return fluids.get(i).getAmount();
                }
                if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                    return 0;
                }
                int filled = capacity - fluid.getAmount();

                if (resource.getAmount() < filled) {
                    fluid.grow(resource.getAmount());
                    filled = resource.getAmount();
                } else {
                    fluid.setAmount(capacity);
                }
                if (filled > 0) {
                    onContentsChanged();
                    return filled;
                }
            }
            return 0;
        }
    }

    public FluidStack internalExtractFluid(int maxDrain, FluidAction action) {
        for (int i = 0; i < fluids.size(); i++) {
            FluidStack fluid = fluids.get(i);
            if (fluid.isEmpty() || !canOutput(i)) continue;
            int drained = maxDrain;
            if (fluid.getAmount() < drained) {
                drained = fluid.getAmount();
            }
            FluidStack stack = fluid.copyWithAmount(drained);
            if (action.execute() && drained > 0) {
                fluid.shrink(drained);
                onContentsChanged();
            }
            if (drained > 0) {
                return stack;
            }
        }
        return FluidStack.EMPTY;
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= fluids.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + fluids.size() + ")");
    }

    public FluidStack internalExtractFluid(FluidStack resource, FluidAction action) {
        if (fluids.stream().anyMatch(fluidStack -> fluidStack.is(resource.getFluid()))) {
            return internalExtractFluid(resource.getAmount(), action);
        }
        return FluidStack.EMPTY;
    }

    protected void onLoad() {
    }

    public void outputSetting(int index, boolean left) {
        this.outputSettings.set(index, next(!left, this.outputSettings.get(index)));
        onContentsChanged();
    }

    public FluidOutputSetting next(boolean reverse, FluidOutputSetting prev) {
        if (reverse) {
            return switch (prev) {
                case DEFAULT -> FluidOutputSetting.VOID;
                case VOID_EXCESS -> FluidOutputSetting.DEFAULT;
                case VOID -> FluidOutputSetting.VOID_EXCESS;
            };
        } else {
            return switch (prev) {
                case DEFAULT -> FluidOutputSetting.VOID_EXCESS;
                case VOID_EXCESS -> FluidOutputSetting.VOID;
                case VOID -> FluidOutputSetting.DEFAULT;
            };
        }
    }

    public FluidOutputSetting getOutputSetting(int slot) {
        return outputSettings.get(slot);
    }

    protected void onContentsChanged() {
    }
}