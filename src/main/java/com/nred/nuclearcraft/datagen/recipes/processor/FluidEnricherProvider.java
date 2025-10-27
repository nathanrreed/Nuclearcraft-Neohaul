package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidEnricherRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.CRYOTHEUM_KEY;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.GLOWING_MUSHROOM;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.COMPOUND_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEM_DUST_MAP;

public class FluidEnricherProvider {
    public FluidEnricherProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("boron_nitride"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("boron_nitride_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("fluorite"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("fluorite_water"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("calcium_sulfate"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("calcium_sulfate_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("sodium_fluoride"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("potassium_fluoride"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("sodium_hydroxide"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("potassium_hydroxide"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("borax"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("borax_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("irradiated_borax"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("irradiated_borax_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("ammonium_sulfate"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_sulfate_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("ammonium_bisulfate"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("ammonium_persulfate"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("hydroquinone"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("hydroquinone_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("sodium_hydroquinone"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("potassium_hydroquinone"), 1).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), 666).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GLOWING_MUSHROOM, 3).addFluidInput(FLAMMABLE_MAP.get("ethanol"), 250).addFluidResult(CUSTOM_FLUID_MAP.get("radaway"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GLOWING_MUSHROOM, 3).addFluidInput(FLAMMABLE_MAP.get("redstone_ethanol"), 250).addFluidResult(CUSTOM_FLUID_MAP.get("radaway_slow"), 250).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(Items.SNOWBALL, 4).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), 25).addFluidResult(CRYOTHEUM_KEY, 25).save(recipeOutput, ncLoc("cryotheum_from_liquid_helium"));
    }
}