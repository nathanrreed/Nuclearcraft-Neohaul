package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.CrystallizerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.SALT_SOLUTION;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEM_DUST_MAP;

public class CrystallizerProvider {
    public CrystallizerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION.get("boron_nitride_solution"), 666).addItemResult(GEM_DUST_MAP.get("boron_nitride"), 1).save(recipeOutput);

    }
}