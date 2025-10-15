package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;

public class CoolantHeaterRecipes extends BasicRecipeHandler<FissionCoolantHeaterRecipe> {
    public CoolantHeaterRecipes() {
        super("coolant_heater", 1, 1, 0, 1);
    }
}