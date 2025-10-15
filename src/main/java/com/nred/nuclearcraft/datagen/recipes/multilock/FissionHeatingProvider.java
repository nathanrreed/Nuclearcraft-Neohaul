package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
import com.nred.nuclearcraft.recipe.fission.BasicRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import static com.nred.nuclearcraft.config.Config2.fission_heater_cooling_rate;
import static com.nred.nuclearcraft.info.Names.FISSION_HEAT_PARTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class FissionHeatingProvider {
    public FissionHeatingProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedFluidIngredient.of(Fluids.WATER, 1), SizedFluidIngredient.of(STEAM_MAP.get("high_pressure_steam").still.get(), 4), 64)).save(recipeOutput);
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedFluidIngredient.of(CUSTOM_FLUID_MAP.get("preheated_water").still.get(), 1), SizedFluidIngredient.of(STEAM_MAP.get("high_pressure_steam").still.get(), 4), 32)).save(recipeOutput);

        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedFluidIngredient.of(COOLANT_MAP.get("nak").still.get(), 1), SizedFluidIngredient.of(HOT_COOLANT_MAP.get("nak_hot").still.get(), 1), fission_heater_cooling_rate[0])).save(recipeOutput);
        for (int i = 1; i < FISSION_HEAT_PARTS.size(); ++i) {
            new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedFluidIngredient.of(COOLANT_MAP.get(FISSION_HEAT_PARTS.get(i) + "_nak").still.get(), 1), SizedFluidIngredient.of(HOT_COOLANT_MAP.get(FISSION_HEAT_PARTS.get(i) + "_nak_hot").still.get(), 1), fission_heater_cooling_rate[i])).save(recipeOutput);
        }
    }
}