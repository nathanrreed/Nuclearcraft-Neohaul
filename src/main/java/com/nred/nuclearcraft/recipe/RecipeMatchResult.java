package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.handler.AbstractRecipeHandler;
import it.unimi.dsi.fastutil.ints.IntList;

public final class RecipeMatchResult {
    public static final RecipeMatchResult FAIL = new RecipeMatchResult(false, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID);

    public final boolean isMatch;

    public final IntList itemIngredientNumbers, fluidIngredientNumbers, itemInputOrder, fluidInputOrder;

    public RecipeMatchResult(boolean isMatch, IntList itemIngredientNumbers, IntList fluidIngredientNumbers, IntList itemInputOrder, IntList fluidInputOrder) {
        this.isMatch = isMatch;
        this.itemIngredientNumbers = itemIngredientNumbers;
        this.fluidIngredientNumbers = fluidIngredientNumbers;
        this.itemInputOrder = itemInputOrder;
        this.fluidInputOrder = fluidInputOrder;
    }
}