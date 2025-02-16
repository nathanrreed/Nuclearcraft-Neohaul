package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.SeparatorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.ingotDust;
import static com.nred.nuclearcraft.registration.ItemRegistration.URANIUM_MAP;

public class SeparatorProvider {
    public SeparatorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("uranium", 10)).addItemResult(URANIUM_MAP.get("238"), 9).addItemResult(URANIUM_MAP.get("235"), 1).save(recipeOutput);

    }
}