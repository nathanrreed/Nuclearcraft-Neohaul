package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ChemicalReactorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.GASSES;

public class ChemicalReactorProvider {
    public ChemicalReactorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GASSES.get("nitrogen"), 250).addFluidInput(GASSES.get("hydrogen"), 750).addFluidResult(GASSES.get("ammonia"), 500).save(recipeOutput);

    }
}