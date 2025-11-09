package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.ElectrolyzerElectrolyteRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import static com.nred.nuclearcraft.registration.FluidRegistration.SALT_SOLUTION_MAP;

public class ElectrolyzerElectrolyteProvider {
    public ElectrolyzerElectrolyteProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new ElectrolyzerElectrolyteRecipe(FluidIngredient.of(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution").still.get()), 0.9, "hydroxide_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new ElectrolyzerElectrolyteRecipe(FluidIngredient.of(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution").still.get()), 1.0, "hydroxide_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new ElectrolyzerElectrolyteRecipe(FluidIngredient.of(SALT_SOLUTION_MAP.get("sodium_fluoride_solution").still.get()), 0.9, "fluoride_solution")).save(recipeOutput);
        new BasicRecipeBuilder<>(new ElectrolyzerElectrolyteRecipe(FluidIngredient.of(SALT_SOLUTION_MAP.get("potassium_fluoride_solution").still.get()), 1.0, "fluoride_solution")).save(recipeOutput);
    }
}