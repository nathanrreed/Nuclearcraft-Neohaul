package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.exchanger.CondenserRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.info.Names.GAS_COOLANTS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class CondenserProvider {
    public CondenserProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new CondenserRecipe(Fluids.sizedIngredient(STEAM_MAP.get("exhaust_steam"), 16), Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("condensate_water"), 1), 32D, 550, 350, -1, 1D)).save(recipeOutput);
        new BasicRecipeBuilder<>(new CondenserRecipe(Fluids.sizedIngredient(STEAM_MAP.get("low_quality_steam"), 32), Fluids.sizedIngredient(CUSTOM_FLUID_MAP.get("condensate_water"), 1), 2D, 350, 350, -1, 0.5D)).save(recipeOutput);

        for (String coolant : GAS_COOLANTS) {
            new BasicRecipeBuilder<>(new CondenserRecipe(Fluids.sizedIngredient(HOT_GAS_MAP.get(coolant + "_exhaust"), 2), Fluids.sizedIngredient(GAS_MAP.get(coolant), 1), 32D, 900, 300, 0, 0.0)).save(recipeOutput);
            new BasicRecipeBuilder<>(new CondenserRecipe(Fluids.sizedIngredient(HOT_GAS_MAP.get(coolant + "_lq"), 4), Fluids.sizedIngredient(GAS_MAP.get(coolant), 1), 2D, 800, 300, 0, 0.0)).save(recipeOutput);
        }
    }
}