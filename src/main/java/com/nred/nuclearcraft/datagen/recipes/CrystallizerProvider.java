package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.CrystallizerRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import static com.nred.nuclearcraft.registration.FluidRegistration.SALT_SOLUTION_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.COMPOUND_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEM_DUST_MAP;

public class CrystallizerProvider {
    public CrystallizerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("boron_nitride_solution"), 666).addItemResult(GEM_DUST_MAP.get("boron_nitride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("fluorite_water"), 666).addItemResult(GEM_DUST_MAP.get("fluorite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("calcium_sulfate_solution"), 666).addItemResult(COMPOUND_MAP.get("calcium_sulfate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 666).addItemResult(COMPOUND_MAP.get("sodium_fluoride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 666).addItemResult(COMPOUND_MAP.get("potassium_fluoride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 0.5, 0.5).addFluidInput(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution"), 666).addItemResult(COMPOUND_MAP.get("sodium_hydroxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 0.5, 0.5).addFluidInput(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution"), 666).addItemResult(COMPOUND_MAP.get("potassium_hydroxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 0.5, 0.5).addFluidInput(SALT_SOLUTION_MAP.get("borax_solution"), 666).addItemResult(COMPOUND_MAP.get("borax"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 0.5, 0.5).addFluidInput(SALT_SOLUTION_MAP.get("irradiated_borax_solution"), 666).addItemResult(COMPOUND_MAP.get("irradiated_borax"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_sulfate_solution"), 666).addItemResult(COMPOUND_MAP.get("ammonium_sulfate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), 666).addItemResult(COMPOUND_MAP.get("ammonium_bisulfate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), 666).addItemResult(COMPOUND_MAP.get("ammonium_persulfate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("hydroquinone_solution"), 666).addItemResult(COMPOUND_MAP.get("hydroquinone"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), 666).addItemResult(COMPOUND_MAP.get("sodium_hydroquinone"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(CrystallizerRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), 666).addItemResult(COMPOUND_MAP.get("potassium_hydroquinone"), 1).save(recipeOutput);
    }
}