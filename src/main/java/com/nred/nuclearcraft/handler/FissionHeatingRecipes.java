package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;

public class FissionHeatingRecipes extends BasicRecipeHandler<FissionHeatingRecipe> {
    public FissionHeatingRecipes() {
        super("fission_heating", 0, 1, 0, 1);
    }
}