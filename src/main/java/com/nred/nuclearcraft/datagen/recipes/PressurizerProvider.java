package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.PressurizerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import static com.nred.nuclearcraft.registration.ItemRegistration.DUST_MAP;

public class PressurizerProvider {
    public PressurizerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(DUST_MAP.get("graphite"), 1).addItemResult(Items.COAL, 1).save(recipeOutput);

    }
}