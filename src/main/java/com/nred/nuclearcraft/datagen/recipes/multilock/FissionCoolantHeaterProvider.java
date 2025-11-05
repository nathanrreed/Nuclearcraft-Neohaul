package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.fission.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.config.NCConfig.fission_heater_cooling_rate;
import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.HOT_COOLANT_MAP;

public class FissionCoolantHeaterProvider {
    public FissionCoolantHeaterProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionCoolantHeaterRecipe(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater").toStack(), SizedChanceFluidIngredient.of(COOLANT_MAP.get("nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get("nak_hot").still.get(), 1), fission_heater_cooling_rate[0], "standard_heater")).save(recipeOutput, "_fission_coolant_heater");
        for (int i = 0; i < COOLANTS.size(); ++i) {
            new BasicRecipeBuilder<>(new FissionCoolantHeaterRecipe(FISSION_REACTOR_MAP.get(COOLANTS.get(i) + "_fission_coolant_heater").toStack(), SizedChanceFluidIngredient.of(COOLANT_MAP.get(COOLANTS.get(i) + "_nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get(COOLANTS.get(i) + "_nak_hot").still.get(), 1), fission_heater_cooling_rate[i + 1], COOLANTS.get(i) + "_heater")).save(recipeOutput, "_heater");
        }
    }
}