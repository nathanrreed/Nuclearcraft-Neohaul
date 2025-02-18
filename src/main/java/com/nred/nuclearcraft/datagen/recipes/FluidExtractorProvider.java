package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidExtractorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.CHOCOLATE_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.FOOD_MAP;

public class FluidExtractorProvider {
    public FluidExtractorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidExtractorRecipe.class, 1, 1).addItemInput(FOOD_MAP.get("ground_cocoa_nibs"), 1).addItemResult(FOOD_MAP.get("cocoa_solids"), 1).addFluidResult(CHOCOLATE_MAP.get("cocoa_butter"), 144).save(recipeOutput);

    }
}