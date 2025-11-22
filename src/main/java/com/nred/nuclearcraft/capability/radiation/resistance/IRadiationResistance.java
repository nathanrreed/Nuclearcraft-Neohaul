package com.nred.nuclearcraft.capability.radiation.resistance;

import com.nred.nuclearcraft.capability.ICapability;

public interface IRadiationResistance extends ICapability<IRadiationResistance> {
    default double getTotalRadResistance() {
        return getBaseRadResistance() + getShieldingRadResistance();
    }

    double getBaseRadResistance();

    void setBaseRadResistance(double newResistance);

    double getShieldingRadResistance();

    void setShieldingRadResistance(double newResistance);
}