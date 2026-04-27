package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionCoolantHeaterRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.HOT_COOLANT_MAP;

public class FissionCoolantHeaterProvider {
    public FissionCoolantHeaterProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionCoolantHeaterRecipe(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater").toStack(), SizedChanceFluidIngredient.of(COOLANT_MAP.get("nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get("nak_hot").still.get(), 1), "standard_heater")).save(recipeOutput, "_fission_coolant_heater");
        for (String coolant : COOLANTS) {
            new BasicRecipeBuilder<>(new FissionCoolantHeaterRecipe(FISSION_REACTOR_MAP.get(coolant + "_fission_coolant_heater").toStack(), SizedChanceFluidIngredient.of(COOLANT_MAP.get(coolant + "_nak").still.get(), 1), SizedChanceFluidIngredient.of(HOT_COOLANT_MAP.get(coolant + "_nak_hot").still.get(), 1), coolant + "_heater")).save(recipeOutput, "_heater");
        }
    }
}