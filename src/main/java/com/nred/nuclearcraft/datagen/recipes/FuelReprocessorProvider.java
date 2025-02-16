package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FuelReprocessorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class FuelReprocessorProvider {
    public FuelReprocessorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FuelReprocessorRecipe.class, 1, 1).addItemInput(DEPLETED_FUEL_THORIUM_MAP.get("tbu_tr"), 9).addItemResult(URANIUM_MAP.get("233_c"),1).addItemResult(URANIUM_MAP.get("238_c"),5).addItemResult(FISSION_DUST_MAP.get("strontium_90"),1).addItemResult(DUST_MAP.get("graphite"),3).addItemResult(NEPTUNIUM_MAP.get("236_c"),1).addItemResult(NEPTUNIUM_MAP.get("237_c"),1).addItemResult(FISSION_DUST_MAP.get("caesium_137"),1).addItemResult(ALLOY_MAP.get("silicon_carbide"), 1).save(recipeOutput);

    }
}