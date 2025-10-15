package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.fission.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;

public class FissionEmergencyCoolingProvider {
    public FissionEmergencyCoolingProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionEmergencyCoolingRecipe(SizedFluidIngredient.of(CUSTOM_FLUID_MAP.get("emergency_coolant").still.get(), 1), SizedFluidIngredient.of(CUSTOM_FLUID_MAP.get("emergency_coolant_heated").still.get(), 1), 1.0)).save(recipeOutput);
    }
}