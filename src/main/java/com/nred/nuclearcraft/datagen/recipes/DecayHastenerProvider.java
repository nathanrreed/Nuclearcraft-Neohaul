package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.DecayHastenerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.ItemRegistration.DUST_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.INGOT_MAP;

public class DecayHastenerProvider {
    public DecayHastenerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("thorium"), 1).addItemResult(DUST_MAP.get("lead"), 1).save(recipeOutput);

    }
}