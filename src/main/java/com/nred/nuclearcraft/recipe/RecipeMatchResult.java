package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.handler.AbstractRecipeHandler;
import it.unimi.dsi.fastutil.ints.IntList;

public final class RecipeMatchResult {
    public static final RecipeMatchResult FAIL = new RecipeMatchResult(false, AbstractRecipeHandler.INVALID, AbstractRecipeHandler.INVALID);

    public final boolean isMatch;

    public final IntList itemInputOrder, fluidInputOrder;

    public RecipeMatchResult(boolean isMatch, IntList itemInputOrder, IntList fluidInputOrder) {
        this.isMatch = isMatch;
        this.itemInputOrder = itemInputOrder;
        this.fluidInputOrder = fluidInputOrder;
    }
}