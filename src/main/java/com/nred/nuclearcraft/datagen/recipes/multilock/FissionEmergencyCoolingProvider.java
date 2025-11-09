package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;

public class FissionEmergencyCoolingProvider {
    public FissionEmergencyCoolingProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionEmergencyCoolingRecipe(SizedChanceFluidIngredient.of(CUSTOM_FLUID_MAP.get("emergency_coolant").still.get(), 1), SizedChanceFluidIngredient.of(CUSTOM_FLUID_MAP.get("emergency_coolant_heated").still.get(), 1), 1.0)).save(recipeOutput);
    }
}