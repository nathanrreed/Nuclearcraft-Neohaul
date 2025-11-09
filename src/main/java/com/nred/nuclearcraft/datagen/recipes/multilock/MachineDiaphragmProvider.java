package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.MachineDiaphragmRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_MAP;

public class MachineDiaphragmProvider {
    public MachineDiaphragmProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new MachineDiaphragmRecipe(Ingredient.of(MACHINE_MAP.get("sintered_steel_diaphragm")), 0.8, 1.0)).save(recipeOutput, ncLoc("diaphragm_sintered_steel"));
        new BasicRecipeBuilder<>(new MachineDiaphragmRecipe(Ingredient.of(MACHINE_MAP.get("polyethersulfone_diaphragm")), 0.9, 1.5)).save(recipeOutput, ncLoc("diaphragm_polyethersulfone"));
        new BasicRecipeBuilder<>(new MachineDiaphragmRecipe(Ingredient.of(MACHINE_MAP.get("zirfon_diaphragm")), 1.0, 2.0)).save(recipeOutput, ncLoc("diaphragm_zirfon"));
    }
}