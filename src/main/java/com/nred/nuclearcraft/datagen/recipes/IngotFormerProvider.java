package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.IngotFormerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.MOLTEN_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.ALLOY_MAP;

public class IngotFormerProvider {
    public IngotFormerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("steel"), 144).addItemResult(ALLOY_MAP.get("steel"), 1).save(recipeOutput);

    }
}