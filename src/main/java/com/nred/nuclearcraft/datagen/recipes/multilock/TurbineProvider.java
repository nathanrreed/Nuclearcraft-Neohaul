package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.turbine.TurbineRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.STEAM_KEY;
import static com.nred.nuclearcraft.registration.FluidRegistration.STEAM_MAP;

public class TurbineProvider {
    public TurbineProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new TurbineRecipe(Fluids.sizedIngredient(STEAM_MAP.get("high_pressure_steam"), 1), Fluids.sizedIngredient(STEAM_MAP.get("exhaust_steam"), 4), 16.0, 4.0, 1.0, "cloud", 5D / 116D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new TurbineRecipe(Fluids.sizedIngredient(STEAM_MAP.get("low_pressure_steam"), 1), Fluids.sizedIngredient(STEAM_MAP.get("low_quality_steam"), 2), 4.0, 2.0, 1.0, "cloud", 5D / 116D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new TurbineRecipe(SizedChanceFluidIngredient.of(STEAM_KEY, 1), Fluids.sizedIngredient(STEAM_MAP.get("low_quality_steam"), 2), 4.0, 2.0, 1.0, "cloud", 5D / 116D)).save(recipeOutput);
    }
}