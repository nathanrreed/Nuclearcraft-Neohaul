package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ElectrolyzerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class ElectrolyzerProvider {
    public ElectrolyzerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(Fluids.WATER, 500).addFluidResult(GAS_MAP.get("hydrogen"), 500).addFluidResult(GAS_MAP.get("oxygen"), 250).save(recipeOutput);

    }
}