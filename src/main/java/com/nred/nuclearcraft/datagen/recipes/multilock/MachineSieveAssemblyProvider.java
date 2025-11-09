package com.nred.nuclearcraft.datagen.recipes.multilock;

import com.nred.nuclearcraft.recipe.BasicRecipeBuilder;
import com.nred.nuclearcraft.recipe.machine.MachineSieveAssemblyRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_MAP;

public class MachineSieveAssemblyProvider {
    public MachineSieveAssemblyProvider(RecipeOutput recipeOutput) {
        new BasicRecipeBuilder<>(new MachineSieveAssemblyRecipe(Ingredient.of(MACHINE_MAP.get("steel_sieve_assembly")), 0.8)).save(recipeOutput, ncLoc("sieve_assembly_steel"));
        new BasicRecipeBuilder<>(new MachineSieveAssemblyRecipe(Ingredient.of(MACHINE_MAP.get("polytetrafluoroethene_sieve_assembly")), 0.9)).save(recipeOutput, ncLoc("sieve_assembly_polytetrafluoroethene"));
        new BasicRecipeBuilder<>(new MachineSieveAssemblyRecipe(Ingredient.of(MACHINE_MAP.get("hastelloy_sieve_assembly")), 1.0)).save(recipeOutput, ncLoc("sieve_assembly_hastelloy"));
    }
}