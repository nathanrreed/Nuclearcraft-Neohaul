package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.InfiltratorPressureFluidRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.fluidTag;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class InfiltratorPressureFluidProvider {
    public InfiltratorPressureFluidProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new InfiltratorPressureFluidRecipe(FluidIngredient.of(GAS_MAP.get("nitrogen").still.get()), 0.8)).save(recipeOutput, "_infiltrator_pressure_fluid");
        new BasicRecipeBuilder<>(new InfiltratorPressureFluidRecipe(FluidIngredient.tag(fluidTag(Tags.Fluids.GASEOUS, "argon")), 0.9)).save(recipeOutput, "_infiltrator_pressure_fluid");
        new BasicRecipeBuilder<>(new InfiltratorPressureFluidRecipe(FluidIngredient.tag(fluidTag(Tags.Fluids.GASEOUS, "neon")), 0.9)).save(recipeOutput, "_infiltrator_pressure_fluid");
        new BasicRecipeBuilder<>(new InfiltratorPressureFluidRecipe(FluidIngredient.of(GAS_MAP.get("helium").still.get()), 1.0)).save(recipeOutput, "_infiltrator_pressure_fluid");
    }
}