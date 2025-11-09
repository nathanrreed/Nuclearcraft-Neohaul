package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.config.NCConfig.fission_heater_cooling_rate;
import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class FissionHeatingProvider {
    public FissionHeatingProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(Fluids.WATER, 1), SizedChanceFluidIngredient.of(STEAM_MAP.get("high_pressure_steam").still.get(), 4), 64, false)).save(recipeOutput);
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(CUSTOM_FLUID_MAP.get("preheated_water").still.get(), 1), SizedChanceFluidIngredient.of(STEAM_MAP.get("high_pressure_steam").still.get(), 4), 32, false)).save(recipeOutput);

        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(COOLANT_MAP.get("nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get("nak_hot").still.get(), 1), fission_heater_cooling_rate[0], true)).save(recipeOutput);
        for (int i = 0; i < COOLANTS.size(); ++i) {
            new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(COOLANT_MAP.get(COOLANTS.get(i) + "_nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get(COOLANTS.get(i) + "_nak_hot").still.get(), 1), fission_heater_cooling_rate[i + 1], true)).save(recipeOutput);
        }
    }
}