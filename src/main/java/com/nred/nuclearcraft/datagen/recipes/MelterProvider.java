package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.MelterRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.ingotDust;
import static com.nred.nuclearcraft.registration.FluidRegistration.MOLTEN;

public class MelterProvider {
    public MelterProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(ingotDust("steel", 1)).addFluidResult(MOLTEN.get("steel"), 144).save(recipeOutput);

    }
}