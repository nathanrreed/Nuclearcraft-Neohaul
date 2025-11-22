package com.nred.nuclearcraft.capability.radiation.sink;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class RadiationSink implements IRadiationSink {
    private double radiationLevel;

    public RadiationSink(double startRadiation) {
        radiationLevel = startRadiation;
    }

    @Override
    public CompoundTag writeNBT(IRadiationSink instance, Direction side, CompoundTag nbt) {
        nbt.putDouble("radiationLevel", getRadiationLevel());
        return nbt;
    }

    @Override
    public void readNBT(IRadiationSink instance, Direction side, CompoundTag nbt) {
        setRadiationLevel(nbt.getDouble("radiationLevel"));
    }

    @Override
    public double getRadiationLevel() {
        return radiationLevel;
    }

    @Override
    public void setRadiationLevel(double newRads) {
        radiationLevel = Math.max(newRads, 0D);
    }
}