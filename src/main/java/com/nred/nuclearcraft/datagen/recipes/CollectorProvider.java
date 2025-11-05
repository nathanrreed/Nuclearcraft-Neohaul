package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import com.nred.nuclearcraft.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class CollectorProvider {
    public CollectorProvider(RecipeOutput recipeOutput) {
        new RecipeBuilder("cobblestone_generator", Ingredient.of(Items.COBBLESTONE)).save(recipeOutput, MODID + ":cobblestone_generator_rate");
        new RecipeBuilder("cobblestone_generator_compact", Ingredient.of(Items.COBBLESTONE)).save(recipeOutput, MODID + ":cobblestone_generator_compact_rate");
        new RecipeBuilder("cobblestone_generator_dense", Ingredient.of(Items.COBBLESTONE)).save(recipeOutput, MODID + ":cobblestone_generator_dense_rate");

        new RecipeBuilder("water_source", FluidIngredient.of(Fluids.WATER)).save(recipeOutput, MODID + ":water_source_rate");
        new RecipeBuilder("water_source_compact", FluidIngredient.of(Fluids.WATER)).save(recipeOutput, MODID + ":water_source_compact_rate");
        new RecipeBuilder("water_source_dense", FluidIngredient.of(Fluids.WATER)).save(recipeOutput, MODID + ":water_source_dense_rate");

        new RecipeBuilder("nitrogen_collector", FluidIngredient.of(GAS_MAP.get("nitrogen").still.get())).save(recipeOutput, MODID + ":nitrogen_collector_rate");
        new RecipeBuilder("nitrogen_collector_compact", FluidIngredient.of(GAS_MAP.get("nitrogen").still.get())).save(recipeOutput, MODID + ":nitrogen_collector_compact_rate");
        new RecipeBuilder("nitrogen_collector_dense", FluidIngredient.of(GAS_MAP.get("nitrogen").still.get())).save(recipeOutput, MODID + ":nitrogen_collector_dense_rate");
    }

    public static class RecipeBuilder extends SimpleRecipeBuilder {
        private final String type;
        private final Ingredient itemProduct;
        private final FluidIngredient fluidProduct;

        public RecipeBuilder(String type, Ingredient itemProduct) {
            super(ItemStack.EMPTY);
            this.type = type;
            this.itemProduct = itemProduct;
            this.fluidProduct = FluidIngredient.empty();
        }

        public RecipeBuilder(String type, FluidIngredient fluidProduct) {
            super(ItemStack.EMPTY);
            this.type = type;
            this.itemProduct = Ingredient.EMPTY;
            this.fluidProduct = fluidProduct;
        }

        @Override
        public void save(RecipeOutput output, ResourceLocation key) {
            Advancement.Builder advancement = output.advancement()
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                    .rewards(AdvancementRewards.Builder.recipe(key))
                    .requirements(AdvancementRequirements.Strategy.OR);

            CollectorRecipe recipe = new CollectorRecipe(this.type, this.itemProduct.isEmpty() ? List.of() : List.of(new SizedChanceItemIngredient(this.itemProduct, 1)),this.fluidProduct.isEmpty() ? List.of() : List.of(new SizedChanceFluidIngredient(this.fluidProduct, 1)));

            output.accept(key, recipe, advancement.build(key.withPrefix("recipes/")));
        }
    }
}