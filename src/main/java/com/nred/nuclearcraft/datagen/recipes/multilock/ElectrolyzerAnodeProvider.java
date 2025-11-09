package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.ElectrolyzerAnodeRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.INGOT_BLOCK_MAP;
import static com.nred.nuclearcraft.registration.BlockRegistration.MATERIAL_BLOCK_MAP;

public class ElectrolyzerAnodeProvider {
    public ElectrolyzerAnodeProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new ElectrolyzerAnodeRecipe(Ingredient.of(MATERIAL_BLOCK_MAP.get("copper_oxide")), 0.6)).save(recipeOutput, ncLoc("copper_oxide_anode")); // TODO no way to get this
        new BasicRecipeBuilder<>(new ElectrolyzerAnodeRecipe(Ingredient.of(INGOT_BLOCK_MAP.get("tin_oxide")), 0.6)).save(recipeOutput, ncLoc("tin_oxide_anode"));
        new BasicRecipeBuilder<>(new ElectrolyzerAnodeRecipe(Ingredient.of(INGOT_BLOCK_MAP.get("nickel_oxide")), 0.7)).save(recipeOutput, ncLoc("nickel_oxide_anode"));
        new BasicRecipeBuilder<>(new ElectrolyzerAnodeRecipe(Ingredient.of(INGOT_BLOCK_MAP.get("cobalt_oxide")), 0.8)).save(recipeOutput, ncLoc("cobalt_oxide_anode"));
        new BasicRecipeBuilder<>(new ElectrolyzerAnodeRecipe(Ingredient.of(INGOT_BLOCK_MAP.get("ruthenium_oxide")), 0.9)).save(recipeOutput, ncLoc("ruthenium_oxide_anode"));
        new BasicRecipeBuilder<>(new ElectrolyzerAnodeRecipe(Ingredient.of(INGOT_BLOCK_MAP.get("iridium_oxide")), 1.0)).save(recipeOutput, ncLoc("iridium_oxide_anode"));
    }
}