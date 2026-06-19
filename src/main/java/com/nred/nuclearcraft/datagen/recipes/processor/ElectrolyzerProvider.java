package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ElectrolyzerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;

public class ElectrolyzerProvider {
    public ElectrolyzerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("hydrogen"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID_MAP.get("le_water"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("hydrogen"), 3 * BUCKET_VOLUME / 8).addFluidResult(GAS_MAP.get("deuterium"), BUCKET_VOLUME / 8).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 4).save(recipeOutput, "le_water");
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID_MAP.get("he_water"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("hydrogen"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("deuterium"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 4).save(recipeOutput, "he_water");
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID_MAP.get("heavy_water"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("deuterium"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 4).save(recipeOutput, "heavy_water");
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("hydrogen"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 4).save(recipeOutput, "hydrofluoric_acid");

        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("naoh"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("sodium"), INGOT_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 8).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("koh"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("potassium"), INGOT_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 8).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("alumina"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("aluminum"), INGOT_VOLUME / 2).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME * 3 / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), GEM_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), GEM_VOLUME / 2).addFluidResult(GAS_MAP.get("hydrogen"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 2, 1).addFluidInput(HOT_GAS_MAP.get("hodybef_vapor"), BUCKET_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("holmium"), BUCKET_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("dysprosium"), INGOT_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("beryllium"), INGOT_VOLUME / 2).addFluidResult(GAS_MAP.get("fluorine"), BUCKET_VOLUME * 3 / 2).save(recipeOutput);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get(name + "_fluoride"), BUCKET_VOLUME / 2).addFluidResult(FISSION_FUEL_MAP.get(name), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).save(recipeOutput);
            new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("depleted_" + name + "_fluoride"), BUCKET_VOLUME / 2).addFluidResult(FISSION_FUEL_MAP.get("depleted_" + name), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).save(recipeOutput);
        }
    }
}