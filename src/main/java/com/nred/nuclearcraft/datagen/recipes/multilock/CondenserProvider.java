package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.STEAM_MAP;

public class CondenserProvider {
    public CondenserProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new CondenserRecipe(Fluids.sizedIngredient(STEAM_MAP.get("exhaust_steam"), 16), Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("condensate_water"), 1), 32D, 550, 350, -1, 0.5D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new CondenserRecipe(Fluids.sizedIngredient(STEAM_MAP.get("low_quality_steam"), 32), Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("condensate_water"), 1), 2D, 350, 350, -1, 0.5D)).save(recipeOutput);
    }
}