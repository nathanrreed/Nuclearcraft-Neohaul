package com.nred.nuclearcraft.datagen.recipes.multiblock;

import com.nred.nuclearcraft.info.NCFluid;
import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.machine.MultiblockDecayPoolRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static net.minecraft.world.level.material.Fluids.WATER;

public class MultiblockDecayPoolProvider {
    public MultiblockDecayPoolProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new MultiblockDecayPoolRecipe(SizedChanceFluidIngredient.of(WATER, 1), NCFluid.sizedIngredient(CUSTOM_FLUID_MAP.get("preheated_water"), 1), 32)).save(recipeOutput);
    }
}