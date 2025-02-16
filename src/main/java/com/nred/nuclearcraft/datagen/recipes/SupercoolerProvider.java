package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.SupercoolerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID;
import static com.nred.nuclearcraft.registration.FluidRegistration.GASSES;

public class SupercoolerProvider {
    public SupercoolerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 1, 1).addFluidInput(GASSES.get("helium"), 8000).addFluidResult(CUSTOM_FLUID.get("liquid_helium"), 25).save(recipeOutput);

    }
}