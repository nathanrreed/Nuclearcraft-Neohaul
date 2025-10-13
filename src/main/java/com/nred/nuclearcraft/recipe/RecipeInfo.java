package com.nred.nuclearcraft.recipe;

import it.unimi.dsi.fastutil.ints.IntList;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class RecipeInfo<T extends IRecipe> {

    public final @Nonnull T recipe;

    public final RecipeMatchResult matchResult;

    public RecipeInfo(@Nonnull T recipe, RecipeMatchResult matchResult) {
        this.recipe = recipe;
        this.matchResult = matchResult;
    }

    /**
     * Already takes item input order into account!
     */
    public IntList getItemIngredientNumbers() {
        return matchResult.itemIngredientNumbers;
    }

    /**
     * Already takes fluid input order into account!
     */
    public IntList getFluidIngredientNumbers() {
        return matchResult.fluidIngredientNumbers;
    }

    public IntList getItemInputOrder() {
        return matchResult.itemInputOrder;
    }

    public IntList getFluidInputOrder() {
        return matchResult.fluidInputOrder;
    }

    // Recipe Unit Info

    public RecipeUnitInfo getRecipeUnitInfo(double baseRateMultiplier) {
        if (matchResult == null || !matchResult.isMatch) {
            return RecipeUnitInfo.DEFAULT;
        }

        List<SizedIngredient> itemIngredients = recipe.getItemIngredients();
        List<SizedFluidIngredient> fluidIngredients = recipe.getFluidIngredients();

        Predicate<SizedIngredient> notEmptyItem = x -> !x.ingredient().hasNoItems();
        Predicate<SizedFluidIngredient> notEmptyFluid = x -> !x.ingredient().hasNoFluids();
        long itemInputCount = itemIngredients.stream().filter(notEmptyItem).count();
        long fluidInputCount = fluidIngredients.stream().filter(notEmptyFluid).count();

        if (itemInputCount == 1 && fluidInputCount == 0) {
            IntList itemInputOrder = getItemInputOrder();
            for (int i = 0, len = itemIngredients.size(); i < len; ++i) {
                SizedIngredient itemIngredient = itemIngredients.get(itemInputOrder.get(i));
                if (!itemIngredient.ingredient().hasNoItems()) {
                    return new RecipeUnitInfo("I/t", 0, baseRateMultiplier * itemIngredient.count());
                }
            }
        } else if (itemInputCount == 0 && fluidInputCount == 1) {
            IntList fluidInputOrder = getFluidInputOrder();
            for (int i = 0, len = fluidIngredients.size(); i < len; ++i) {
                SizedFluidIngredient fluidIngredient = fluidIngredients.get(fluidInputOrder.get(i));
                if (!fluidIngredient.ingredient().hasNoFluids()) {
                    return new RecipeUnitInfo("B/t", -1, baseRateMultiplier * fluidIngredient.amount());
                }
            }
        }

        return RecipeUnitInfo.DEFAULT.withRateMultiplier(baseRateMultiplier);
    }
}