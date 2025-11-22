package com.nred.nuclearcraft.capability.radiation.resistance;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class RadiationResistance implements IRadiationResistance {
    private double baseRadResistance, shieldingRadResistance = 0D;

    public RadiationResistance(double baseRadResistance) {
        this.baseRadResistance = baseRadResistance;
    }

    @Override
    public CompoundTag writeNBT(IRadiationResistance instance, Direction side, CompoundTag nbt) {
        nbt.putDouble("radiationResistance", getBaseRadResistance());
        nbt.putDouble("shieldingRadResistance", getShieldingRadResistance());
        return nbt;
    }

    @Override
    public void readNBT(IRadiationResistance instance, Direction side, CompoundTag nbt) {
        setBaseRadResistance(nbt.getDouble("radiationResistance"));
        setShieldingRadResistance(nbt.getDouble("shieldingRadResistance"));
    }

    @Override
    public double getBaseRadResistance() {
        return baseRadResistance;
    }

    @Override
    public void setBaseRadResistance(double newResistance) {
        baseRadResistance = Math.max(newResistance, 0D);
    }

    @Override
    public double getShieldingRadResistance() {
        return shieldingRadResistance;
    }

    @Override
    public void setShieldingRadResistance(double newResistance) {
        shieldingRadResistance = Math.max(newResistance, 0D);
    }
}