package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidEnricherRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.CRYOTHEUM_TAG;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.GLOWING_MUSHROOM;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.COMPOUND_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEM_DUST_MAP;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;

public class FluidEnricherProvider {
    public FluidEnricherProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("boron_nitride"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("boron_nitride_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("fluorite"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("fluorite_water"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("calcium_sulfate"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("calcium_sulfate_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("sodium_fluoride"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("potassium_fluoride"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("sodium_hydroxide"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("potassium_hydroxide"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("borax"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("borax_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 0.5).addItemInput(COMPOUND_MAP.get("irradiated_borax"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("irradiated_borax_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("ammonium_sulfate"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_sulfate_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("ammonium_bisulfate"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("ammonium_persulfate"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("hydroquinone"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("hydroquinone_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("sodium_hydroquinone"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("potassium_hydroquinone"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("dysprholminite"), 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("dysprholminite_water"), GEM_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 2, 2).addItemInput(GEM_DUST_MAP.get("nichromite"), 1).addFluidInput(MOLTEN_MAP.get("barium_oxide"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("bacro_nio"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 0.5).addItemInput(GLOWING_MUSHROOM, 3).addFluidInput(FLAMMABLE_MAP.get("ethanol"), BUCKET_VOLUME / 4).addFluidResult(CUSTOM_FLUID_MAP.get("radaway"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 1, 0.5).addItemInput(GLOWING_MUSHROOM, 3).addFluidInput(FLAMMABLE_MAP.get("redstone_ethanol"), BUCKET_VOLUME / 4).addFluidResult(CUSTOM_FLUID_MAP.get("radaway_slow"), BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 0.5, 1).addItemInput(Items.SNOWBALL, 4).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), 25).addFluidResult(CRYOTHEUM_TAG, 25).save(recipeOutput, ncLoc("cryotheum_from_liquid_helium"));

        new ProcessorRecipeBuilder(FluidEnricherRecipe.class, 2, 1).addItemInput(COMPOUND_MAP.get("dimensional_blend"), 1).addFluidInput(SOUL_MAP.get("soul"), 100).addFluidResult(SOUL_MAP.get("mysterious_soul"), 100).save(recipeOutput);
    }
}