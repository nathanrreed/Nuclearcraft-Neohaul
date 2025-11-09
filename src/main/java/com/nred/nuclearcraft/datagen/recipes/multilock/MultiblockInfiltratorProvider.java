package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.MultiblockInfiltratorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.List;

import static com.nred.nuclearcraft.registration.FluidRegistration.MOLTEN_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class MultiblockInfiltratorProvider {
    public MultiblockInfiltratorProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new MultiblockInfiltratorRecipe(List.of(SizedChanceItemIngredient.of(PART_MAP.get("silicon_carbide_fiber"), 2)), List.of(Fluids.sizedIngredient(MOLTEN_MAP.get("polymethylsilylene_methylene"), 144)), SizedChanceItemIngredient.of(ALLOY_MAP.get("silicon_carbide"), 1), 1.0, 1.0, 1.0)).save(recipeOutput);
        new BasicRecipeBuilder<>(new MultiblockInfiltratorRecipe(List.of(SizedChanceItemIngredient.of(PART_MAP.get("sintered_zirconia"), 1), SizedChanceItemIngredient.of(DUST_MAP.get("zirconia"), 1)), List.of(Fluids.sizedIngredient(MOLTEN_MAP.get("polyphenylene_sulfide"), 144)), SizedChanceItemIngredient.of(PART_MAP.get("zirfon"), 1), 1.0, 1.0, 0.0)).save(recipeOutput);
    }
}