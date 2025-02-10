package com.nred.nuclearcraft.helpers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class CustomFluidStackHandler implements IFluidHandler, INBTSerializable<CompoundTag> {
    private final int capacity;
    private final int tanks;
    private final boolean allowInput;
    private final boolean allowOutput;
    protected NonNullList<FluidStack> fluids;

    public CustomFluidStackHandler(int capacity, int tanks, boolean allowInput, boolean allowOutput) {
        this.capacity = capacity;
        this.tanks = tanks;
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
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
    public boolean isFluidValid(int tank, FluidStack stack) {
        return false; //TODO
    }

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
        onLoad();
    }

    public void setSize(int size) {
        fluids = NonNullList.withSize(size, FluidStack.EMPTY);
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

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (allowOutput) {
            return internalExtractFluid(resource, action);
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
        if (resource.isEmpty()) {//TODO
            return 0;
        }
        if (action.simulate()) {
            int amount = 0;
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (FluidStack fluid : fluids) {
                i++;
                if (!isFluidValid(i, fluid)) continue;
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
            int filled = 0;
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (FluidStack fluid : fluids) {
                i++;
                if (!isFluidValid(i, fluid)) continue;
                if (fluid.isEmpty()) {
                    int change = Math.min(capacity, resource.getAmount());
                    fluid.grow(change);
                    resource.shrink(change);
                    filled += change;
                }
                if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                    continue;
                }
                int change = Math.min(capacity - fluid.getAmount(), resource.getAmount());
                fluid.grow(change);
                resource.shrink(change);
                filled += change;
            }
            if (filled > 0)
                onContentsChanged();
            return filled;
        }
    }

    public FluidStack internalExtractFluid(int maxDrain, FluidAction simulate) {
        return FluidStack.EMPTY;  //TODO
//        int drained = maxDrain;
//        if (fluid.getAmount() < drained) {
//            drained = fluid.getAmount();
//        }
//        FluidStack stack = fluid.copyWithAmount(drained);
//        if (action.execute() && drained > 0) {
//            fluid.shrink(drained);
//            onContentsChanged();
//        }
//        return stack;
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= fluids.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + fluids.size() + ")");
    }

    public FluidStack internalExtractFluid(FluidStack resource, FluidAction simulate) {
        return FluidStack.EMPTY; //TODO
    }

    protected void onLoad() {
    }

    protected void onContentsChanged() {
    }
}