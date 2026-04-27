package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class HeatExchangerProvider {
    public HeatExchangerProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new HeatExchangerRecipe(SizedChanceFluidIngredient.of(net.minecraft.world.level.material.Fluids.WATER, 1), Fluids.sizedIngredient(STEAM_MAP.get("high_pressure_steam"), 4), 64D, 300, 600, false, 1, 0.5D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("preheated_water"), 1), Fluids.sizedIngredient(STEAM_MAP.get("high_pressure_steam"), 4), 32D, 400, 600, false, 1, 0.5D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(STEAM_MAP.get("high_pressure_steam"), 1), Fluids.sizedIngredient(STEAM_MAP.get("low_pressure_steam"), 1), 4D, 600, 550, false, 0, 0.0)).save(recipeOutput);
        new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(STEAM_MAP.get("exhaust_steam"), 1), Fluids.sizedIngredient(STEAM_MAP.get("low_pressure_steam"), 1), 1D, 400, 550, false, 0, 0.0)).save(recipeOutput);
        new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(STEAM_MAP.get("low_quality_steam"), 32), Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("condensate_water"), 1), 2D, 350, 350, false, -1, 0.5D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("condensate_water"), 1), Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("preheated_water"), 1), 8D, 350, 400, false, 0, 0.5D)).save(recipeOutput);

        new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(HOT_COOLANT_MAP.get("nak_hot"), 1), Fluids.sizedIngredient(COOLANT_MAP.get("nak"), 1), "standard", 800, 400, false, 0, 0.0)).save(recipeOutput);
        for (String coolant : COOLANTS) {
            new BasicRecipeBuilder<>(new HeatExchangerRecipe(Fluids.sizedIngredient(HOT_COOLANT_MAP.get(coolant + "_nak_hot"), 1), Fluids.sizedIngredient(COOLANT_MAP.get(coolant + "_nak"), 1), coolant, 800, 400, false, 0, 0.0)).save(recipeOutput);
        }
    }
}