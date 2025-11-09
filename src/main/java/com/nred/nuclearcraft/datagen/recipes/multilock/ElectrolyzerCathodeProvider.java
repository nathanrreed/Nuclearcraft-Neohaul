package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.ElectrolyzerCathodeRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.tag;

public class ElectrolyzerCathodeProvider {
    public ElectrolyzerCathodeProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new ElectrolyzerCathodeRecipe(Ingredient.of(tag(Tags.Items.STORAGE_BLOCKS, "iron")), 0.6)).save(recipeOutput, ncLoc("iron_cathode"));
        new BasicRecipeBuilder<>(new ElectrolyzerCathodeRecipe(Ingredient.of(tag(Tags.Items.STORAGE_BLOCKS, "nickel")), 0.7)).save(recipeOutput, ncLoc("nickel_cathode"));
        new BasicRecipeBuilder<>(new ElectrolyzerCathodeRecipe(Ingredient.of(tag(Tags.Items.STORAGE_BLOCKS, "molybdenum")), 0.8)).save(recipeOutput, ncLoc("molybdenum_cathode"));
        new BasicRecipeBuilder<>(new ElectrolyzerCathodeRecipe(Ingredient.of(tag(Tags.Items.STORAGE_BLOCKS, "cobalt")), 0.9)).save(recipeOutput, ncLoc("cobalt_cathode"));
        new BasicRecipeBuilder<>(new ElectrolyzerCathodeRecipe(Ingredient.of(tag(Tags.Items.STORAGE_BLOCKS, "platinum")), 1.0)).save(recipeOutput, ncLoc("platinum_cathode"));
        new BasicRecipeBuilder<>(new ElectrolyzerCathodeRecipe(Ingredient.of(tag(Tags.Items.STORAGE_BLOCKS, "palladium")), 1.0)).save(recipeOutput,ncLoc("palladium_cathode"));
    }
}