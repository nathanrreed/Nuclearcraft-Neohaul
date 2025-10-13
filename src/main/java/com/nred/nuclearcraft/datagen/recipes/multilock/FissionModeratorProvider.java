package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockRegistration.HEAVY_WATER_MODERATOR;
import static com.nred.nuclearcraft.registration.BlockRegistration.INGOT_BLOCK_MAP;

public class FissionModeratorProvider {
    public FissionModeratorProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(Ingredient.of(INGOT_BLOCK_MAP.get("graphite")), 10, 1.1).save(recipeOutput, MODID + ":graphite_moderator");
        new RecipeBuilder(Ingredient.of(INGOT_BLOCK_MAP.get("beryllium")), 22, 1.05).save(recipeOutput, MODID + ":beryllium_moderator");
        new RecipeBuilder(Ingredient.of(HEAVY_WATER_MODERATOR), 36, 1).save(recipeOutput, MODID + ":heavy_water_moderator");
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final Ingredient moderator;
        private final int fluxFactor;
        private final double efficiency;

        public RecipeBuilder(Ingredient moderator, int fluxFactor, double efficiency) {
            super(ItemStack.EMPTY);
            this.moderator = moderator;
            this.fluxFactor = fluxFactor;
            this.efficiency = efficiency;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            FissionModeratorRecipe recipe = new FissionModeratorRecipe(this.moderator, this.fluxFactor, this.efficiency);

            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}