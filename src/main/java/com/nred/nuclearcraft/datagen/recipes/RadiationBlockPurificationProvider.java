package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.RadiationBlockPurificationRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.block.Blocks;

import static com.nred.nuclearcraft.registration.BlockRegistration.WASTELAND_EARTH;

public class RadiationBlockPurificationProvider {
    public RadiationBlockPurificationProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new RadiationBlockPurificationRecipe(SizedChanceItemIngredient.of(WASTELAND_EARTH, 1), SizedChanceItemIngredient.of(Blocks.DIRT, 1), 0.0)).save(recipeOutput);
    }
}