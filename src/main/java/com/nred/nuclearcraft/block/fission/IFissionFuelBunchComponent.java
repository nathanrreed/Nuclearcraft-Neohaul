package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionFuelBunch;

import javax.annotation.Nullable;

public interface IFissionFuelBunchComponent extends IFissionFuelComponent {

    @Nullable
    FissionFuelBunch getFuelBunch();

    void setFuelBunch(@Nullable FissionFuelBunch fuelBunch);

    default int getFuelBunchSize() {
        FissionFuelBunch fuelBunch = getFuelBunch();
        return fuelBunch == null ? 0 : fuelBunch.fuelComponentMap.size();
    }

    long getIndividualFlux();

    long getIndividualHeatMultiplier(boolean simulate);
}
