package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.CentrifugeRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.MOLTEN_MAP;

public class CentrifugeProvider {
    public CentrifugeProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(CentrifugeRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron"), 192).addFluidResult(MOLTEN_MAP.get("boron_11"), 144).addFluidResult(MOLTEN_MAP.get("boron_10"), 48).save(recipeOutput);

    }
}