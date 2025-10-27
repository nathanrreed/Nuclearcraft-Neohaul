package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.SupercoolerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class SupercoolerProvider {
    public SupercoolerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("helium"), 8000).addFluidResult(CUSTOM_FLUID_MAP.get("liquid_helium"), 25).save(recipeOutput);
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("nitrogen"), 8000).addFluidResult(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), 25).save(recipeOutput);
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 0.25, 0.5).addFluidInput(Fluids.WATER, 250).addFluidResult(CUSTOM_FLUID_MAP.get("ice"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(SupercoolerRecipe.class, 0.25, 1).addFluidInput(CUSTOM_FLUID_MAP.get("emergency_coolant_heated"), 250).addFluidResult(CUSTOM_FLUID_MAP.get("emergency_coolant"), 250).save(recipeOutput);
    }
}