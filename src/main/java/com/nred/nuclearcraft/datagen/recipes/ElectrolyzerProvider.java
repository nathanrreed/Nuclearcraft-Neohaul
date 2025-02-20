package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ElectrolyzerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class ElectrolyzerProvider {
    public ElectrolyzerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(Fluids.WATER, 500).addFluidResult(GAS_MAP.get("hydrogen"), 500).addFluidResult(GAS_MAP.get("oxygen"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID.get("le_water"), 500).addFluidResult(GAS_MAP.get("hydrogen"), 375).addFluidResult(GAS_MAP.get("deuterium"), 125).addFluidResult(GAS_MAP.get("oxygen"), 250).save(recipeOutput, "le_water");
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID.get("he_water"), 500).addFluidResult(GAS_MAP.get("hydrogen"), 250).addFluidResult(GAS_MAP.get("deuterium"), 250).addFluidResult(GAS_MAP.get("oxygen"), 250).save(recipeOutput, "he_water");
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(CUSTOM_FLUID.get("heavy_water"), 500).addFluidResult(GAS_MAP.get("deuterium"), 500).addFluidResult(GAS_MAP.get("oxygen"), 250).save(recipeOutput, "heavy_water");
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(ACID_MAP.get("hydrofluoric_acid"), 250).addFluidResult(GAS_MAP.get("hydrogen"), 250).addFluidResult(GAS_MAP.get("fluorine"), 250).save(recipeOutput, "hydrofluoric_acid");

        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("naoh"), 333).addFluidResult(MOLTEN_MAP.get("sodium"), 72).addFluidResult(Fluids.WATER, 250).addFluidResult(GAS_MAP.get("oxygen"), 125).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("koh"), 333).addFluidResult(MOLTEN_MAP.get("potassium"), 72).addFluidResult(Fluids.WATER, 250).addFluidResult(GAS_MAP.get("oxygen"), 125).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("alumina"), 72).addFluidResult(MOLTEN_MAP.get("aluminum"), 144).addFluidResult(GAS_MAP.get("oxygen"), 750).save(recipeOutput);
        new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), 666).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), 333).addFluidResult(GAS_MAP.get("hydrogen"), 333).save(recipeOutput);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get(name + "_fluoride"), 500).addFluidResult(FISSION_FUEL_MAP.get(name), 250).addFluidResult(GAS_MAP.get("fluorine"), 500).save(recipeOutput);
            new ProcessorRecipeBuilder(ElectrolyzerRecipe.class, 0.5, 1).addFluidInput(FISSION_FUEL_MAP.get("depleted_" + name + "_fluoride"), 500).addFluidResult(FISSION_FUEL_MAP.get("depleted_" + name), 250).addFluidResult(GAS_MAP.get("fluorine"), 500).save(recipeOutput);
        }
    }
}