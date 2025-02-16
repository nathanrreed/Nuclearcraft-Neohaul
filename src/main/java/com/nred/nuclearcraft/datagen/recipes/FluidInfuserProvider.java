package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidInfuserRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.GASSES;
import static com.nred.nuclearcraft.registration.ItemRegistration.INGOT_MAP;

public class FluidInfuserProvider {
    public FluidInfuserProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("manganese"), 1).addFluidInput(GASSES.get("oxygen"), 1000).addItemResult(INGOT_MAP.get("manganese_oxide"), 1).save(recipeOutput);

    }
}