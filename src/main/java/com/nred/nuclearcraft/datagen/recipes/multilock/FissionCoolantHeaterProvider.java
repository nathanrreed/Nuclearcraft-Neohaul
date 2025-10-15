package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.fission.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import static com.nred.nuclearcraft.config.Config2.fission_heater_cooling_rate;
import static com.nred.nuclearcraft.info.Names.FISSION_HEAT_PARTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.HOT_COOLANT_MAP;

public class FissionCoolantHeaterProvider {
    public FissionCoolantHeaterProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionCoolantHeaterRecipe(SizedFluidIngredient.of(COOLANT_MAP.get("nak").still.get(), 1), SizedFluidIngredient.of(HOT_COOLANT_MAP.get("nak_hot").still.get(), 1), fission_heater_cooling_rate[0], "standard_heater")).save(recipeOutput, "_heater");
        for (int i = 1; i < FISSION_HEAT_PARTS.size(); ++i) {
            new BasicRecipeBuilder<>(new FissionCoolantHeaterRecipe(SizedFluidIngredient.of(COOLANT_MAP.get(FISSION_HEAT_PARTS.get(i) + "_nak").still.get(), 1), SizedFluidIngredient.of(HOT_COOLANT_MAP.get(FISSION_HEAT_PARTS.get(i) + "_nak_hot").still.get(), 1), fission_heater_cooling_rate[i], FISSION_HEAT_PARTS.get(i) + "_heater")).save(recipeOutput, "_heater");
        }
    }
}