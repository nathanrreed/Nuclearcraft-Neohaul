package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidEnricherRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.registration.FluidRegistration.SALT_SOLUTION_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEM_DUST_MAP;

public class FluidEnricherProvider {
    public FluidEnricherProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("boron_nitride"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("boron_nitride_solution"), 666).save(recipeOutput);

    }
}