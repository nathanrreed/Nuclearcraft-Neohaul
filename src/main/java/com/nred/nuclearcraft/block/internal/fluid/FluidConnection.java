package com.nred.nuclearcraft.block.internal.fluid;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FluidConnection {
    private final @Nonnull List<TankSorption> sorptionList;
    private final @Nonnull List<TankSorption> defaultSorptions;

    public FluidConnection(@Nonnull List<TankSorption> sorptionList) {
        this.sorptionList = new ArrayList<>(sorptionList);
        defaultSorptions = new ArrayList<>(sorptionList);
    }

    private FluidConnection(@Nonnull FluidConnection connection) {
        sorptionList = new ArrayList<>(connection.sorptionList);
        defaultSorptions = new ArrayList<>(connection.defaultSorptions);
    }

    private FluidConnection copy() {
        return new FluidConnection(this);
    }

    public static FluidConnection[] cloneArray(@Nonnull FluidConnection[] connections) {
        FluidConnection[] clone = new FluidConnection[6];
        for (int i = 0; i < 6; ++i) {
            clone[i] = connections[i].copy();
        }
        return clone;
    }

    public TankSorption getTankSorption(int tankNumber) {
        return sorptionList.get(tankNumber);
    }

    public void setTankSorption(int tankNumber, TankSorption sorption) {
        sorptionList.set(tankNumber, sorption);
    }

    public TankSorption getDefaultTankSorption(int slot) {
        return defaultSorptions.get(slot);
    }

    public boolean canConnect() {
        for (TankSorption sorption : sorptionList) {
            if (sorption.canConnect()) {
                return true;
            }
        }
        return false;
    }

    public boolean anyOfSorption(TankSorption tankSorption) {
        for (TankSorption sorption : sorptionList) {
            if (sorption.equals(tankSorption)) {
                return true;
            }
        }
        return false;
    }

    public void toggleTankSorption(int tankNumber, TankSorption.Type type, boolean reverse) {
        setTankSorption(tankNumber, getTankSorption(tankNumber).next(type, reverse));
    }

    public final CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider registries, @Nonnull Direction side) {
        CompoundTag connectionTag = new CompoundTag();
        for (int i = 0; i < sorptionList.size(); ++i) {
            connectionTag.putInt("sorption" + i, getTankSorption(i).ordinal());
        }
        nbt.put("fluidConnection" + side.ordinal(), connectionTag);
        return nbt;

    }

    public final FluidConnection readFromNBT(CompoundTag nbt, HolderLookup.Provider registries, @Nonnull Direction side) {
        if (nbt.contains("fluidConnection" + side.ordinal())) {
            CompoundTag connectionTag = nbt.getCompound("fluidConnection" + side.ordinal());
            for (int i = 0; i < sorptionList.size(); ++i) {
                if (connectionTag.contains("sorption" + i)) {
                    setTankSorption(i, TankSorption.values()[connectionTag.getInt("sorption" + i)]);
                }
            }

        }
        return this;
    }
}