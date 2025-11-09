package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.radiation.RadSources;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.fission.FissionIrradiatorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class FissionIrradiatorProvider {
    public FissionIrradiatorProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new FissionIrradiatorRecipe(new SizedChanceItemIngredient(Ingredient.of(INGOT_MAP.get("thorium").asItem(), DUST_MAP.get("thorium").asItem()), 1), SizedChanceItemIngredient.of(FISSION_DUST_MAP.get("tbp"), 1), 160000L, 0, 0, 0L, -1L, RadSources.THORIUM)).save(recipeOutput);
        new BasicRecipeBuilder<>(new FissionIrradiatorRecipe(SizedChanceItemIngredient.of(FISSION_DUST_MAP.get("tbp"), 1), SizedChanceItemIngredient.of(FISSION_DUST_MAP.get("protactinium_233"), 1), 2720000L, 0, 0, 0L, -1L, RadSources.TBP)).save(recipeOutput);
        new BasicRecipeBuilder<>(new FissionIrradiatorRecipe(SizedChanceItemIngredient.of(FISSION_DUST_MAP.get("bismuth"), 1), SizedChanceItemIngredient.of(FISSION_DUST_MAP.get("polonium"), 1), 1920000L, 0, 0.5, 0L, -1L, RadSources.BISMUTH)).save(recipeOutput);
    }
}