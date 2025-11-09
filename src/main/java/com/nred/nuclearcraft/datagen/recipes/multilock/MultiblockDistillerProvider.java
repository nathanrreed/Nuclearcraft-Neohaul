package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.MultiblockDistillerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.List;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class MultiblockDistillerProvider {
    public MultiblockDistillerProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new MultiblockDistillerRecipe(List.of(SizedChanceFluidIngredient.of(net.minecraft.world.level.material.Fluids.WATER, 250), Fluids.sizedIngredient(GAS_MAP.get("hydrogen_sulfide"), 250)), List.of(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("le_water"), 250), Fluids.sizedIngredient(GAS_MAP.get("depleted_hydrogen_sulfide"), 250)), 1.0, 0.5)).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockDistillerRecipe(List.of(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("le_water"), 250), Fluids.sizedIngredient(GAS_MAP.get("hydrogen_sulfide"), 250)), List.of(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("he_water"), 250), Fluids.sizedIngredient(GAS_MAP.get("depleted_hydrogen_sulfide"), 250)), 1.0, 0.5)).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockDistillerRecipe(List.of(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("he_water"), 250), Fluids.sizedIngredient(GAS_MAP.get("hydrogen_sulfide"), 250)), List.of(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("heavy_water"), 250), Fluids.sizedIngredient(GAS_MAP.get("depleted_hydrogen_sulfide"), 250)), 1.0, 0.5)).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockDistillerRecipe(List.of(SizedChanceFluidIngredient.of(net.minecraft.world.level.material.Fluids.WATER, 250), Fluids.sizedIngredient(GAS_MAP.get("depleted_hydrogen_sulfide"), 250)), List.of(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("preheated_water"), 250), Fluids.sizedIngredient(GAS_MAP.get("hydrogen_sulfide"), 250)), 0.5, 1.0)).save(recipeOutput);
    }
}