package com.nred.nuclearcraft.recipe.machine;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipe;

import java.util.List;

public class MultiblockMachine extends BasicRecipe {
    private final double timeModifier;
    private final double powerModifier;
    private final double radiation;

    public MultiblockMachine(List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients, List<SizedChanceItemIngredient> itemProducts, List<SizedChanceFluidIngredient> fluidProducts, double timeModifier, double powerModifier) {
        super(itemIngredients, fluidIngredients, itemProducts, fluidProducts);
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
        this.radiation = 0; // TODO
    }

    public double getProcessTimeMultiplier() {
        return timeModifier;
    }

    public double getProcessPowerMultiplier() {
        return powerModifier;
    }

    public double getBaseProcessTime(double defaultProcessTime) {
        return getProcessTimeMultiplier() * defaultProcessTime;
    }

    public double getBaseProcessPower(double defaultProcessPower) {
        return getProcessPowerMultiplier() * defaultProcessPower;
    }

    public double getBaseProcessRadiation() {
        return radiation;
    }
}