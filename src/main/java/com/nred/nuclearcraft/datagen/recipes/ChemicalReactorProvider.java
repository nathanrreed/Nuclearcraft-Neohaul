package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ChemicalReactorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

public class ChemicalReactorProvider {
    public ChemicalReactorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron"), 144).addFluidInput(GAS_MAP.get("hydrogen"), 1500).addFluidResult(GAS_MAP.get("diborane"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("diborane"), 250).addFluidInput(Fluids.WATER, 1500).addFluidResult(ACID_MAP.get("boric_acid"), 500).addFluidResult(GAS_MAP.get("hydrogen"), 1500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("nitrogen"), 250).addFluidInput(GAS_MAP.get("hydrogen"), 750).addFluidResult(GAS_MAP.get("ammonia"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(ACID_MAP.get("boric_acid"), 500).addFluidInput(GAS_MAP.get("ammonia"), 500).addFluidResult(SALT_SOLUTION_MAP.get("boron_nitride_solution"), 333).addFluidResult(Fluids.WATER, 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("hydrogen"), 500).addFluidInput(GAS_MAP.get("oxygen"), 250).addFluidResult(Fluids.WATER, 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("deuterium"), 500).addFluidInput(GAS_MAP.get("oxygen"), 250).addFluidResult(CUSTOM_FLUID_MAP.get("heavy_water"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("hydrogen"), 250).addFluidInput(GAS_MAP.get("fluorine"), 250).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lithium"), 72).addFluidInput(GAS_MAP.get("fluorine"), 250).addFluidResult(MOLTEN_MAP.get("lif"), 72).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("beryllium"), 72).addFluidInput(GAS_MAP.get("fluorine"), 500).addFluidResult(MOLTEN_MAP.get("bef2"), 72).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("sulfur"), 333).addFluidInput(GAS_MAP.get("oxygen"), 500).addFluidResult(GAS_MAP.get("sulfur_dioxide"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("sulfur_dioxide"), 500).addFluidInput(GAS_MAP.get("oxygen"), 250).addFluidResult(GAS_MAP.get("sulfur_trioxide"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("sulfur_trioxide"), 250).addFluidInput(Fluids.WATER, 250).addFluidResult(ACID_MAP.get("sulfuric_acid"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("sulfur"), 333).addFluidInput(GAS_MAP.get("hydrogen"), 500).addFluidResult(GAS_MAP.get("hydrogen_sulfide"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 1).addFluidInput(SALT_SOLUTION_MAP.get("fluorite_water"), 333).addFluidInput(ACID_MAP.get("sulfuric_acid"), 500).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 1000).addFluidResult(SALT_SOLUTION_MAP.get("calcium_sulfate_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 333).addFluidInput(Fluids.WATER, 500).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), 333).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 333).addFluidInput(Fluids.WATER, 500).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), 333).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 3, 2).addFluidInput(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 666).addFluidInput(ACID_MAP.get("boric_acid"), 2000).addFluidResult(SALT_SOLUTION_MAP.get("borax_solution"), 333).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(GAS_MAP.get("ammonia"), 1000).addFluidInput(ACID_MAP.get("sulfuric_acid"), 500).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_sulfate_solution"), 333).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), 333).addFluidInput(Fluids.WATER, 1000).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), 666).addFluidResult(CUSTOM_FLUID_MAP.get("hydrogen_peroxide"), 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(MOLTEN_MAP.get("sodium"), 144).addFluidInput(MOLTEN_MAP.get("sulfur"), 333).addFluidResult(MOLTEN_MAP.get("sodium_sulfide"), 72).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(MOLTEN_MAP.get("potassium"), 144).addFluidInput(MOLTEN_MAP.get("sulfur"), 333).addFluidResult(MOLTEN_MAP.get("potassium_sulfide"), 72).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(GAS_MAP.get("oxygen_difluoride"), 250).addFluidInput(Fluids.WATER, 250).addFluidResult(GAS_MAP.get("oxygen"), 250).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(GAS_MAP.get("oxygen_difluoride"), 250).addFluidInput(GAS_MAP.get("sulfur_dioxide"), 250).addFluidResult(GAS_MAP.get("sulfur_trioxide"), 250).addFluidResult(GAS_MAP.get("fluorine"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("oxygen"), 250).addFluidInput(GAS_MAP.get("fluorine"), 500).addFluidResult(GAS_MAP.get("oxygen_difluoride"), 500).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(SUGAR_MAP.get("sugar"), 72).addFluidInput(Fluids.WATER, 500).addFluidResult(FLAMMABLE_MAP.get("ethanol"), 2000).addFluidResult(GAS_MAP.get("carbon_dioxide"), 2000).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("carbon_dioxide"), 250).addFluidInput(GAS_MAP.get("hydrogen"), 250).addFluidResult(GAS_MAP.get("carbon_monoxide"), 250).addFluidResult(Fluids.WATER, 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("carbon_monoxide"), 250).addFluidInput(GAS_MAP.get("hydrogen"), 500).addFluidResult(FLAMMABLE_MAP.get("methanol"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(FLAMMABLE_MAP.get("methanol"), 250).addFluidInput(ACID_MAP.get("hydrofluoric_acid"), 250).addFluidResult(GAS_MAP.get("fluoromethane"), 250).addFluidResult(Fluids.WATER, 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(GAS_MAP.get("fluoromethane"), 500).addFluidInput(MOLTEN_MAP.get("naoh"), 333).addFluidResult(GAS_MAP.get("ethylene"), 250).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(GAS_MAP.get("fluoromethane"), 500).addFluidInput(MOLTEN_MAP.get("koh"), 333).addFluidResult(GAS_MAP.get("ethylene"), 250).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 333).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(GAS_MAP.get("ethylene"), 250).addFluidInput(ACID_MAP.get("sulfuric_acid"), 250).addFluidResult(FLAMMABLE_MAP.get("ethanol"), 250).addFluidResult(GAS_MAP.get("sulfur_trioxide"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 3, 4).addFluidInput(GAS_MAP.get("ethylene"), 500).addFluidInput(GAS_MAP.get("oxygen"), 500).addFluidResult(GAS_MAP.get("acetylene"), 500).addFluidResult(Fluids.WATER, 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 3, 4).addFluidInput(GAS_MAP.get("ethylene"), 250).addFluidInput(GAS_MAP.get("acetylene"), 500).addFluidResult(FLAMMABLE_MAP.get("benzene"), 250).addFluidResult(GAS_MAP.get("hydrogen"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FLAMMABLE_MAP.get("benzene"), 500).addFluidInput(GAS_MAP.get("oxygen_difluoride"), 250).addFluidResult(FLAMMABLE_MAP.get("fluorobenzene"), 500).addFluidResult(Fluids.WATER, 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(FLAMMABLE_MAP.get("fluorobenzene"), 1000).addFluidInput(GAS_MAP.get("sulfur_trioxide"), 500).addFluidResult(MOLTEN_MAP.get("dfdps"), 333).addFluidResult(Fluids.WATER, 500).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FLAMMABLE_MAP.get("benzene"), 250).addFluidInput(GAS_MAP.get("fluorine"), 500).addFluidResult(FLAMMABLE_MAP.get("difluorobenzene"), 250).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), 500).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(FLAMMABLE_MAP.get("difluorobenzene"), 250).addFluidInput(MOLTEN_MAP.get("sodium_sulfide"), 36).addFluidResult(MOLTEN_MAP.get("polyphenylene_sulfide"), 36).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(FLAMMABLE_MAP.get("difluorobenzene"), 250).addFluidInput(MOLTEN_MAP.get("potassium_sulfide"), 36).addFluidResult(MOLTEN_MAP.get("polyphenylene_sulfide"), 36).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(GAS_MAP.get("fluoromethane"), 500).addFluidInput(MOLTEN_MAP.get("silicon"), 36).addFluidResult(FLAMMABLE_MAP.get("dimethyldifluorosilane"), 250).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(FLAMMABLE_MAP.get("dimethyldifluorosilane"), 250).addFluidInput(MOLTEN_MAP.get("sodium"), 72).addFluidResult(MOLTEN_MAP.get("polydimethylsilylene"), 36).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(FLAMMABLE_MAP.get("dimethyldifluorosilane"), 250).addFluidInput(MOLTEN_MAP.get("potassium"), 72).addFluidResult(MOLTEN_MAP.get("polydimethylsilylene"), 36).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(FLAMMABLE_MAP.get("benzene"), 500).addFluidInput(GAS_MAP.get("oxygen"), 250).addFluidResult(FLAMMABLE_MAP.get("phenol"), 500).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(FLAMMABLE_MAP.get("phenol"), 500).addFluidInput(CUSTOM_FLUID_MAP.get("hydrogen_peroxide"), 500).addFluidResult(SALT_SOLUTION_MAP.get("hydroquinone_solution"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(SALT_SOLUTION_MAP.get("hydroquinone_solution"), 333).addFluidInput(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution"), 1000).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), 333).addFluidResult(Fluids.WATER, 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(SALT_SOLUTION_MAP.get("hydroquinone_solution"), 333).addFluidInput(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution"), 1000).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), 333).addFluidResult(Fluids.WATER, 1000).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(MOLTEN_MAP.get("dfdps"), 333).addFluidInput(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), 333).addFluidResult(MOLTEN_MAP.get("polyethersulfone"), 72).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(MOLTEN_MAP.get("dfdps"), 333).addFluidInput(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), 333).addFluidResult(MOLTEN_MAP.get("polyethersulfone"), 72).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), 666).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(GAS_MAP.get("ethylene"), 250).addFluidInput(GAS_MAP.get("fluorine"), 500).addFluidResult(MOLTEN_MAP.get("polytetrafluoroethene"), 36).addFluidResult(GAS_MAP.get("hydrogen"), 500).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 2).addFluidInput(MOLTEN_MAP.get("boron"), 72).addFluidInput(HOT_GAS_MAP.get("arsenic"), 333).addFluidResult(MOLTEN_MAP.get("bas"), 333).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("alugentum"), 72).addFluidInput(GAS_MAP.get("oxygen"), 3000).addFluidResult(MOLTEN_MAP.get("alumina"), 144).addFluidResult(MOLTEN_MAP.get("silver"), 72).save(recipeOutput);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get(name), 72).addFluidInput(GAS_MAP.get("fluorine"), 500).addFluidResult(FISSION_FUEL_MAP.get(name + "_fluoride"), 72).save(recipeOutput);
        }
    }
}