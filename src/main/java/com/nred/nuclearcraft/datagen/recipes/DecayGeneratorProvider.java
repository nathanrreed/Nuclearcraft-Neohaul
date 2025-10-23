package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.nred.nuclearcraft.registration.BlockRegistration.FERTILE_ISOTOPE_MAP;
import static com.nred.nuclearcraft.registration.BlockRegistration.INGOT_BLOCK_MAP;

public class DecayGeneratorProvider {
    public DecayGeneratorProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder(INGOT_BLOCK_MAP.get("thorium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 0.75D, 0.75D, RadSources.THORIUM).save(recipeOutput);
        new RecipeBuilder(INGOT_BLOCK_MAP.get("uranium"), FERTILE_ISOTOPE_MAP.get("uranium"), 12000D / 1.2D, 1.2D, RadSources.URANIUM).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("uranium"), INGOT_BLOCK_MAP.get("lead"), 1200D, 1D, RadSources.URANIUM_238).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("neptunium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 2.2D, 2.2D, RadSources.NEPTUNIUM_237).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("plutonium"), FERTILE_ISOTOPE_MAP.get("uranium"), 12000D / 3D, 3D, RadSources.PLUTONIUM_242).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("americium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 18D, 18D, RadSources.AMERICIUM_243).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("curium"), FERTILE_ISOTOPE_MAP.get("plutonium"), 12000D / 28D, 28D, RadSources.CURIUM_246).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("berkelium"), FERTILE_ISOTOPE_MAP.get("americium"), 12000D / 80D, 80D, RadSources.BERKELIUM_247).save(recipeOutput);
        new RecipeBuilder(FERTILE_ISOTOPE_MAP.get("californium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 1000D, 1000D, RadSources.CALIFORNIUM_252).save(recipeOutput);
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final ItemStack input;
        private final ItemStack output;
        private final double lifetime;
        private final double power;
        private final double radiation;

        public RecipeBuilder(DeferredBlock<Block> input, DeferredBlock<Block> output, double lifetime, double power, double radiation) {
            super(output.toStack());
            this.input = input.toStack();
            this.output = output.toStack();
            this.lifetime = lifetime;
            this.power = power;
            this.radiation = radiation;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            DecayGeneratorRecipe recipe = new DecayGeneratorRecipe(SizedIngredient.of(this.input.getItem(), 1), SizedIngredient.of(this.output.getItem(), 1), this.lifetime, this.power, this.radiation);
            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}