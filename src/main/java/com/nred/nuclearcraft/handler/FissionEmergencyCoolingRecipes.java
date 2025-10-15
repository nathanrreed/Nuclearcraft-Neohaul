package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;

public class FissionEmergencyCoolingRecipes extends BasicRecipeHandler<FissionEmergencyCoolingRecipe> {
    public FissionEmergencyCoolingRecipes() {
        super("fission_emergency_cooling", 0, 1, 0, 1);
    }
}