package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.info.NCFluid.sizedIngredient;
import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.info.Names.GAS_COOLANTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class FissionHeatingProvider {
    public FissionHeatingProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(SizedChanceFluidIngredient.of(Fluids.WATER, 1), sizedIngredient(STEAM_MAP.get("high_pressure_steam"), 4), 64)).save(recipeOutput);
        new BasicRecipeBuilder<>(new FissionHeatingRecipe(sizedIngredient(CUSTOM_FLUID_MAP.get("preheated_water"), 1), sizedIngredient(STEAM_MAP.get("high_pressure_steam"), 4), 32)).save(recipeOutput);

        // Gas -> Hot Gas

        for (String coolant : GAS_COOLANTS) {
            new BasicRecipeBuilder<>(new FissionHeatingRecipe(sizedIngredient(GAS_MAP.get(coolant), 1), sizedIngredient(HOT_GAS_MAP.get(coolant + "_hot"), 2), FissionHeatingRecipe.RecipeHeatingType.GAS)).save(recipeOutput);
        }

        // NaK -> Hot NaK

        new BasicRecipeBuilder<>(new FissionHeatingRecipe(sizedIngredient(COOLANT_MAP.get("nak"), 1), sizedIngredient(HOT_COOLANT_MAP.get("nak_hot"), 1), FissionHeatingRecipe.RecipeHeatingType.NAK)).save(recipeOutput);
        for (String coolant : COOLANTS) {
            new BasicRecipeBuilder<>(new FissionHeatingRecipe(sizedIngredient(COOLANT_MAP.get(coolant + "_nak"), 1), sizedIngredient(HOT_COOLANT_MAP.get(coolant + "_nak_hot"), 1), FissionHeatingRecipe.RecipeHeatingType.NAK)).save(recipeOutput);
        }
    }
}