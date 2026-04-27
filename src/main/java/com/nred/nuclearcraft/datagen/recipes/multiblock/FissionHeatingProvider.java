package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class FissionHeatingProvider {
    public FissionHeatingProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(Fluids.WATER, 1), SizedChanceFluidIngredient.of(STEAM_MAP.get("high_pressure_steam").still.get(), 4), 64)).save(recipeOutput);
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(CUSTOM_FLUID_MAP.get("preheated_water").still.get(), 1), SizedChanceFluidIngredient.of(STEAM_MAP.get("high_pressure_steam").still.get(), 4), 32)).save(recipeOutput);

        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(COOLANT_MAP.get("nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get("nak_hot").still.get(), 1), "standard")).save(recipeOutput);
        for (String coolant : COOLANTS) {
            new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(COOLANT_MAP.get(coolant + "_nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get(coolant + "_nak_hot").still.get(), 1), coolant)).save(recipeOutput);
        }
    }
}