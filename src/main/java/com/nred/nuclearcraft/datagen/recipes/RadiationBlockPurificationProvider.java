package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.RadiationBlockPurificationRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import static com.nred.nuclearcraft.registration.BlockRegistration.WASTELAND_EARTH;

public class RadiationBlockPurificationProvider {
    public RadiationBlockPurificationProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(Ingredient.of(WASTELAND_EARTH), Ingredient.of(Blocks.DIRT), 0.0).save(recipeOutput);
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final Ingredient itemIngredient;
        private final Ingredient itemResult;
        private final double purificationThreshold;

        public RecipeBuilder(Ingredient itemIngredient, Ingredient itemResult, double purificationThreshold) {
            super(itemResult.getItems()[0]);
            this.itemIngredient = itemIngredient;
            this.itemResult = itemResult;
            this.purificationThreshold = purificationThreshold;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            RadiationBlockPurificationRecipe recipe = new RadiationBlockPurificationRecipe(new SizedChanceItemIngredient(itemIngredient, 1), new SizedChanceItemIngredient(itemResult, 1), purificationThreshold);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}