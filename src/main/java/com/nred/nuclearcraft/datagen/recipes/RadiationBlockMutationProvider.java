package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.RadiationBlockMutationRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.WASTELAND_EARTH;

public class RadiationBlockMutationProvider {
    public RadiationBlockMutationProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new RadiationBlockMutationRecipe(List.of(SizedChanceItemIngredient.of(Blocks.DIRT, 1), SizedChanceItemIngredient.of(Blocks.GRASS_BLOCK, 1)), Ingredient.of(WASTELAND_EARTH), 10.0)).save(recipeOutput, ncLoc("dirt_mutation"));
        new BasicRecipeBuilder<>(new RadiationBlockMutationRecipe(List.of(SizedChanceItemIngredient.of(ItemTags.LEAVES, 1), SizedChanceItemIngredient.of(Blocks.VINE, 1)), Ingredient.of(Blocks.AIR), 100)).save(recipeOutput, ncLoc("leaf_mutation"));
        new BasicRecipeBuilder<>(new RadiationBlockMutationRecipe(List.of(SizedChanceItemIngredient.of(ItemTags.SAPLINGS, 1), SizedChanceItemIngredient.of(ItemTags.create(ResourceLocation.parse("c:crops")), 1)), Ingredient.of(Blocks.AIR), 4.0)).save(recipeOutput, ncLoc("crop_mutation"));
    }
}