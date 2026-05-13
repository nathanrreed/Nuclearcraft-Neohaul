package com.nred.nuclearcraft.recipe;

public class IngredientMatchResult {
    public static final IngredientMatchResult FAIL = new IngredientMatchResult(false);

    private final boolean match;

    public IngredientMatchResult(boolean match) {
        this.match = match;
    }

    public boolean matches() {
        return match;
    }
}