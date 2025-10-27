package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.PressurizerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import static com.nred.nuclearcraft.registration.BlockRegistration.MATERIAL_BLOCK_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class PressurizerProvider {
    public PressurizerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(DUST_MAP.get("graphite"), 1).addItemResult(Items.COAL, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("graphite"), 1).addItemResult(PART_MAP.get("pyrolytic_carbon"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("diamond"), 1).addItemResult(Items.DIAMOND, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("rhodochrosite"), 1).addItemResult(GEM_MAP.get("rhodochrosite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("quartz"), 1).addItemResult(Items.QUARTZ, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1.5, 1.5).addItemInput(GEM_DUST_MAP.get("obsidian"), 4).addItemResult(Items.OBSIDIAN, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("boron_nitride"), 1).addItemResult(GEM_MAP.get("boron_nitride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("fluorite"), 1).addItemResult(GEM_MAP.get("fluorite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("villiaumite"), 1).addItemResult(GEM_MAP.get("villiaumite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("carobbiite"), 1).addItemResult(GEM_MAP.get("carobbiite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 2, 2).addItemInput(DUST_MAP.get("zirconia"), 1).addItemResult(PART_MAP.get("sintered_zirconia"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 9, 1).addItemInput(FISSION_DUST_MAP.get("molybdenum"), 9).addItemResult(MATERIAL_BLOCK_MAP.get("molybdenum"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(PressurizerRecipe.class, 0.5, 0.5).addItemInput(FOOD_MAP.get("flour"), 2).addItemResult(FOOD_MAP.get("graham_cracker"), 1).save(recipeOutput);
    }
}