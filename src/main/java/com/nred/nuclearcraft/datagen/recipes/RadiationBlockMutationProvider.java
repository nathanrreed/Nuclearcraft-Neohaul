package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.RadiationBlockMutationRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.WASTELAND_EARTH;

public class RadiationBlockMutationProvider {
    public RadiationBlockMutationProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(List.of(Ingredient.of(Blocks.DIRT), Ingredient.of(Blocks.GRASS_BLOCK)), Ingredient.of(WASTELAND_EARTH), 10.0).save(recipeOutput, ncLoc("dirt_mutation"));
        new RecipeBuilder(List.of(Ingredient.of(ItemTags.LEAVES), Ingredient.of(Blocks.VINE)), Ingredient.of(Blocks.AIR), 100).save(recipeOutput, ncLoc("leaf_mutation"));
        new RecipeBuilder(List.of(Ingredient.of(ItemTags.SAPLINGS), Ingredient.of(ItemTags.create(ResourceLocation.parse("c:crops")))), Ingredient.of(Blocks.AIR), 4.0).save(recipeOutput, ncLoc("crop_mutation"));
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final List<Ingredient> itemIngredients;
        private final Ingredient itemResult;
        private final double mutationThreshold;

        public RecipeBuilder(List<Ingredient> itemIngredients, Ingredient itemResult, double mutationThreshold) {
            super(ItemStack.EMPTY);
            this.itemIngredients = itemIngredients;
            this.itemResult = itemResult;
            this.mutationThreshold = mutationThreshold;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            RadiationBlockMutationRecipe recipe = new RadiationBlockMutationRecipe(itemIngredients.stream().map(a -> new SizedChanceItemIngredient(a, 1)).toList(), itemResult, mutationThreshold);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}