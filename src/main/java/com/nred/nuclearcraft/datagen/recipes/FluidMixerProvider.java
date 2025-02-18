package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidMixerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.MOLTEN_MAP;

public class FluidMixerProvider {
    public FluidMixerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidMixerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("sodium"), 72).addFluidInput(MOLTEN_MAP.get("potassium"), 288).addFluidResult(COOLANT_MAP.get("nak"), 144).save(recipeOutput);

    }
}