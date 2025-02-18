package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.SeparatorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.ingotDust;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class SeparatorProvider {
    public SeparatorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("uranium", 10)).addItemResult(URANIUM_MAP.get("238"), 9).addItemResult(URANIUM_MAP.get("235"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("boron", 12)).addItemResult(BORON_MAP.get("11"), 9).addItemResult(BORON_MAP.get("10"), 3).save(recipeOutput);
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("lithium", 10)).addItemResult(LITHIUM_MAP.get("7"), 9).addItemResult(LITHIUM_MAP.get("6"), 1).save(recipeOutput);
    }
}