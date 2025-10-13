package com.nred.nuclearcraft.helpers;

import com.nred.nuclearcraft.block.internal.fluid.Tank;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public abstract class CustomFluidTankHandler implements IFluidHandler, INBTSerializable<CompoundTag> { // TODO REMOVE
    private final int numTanks;
    protected NonNullList<Tank> tanks;

    public CustomFluidTankHandler(List<Tank> tanks) {
        this.numTanks = tanks.size();
        this.tanks = NonNullList.copyOf(tanks);
    }

    @Override
    public int getTanks() {
        return numTanks;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        validateSlotIndex(tank);
        return tanks.get(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return tanks.get(tank).getCapacity();
    }

    @Override
    public abstract boolean isFluidValid(int tank, FluidStack stack);

    public abstract boolean canOutput(int tank);

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : tanks.size());

        ListTag tagList = nbt.getList("Tanks", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag fluidTags = tagList.getCompound(i);

            if (i < tanks.size()) {
                tanks.set(i, Tank.of(provider, fluidTags, "Slot" + i));
            }
        }

        onLoad();
    }

    public int getFluidAmount(int tank) {
        return tanks.get(tank).getFluidAmount();
    }

    public Tank getFirst() {
        return tanks.getFirst();
    }

    public Tank get(int i) {
        return tanks.get(i);
    }

    public List<FluidStack> getFluids() {
        return tanks.stream().map(FluidTank::getFluid).toList();
    }

    public void setFluid(int tank, FluidStack fluidStack) {
        validateSlotIndex(tank);
        tanks.get(tank).setFluid(fluidStack);
        onContentsChanged();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < this.getTanks(); i++) {
            CompoundTag tankTag = new CompoundTag();
            nbtTagList.add(tanks.get(i).writeToNBT(tankTag, provider, "Slot" + i));
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Tanks", nbtTagList);
        nbt.putInt("Size", tanks.size());

        return nbt;
    }

    protected void onLoad() {
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= tanks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + tanks.size() + ")");
    }

    public void setSize(int size) {
        tanks = NonNullList.withSize(size, new Tank(0, new HashSet<>()));
        onContentsChanged();
    }

    // Stop other mods using capability from doing unexpected things
    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return internalInsertFluid(resource, action);
    }

    public int fill(int tank, FluidStack resource, FluidAction action) {
        if (!FluidStack.isSameFluidSameComponents(getFluidInTank(tank), resource) && !getFluidInTank(tank).isEmpty()) {
            return 0;
        }

        int amount = resource.getAmount();
        if (action.execute()) {
            setFluid(tank, resource.copyWithAmount(resource.getAmount() + getFluidAmount(tank)));
        }

        int capacity = tanks.get(tank).getCapacity();
        if (getFluidAmount(tank) + amount > capacity) {
            setFluid(tank, getFluidInTank(tank).copyWithAmount(capacity));
            return capacity - getFluidAmount(tank);
        }
        return amount;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return internalExtractFluid(resource, action);
    }

    public FluidStack drain(int tank, int maxDrain, FluidAction action) {
        FluidStack fluid = tanks.get(tank).getFluid();
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
        return internalExtractFluid(maxDrain, action);
    }

    // Normal insert and extract for use within the mod
    public int internalInsertFluid(FluidStack resource, FluidAction action) {
        return internalInsertFluid(resource, action, false, Direction.NORTH);
    }

    public int internalInsertFluid(FluidStack resource, FluidAction action, boolean check, Direction dir) {
        if (resource.isEmpty()) {
            return 0;
        }
        if (action.simulate()) {
            int amount = 0;
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (Tank tank : tanks) {
                i++;
                if (check || !isFluidValid(i, resource)) continue;
                if (tank.isEmpty()) {
                    amount += Math.min(tank.getCapacity(), resource.getAmount());
                }
                if (!FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
                    continue;
                }
                amount += Math.min(tank.getCapacity() - tank.getFluidAmount(), resource.getAmount());
            }
            return amount;
        } else {
            int i = -1; // Will start at 0 always since i++ is right after, just makes continues easier
            for (Tank tank : tanks) {
                i++;
                if (check  || !isFluidValid(i, resource)) continue;
                if (tank.isEmpty()) {
                    tank.setFluid(resource.copyWithAmount(Math.min(tank.getCapacity(), resource.getAmount())));
                    onContentsChanged();
                    return tank.getFluidAmount();
                }
                if (!FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
                    return 0;
                }
                int filled = tank.getCapacity() - tank.getFluidAmount();

                if (resource.getAmount() < filled) {
                    tank.getFluid().grow(resource.getAmount());
                    filled = resource.getAmount();
                } else {
                    tank.getFluid().setAmount(tank.getCapacity());
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
        return internalExtractFluid(maxDrain, action, false, Direction.NORTH);
    }

    public FluidStack internalExtractFluid(int maxDrain, FluidAction action, boolean check, Direction dir) {
        for (int i = 0; i < tanks.size(); i++) {
            FluidStack fluid = tanks.get(i).getFluid();
            if (check  || fluid.isEmpty() || !canOutput(i)) continue;
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


    public FluidStack internalExtractFluid(FluidStack resource, FluidAction action) {
        return internalExtractFluid(resource, action, false, Direction.NORTH);
    }

    public FluidStack internalExtractFluid(FluidStack resource, FluidAction action, boolean check, Direction dir) {
        if (tanks.stream().anyMatch(tank -> tank.getFluid().is(resource.getFluid()))) {
            return internalExtractFluid(resource.getAmount(), action, check, dir);
        }
        return FluidStack.EMPTY;
    }

    protected void onContentsChanged() {
    }
}