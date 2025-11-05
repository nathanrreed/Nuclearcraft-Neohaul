package com.nred.nuclearcraft.recipe.fission;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.NCMath;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;

public abstract class ItemFissionRecipe extends BasicRecipe {
    private final int time;
    private final int heat;
    private final double efficiency;
    private final int criticality;
    private final double decayFactor;
    private final boolean selfPriming;
    private final double radiation;

    public ItemFissionRecipe(SizedChanceItemIngredient ingredient, SizedChanceItemIngredient product, int time, int heat, double efficiency, int criticality, double decayFactor, boolean selfPriming, double radiation) {
        super(List.of(ingredient), List.of(), List.of(product), List.of());
        this.time = time;
        this.heat = heat;
        this.efficiency = efficiency;
        this.criticality = criticality;
        this.decayFactor = decayFactor;
        this.selfPriming = selfPriming;
        this.radiation = radiation;
    }

    public int getFissionFuelTimeRaw() {
        return NCMath.toInt(time);
    }

    public int getFissionFuelTime() {
        return NCMath.toInt(fission_fuel_time_multiplier * time);
    }

    public int getFissionFuelHeatRaw() {
        return NCMath.toInt(heat);
    }

    public int getFissionFuelHeat() {
        return NCMath.toInt(fission_fuel_heat_multiplier * heat);
    }

    public double getFissionFuelEfficiencyRaw() {
        return efficiency;
    }

    public double getFissionFuelEfficiency() {
        return fission_fuel_efficiency_multiplier * efficiency;
    }

    public int getFissionFuelCriticality() {
        return criticality;
    }

    public double getFissionFuelDecayFactor() {
        return decayFactor;
    }

    public boolean getFissionFuelSelfPriming() {
        return selfPriming;
    }

    public double getFissionFuelRadiationRaw() {
        return radiation;
    }

    public double getFissionFuelRadiation() {
        return fission_fuel_radiation_multiplier * radiation;
    }
}