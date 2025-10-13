package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.RockCrusherRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.probabilityPacker;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class RockCrusherProvider {
    public RockCrusherProvider(RecipeOutput recipeOutput) {
        // Amount works differently here, it the a probability and the amount packed
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.GRANITE, Items.POLISHED_GRANITE), 1).addItemResult(GEM_DUST_MAP.get("rhodochrosite"), probabilityPacker(40, 2)).addItemResult(GEM_DUST_MAP.get("sulfur"), probabilityPacker(30, 2)).addItemResult(GEM_DUST_MAP.get("villiaumite"), probabilityPacker(35, 1)).save(recipeOutput);
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.DIORITE, Items.POLISHED_DIORITE), 1).addItemResult(DUST_MAP.get("zirconium"), probabilityPacker(50, 2)).addItemResult(GEM_DUST_MAP.get("fluorite"), probabilityPacker(45, 2)).addItemResult(GEM_DUST_MAP.get("carobbiite"), probabilityPacker(35, 1)).save(recipeOutput);
        new ProcessorRecipeBuilder(RockCrusherRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.ANDESITE, Items.POLISHED_ANDESITE), 1).addItemResult(DUST_MAP.get("beryllium"), probabilityPacker(50, 2)).addItemResult(COMPOUND_MAP.get("alugentum"), probabilityPacker(30, 2)).addItemResult(GEM_DUST_MAP.get("arsenic"), probabilityPacker(30, 1)).save(recipeOutput);
    }
}