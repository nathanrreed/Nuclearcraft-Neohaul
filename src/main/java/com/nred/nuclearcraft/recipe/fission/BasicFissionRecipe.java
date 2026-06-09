package com.nred.nuclearcraft.recipe.fission;

import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;

import java.util.List;

public abstract class BasicFissionRecipe extends BasicRecipe {
    protected final double time;
    protected final int heat;
    protected final double efficiency;
    protected final int criticality;
    protected final double decayFactor;
    protected final boolean selfPriming;
    protected final double radiation;
    protected final int intrinsic_flux;

    public BasicFissionRecipe(List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients, List<SizedChanceItemIngredient> itemProducts, List<SizedChanceFluidIngredient> fluidProducts, double time, int heat, double efficiency, int criticality, int intrinsic_flux, double decayFactor, boolean selfPriming, double radiation) {
        super(itemIngredients, fluidIngredients, itemProducts, fluidProducts);
        this.time = time;
        this.heat = heat;
        this.efficiency = efficiency;
        this.criticality = criticality;
        this.intrinsic_flux = intrinsic_flux;
        this.decayFactor = decayFactor;
        this.selfPriming = selfPriming;
        this.radiation = radiation;
    }

    public abstract int getFissionFuelHeatRaw();

    public abstract int getFissionFuelHeat();

    public abstract double getFissionFuelEfficiencyRaw();

    public abstract double getFissionFuelEfficiency();

    public abstract int getFissionFuelCriticality();

    public abstract int getFissionFuelIntrinsicFlux();

    public abstract double getFissionFuelDecayFactor();

    public abstract boolean getFissionFuelSelfPriming();

    public abstract double getFissionFuelRadiationRaw();

    public abstract double getFissionFuelRadiation();
}