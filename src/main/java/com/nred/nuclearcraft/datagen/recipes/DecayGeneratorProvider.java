package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.DecayGeneratorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.FERTILE_ISOTOPE_MAP;
import static com.nred.nuclearcraft.registration.BlockRegistration.INGOT_BLOCK_MAP;

public class DecayGeneratorProvider {
    public DecayGeneratorProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(INGOT_BLOCK_MAP.get("thorium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 0.75D, 0.75D, RadSources.THORIUM)).save(recipeOutput, ncLoc("lead_from_thorium"));
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(INGOT_BLOCK_MAP.get("uranium"), FERTILE_ISOTOPE_MAP.get("uranium"), 12000D / 1.2D, 1.2D, RadSources.URANIUM)).save(recipeOutput);
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("uranium"), INGOT_BLOCK_MAP.get("lead"), 1200D, 1D, RadSources.URANIUM_238)).save(recipeOutput, ncLoc("lead_from_uranium"));
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("neptunium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 2.2D, 2.2D, RadSources.NEPTUNIUM_237)).save(recipeOutput, ncLoc("lead_from_neptunium"));
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("plutonium"), FERTILE_ISOTOPE_MAP.get("uranium"), 12000D / 3D, 3D, RadSources.PLUTONIUM_242)).save(recipeOutput, ncLoc("uranium_from_plutonium"));
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("americium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 18D, 18D, RadSources.AMERICIUM_243)).save(recipeOutput, ncLoc("lead_from_americium"));
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("curium"), FERTILE_ISOTOPE_MAP.get("plutonium"), 12000D / 28D, 28D, RadSources.CURIUM_246)).save(recipeOutput);
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("berkelium"), FERTILE_ISOTOPE_MAP.get("americium"), 12000D / 80D, 80D, RadSources.BERKELIUM_247)).save(recipeOutput);
        new BasicRecipeBuilder<>(new DecayGeneratorRecipe(FERTILE_ISOTOPE_MAP.get("californium"), INGOT_BLOCK_MAP.get("lead"), 12000D / 1000D, 1000D, RadSources.CALIFORNIUM_252)).save(recipeOutput, ncLoc("lead_from_californium"));
    }
}