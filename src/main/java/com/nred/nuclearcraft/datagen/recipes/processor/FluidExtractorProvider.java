package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidExtractorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.block.Blocks;

import static com.nred.nuclearcraft.registration.BlockRegistration.MATERIAL_BLOCK_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.CHOCOLATE_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.SOUL_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.FOOD_MAP;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_VOLUME;

public class FluidExtractorProvider {
    public FluidExtractorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidExtractorRecipe.class, 1, 2).addItemInput(Blocks.SOUL_SAND, 1).addItemResult(MATERIAL_BLOCK_MAP.get("soulless_sand"), 1).addFluidResult(SOUL_MAP.get("soul"), 100).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidExtractorRecipe.class, 0.5, 0.5).addItemInput(FOOD_MAP.get("ground_cocoa_nibs"), 1).addItemResult(FOOD_MAP.get("cocoa_solids"), 1).addFluidResult(CHOCOLATE_MAP.get("cocoa_butter"), INGOT_VOLUME).save(recipeOutput);
    }
}