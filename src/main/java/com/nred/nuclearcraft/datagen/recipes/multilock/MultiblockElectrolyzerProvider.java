package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.List;

import static com.nred.nuclearcraft.info.Fluids.sizedIngredient;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class MultiblockElectrolyzerProvider {
    public MultiblockElectrolyzerProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new MultiblockElectrolyzerRecipe(List.of(), List.of(SizedChanceFluidIngredient.of(net.minecraft.world.level.material.Fluids.WATER, 500)), List.of(), List.of(sizedIngredient(GAS_MAP.get("hydrogen"), 500), sizedIngredient(GAS_MAP.get("oxygen"), 250)), 1D, 1D, "hydroxide_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockElectrolyzerRecipe(List.of(), List.of(sizedIngredient(CUSTOM_FLUID_MAP.get("le_water"), 500)), List.of(), List.of(sizedIngredient(GAS_MAP.get("hydrogen"), 375), sizedIngredient(GAS_MAP.get("deuterium"), 125), sizedIngredient(GAS_MAP.get("oxygen"), 250)), 1D, 1D, "hydroxide_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockElectrolyzerRecipe(List.of(), List.of(sizedIngredient(CUSTOM_FLUID_MAP.get("he_water"), 500)), List.of(), List.of(sizedIngredient(GAS_MAP.get("hydrogen"), 250), sizedIngredient(GAS_MAP.get("deuterium"), 250), sizedIngredient(GAS_MAP.get("oxygen"), 250)), 1D, 1D, "hydroxide_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockElectrolyzerRecipe(List.of(), List.of(sizedIngredient(CUSTOM_FLUID_MAP.get("heavy_water"), 500)), List.of(), List.of(sizedIngredient(GAS_MAP.get("deuterium"), 500), sizedIngredient(GAS_MAP.get("oxygen"), 250)), 1D, 1D, "hydroxide_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockElectrolyzerRecipe(List.of(), List.of(sizedIngredient(ACID_MAP.get("hydrofluoric_acid"), 250)), List.of(), List.of(sizedIngredient(GAS_MAP.get("hydrogen"), 250), sizedIngredient(GAS_MAP.get("fluorine"), 250)), 1D, 1D, "fluoride_solution")).save(recipeOutput);
    }
}