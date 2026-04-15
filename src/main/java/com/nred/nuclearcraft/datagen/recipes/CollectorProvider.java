package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.CollectorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;

public class CollectorProvider {
    public CollectorProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new CollectorRecipe("cobblestone_generator", Ingredient.of(Items.COBBLESTONE))).save(recipeOutput, "cobblestone_generator_rate");
        new BasicRecipeBuilder<>(new CollectorRecipe("cobblestone_generator_compact", Ingredient.of(Items.COBBLESTONE))).save(recipeOutput, "cobblestone_generator_compact_rate");
        new BasicRecipeBuilder<>(new CollectorRecipe("cobblestone_generator_dense", Ingredient.of(Items.COBBLESTONE))).save(recipeOutput, "cobblestone_generator_dense_rate");

        new BasicRecipeBuilder<>(new CollectorRecipe("water_source", FluidIngredient.of(Fluids.WATER))).save(recipeOutput, "water_source_rate");
        new BasicRecipeBuilder<>(new CollectorRecipe("water_source_compact", FluidIngredient.of(Fluids.WATER))).save(recipeOutput, "water_source_compact_rate");
        new BasicRecipeBuilder<>(new CollectorRecipe("water_source_dense", FluidIngredient.of(Fluids.WATER))).save(recipeOutput, "water_source_dense_rate");

        new BasicRecipeBuilder<>(new CollectorRecipe("nitrogen_collector", FluidIngredient.of(GAS_MAP.get("nitrogen").still.get()))).save(recipeOutput, "nitrogen_collector_rate");
        new BasicRecipeBuilder<>(new CollectorRecipe("nitrogen_collector_compact", FluidIngredient.of(GAS_MAP.get("nitrogen").still.get()))).save(recipeOutput, "nitrogen_collector_compact_rate");
        new BasicRecipeBuilder<>(new CollectorRecipe("nitrogen_collector_dense", FluidIngredient.of(GAS_MAP.get("nitrogen").still.get()))).save(recipeOutput, "nitrogen_collector_dense_rate");
    }
}