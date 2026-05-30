package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ChemicalReactorRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.material.Fluids;

import static com.nred.nuclearcraft.datagen.ModFluidTagProvider.*;
import static com.nred.nuclearcraft.helpers.FissionConstants.FISSION_FUEL_FLUIDS;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;

public class ChemicalReactorProvider {
    public ChemicalReactorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("boron"), INGOT_VOLUME).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME * 3 / 2).addFluidResult(GAS_MAP.get("diborane"), BUCKET_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("diborane"), BUCKET_VOLUME / 4).addFluidInput(Fluids.WATER, BUCKET_VOLUME * 3 / 2).addFluidResult(ACID_MAP.get("boric_acid"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("hydrogen"), BUCKET_VOLUME * 3 / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(NITROGEN_TAG, BUCKET_VOLUME / 4).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME * 3 / 4).addFluidResult(GAS_MAP.get("ammonia"), BUCKET_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(ACID_MAP.get("boric_acid"), BUCKET_VOLUME / 2).addFluidInput(GAS_MAP.get("ammonia"), BUCKET_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("boron_nitride_solution"), GEM_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 4).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("deuterium"), BUCKET_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 4).addFluidResult(CUSTOM_FLUID_MAP.get("heavy_water"), BUCKET_VOLUME / 2).save(recipeOutput);

//        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("chlorine"), BUCKET_VOLUME / 4).addFluidResult(ACID_MAP.get("hydrogen_chloride"), BUCKET_VOLUME / 4).save(recipeOutput); TODO
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 4).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(MOLTEN_MAP.get("lithium"), INGOT_VOLUME / 2).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("lif"), INGOT_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(MOLTEN_MAP.get("beryllium"), INGOT_VOLUME / 2).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("bef2"), INGOT_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(MOLTEN_MAP.get("sulfur"), GEM_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("sulfur_dioxide"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("sulfur_dioxide"), BUCKET_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("sulfur_trioxide"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("sulfur_trioxide"), BUCKET_VOLUME / 4).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 4).addFluidResult(ACID_MAP.get("sulfuric_acid"), BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 0.5).addFluidInput(MOLTEN_MAP.get("sulfur"), GEM_VOLUME / 2).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("hydrogen_sulfide"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 0.5).addFluidInput(MOLTEN_MAP.get("sulfur"), GEM_VOLUME / 2).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME * 3 / 2).addFluidResult(GAS_MAP.get("sulfur_hexafluoride"), BUCKET_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 0.5).addFluidInput(SALT_SOLUTION_MAP.get("fluorite_water"), GEM_VOLUME / 2).addFluidInput(ACID_MAP.get("sulfuric_acid"), BUCKET_VOLUME / 2).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("calcium_sulfate_solution"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME / 2).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), GEM_VOLUME / 2).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), GEM_VOLUME / 2).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), GEM_VOLUME / 2).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1.5, 1).addFluidInput(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME).addFluidInput(ACID_MAP.get("boric_acid"), BUCKET_VOLUME * 2).addFluidResult(SALT_SOLUTION_MAP.get("borax_solution"), GEM_VOLUME / 2).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("ammonia"), BUCKET_VOLUME).addFluidInput(ACID_MAP.get("sulfuric_acid"), BUCKET_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_sulfate_solution"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(SALT_SOLUTION_MAP.get("ammonium_persulfate_solution"), GEM_VOLUME / 2).addFluidInput(Fluids.WATER, BUCKET_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("ammonium_bisulfate_solution"), GEM_VOLUME).addFluidResult(CUSTOM_FLUID_MAP.get("hydrogen_peroxide"), BUCKET_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("sodium"), INGOT_VOLUME).addFluidInput(MOLTEN_MAP.get("sulfur"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("sodium_sulfide"), INGOT_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("potassium"), INGOT_VOLUME).addFluidInput(MOLTEN_MAP.get("sulfur"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("potassium_sulfide"), INGOT_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(GAS_MAP.get("oxygen_difluoride"), BUCKET_VOLUME / 4).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("oxygen"), BUCKET_VOLUME / 4).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(GAS_MAP.get("oxygen_difluoride"), BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("sulfur_dioxide"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("sulfur_trioxide"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("oxygen_difluoride"), BUCKET_VOLUME / 2).save(recipeOutput);

//        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("manganese_dioxide"), INGOT_VOLUME).addFluidInput(MOLTEN_MAP.get("carbon"), 100).addFluidResult(MOLTEN_MAP.get("manganese"), BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("carbon_monoxide"), BUCKET_VOLUME).save(recipeOutput); TODO

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(SUGAR_MAP.get("sugar"), INGOT_VOLUME / 2).addFluidInput(Fluids.WATER, BUCKET_VOLUME / 2).addFluidResult(FLAMMABLE_MAP.get("ethanol"), BUCKET_VOLUME * 2).addFluidResult(GAS_MAP.get("carbon_dioxide"), BUCKET_VOLUME * 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("carbon_dioxide"), BUCKET_VOLUME / 4).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("carbon_monoxide"), BUCKET_VOLUME / 4).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("carbon_monoxide"), BUCKET_VOLUME / 4).addFluidInput(HYDROGEN_TAG, BUCKET_VOLUME / 2).addFluidResult(FLAMMABLE_MAP.get("methanol"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 2).addFluidInput(FLAMMABLE_MAP.get("methanol"), BUCKET_VOLUME / 4).addFluidInput(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("fluoromethane"), BUCKET_VOLUME / 4).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(FLAMMABLE_MAP.get("methanol"), BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("hydrogen"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("methane"), BUCKET_VOLUME / 4).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("fluoromethane"), BUCKET_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("naoh"), GEM_VOLUME / 2).addFluidResult(GAS_MAP.get("ethene"), BUCKET_VOLUME / 4).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(GAS_MAP.get("fluoromethane"), BUCKET_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("koh"), GEM_VOLUME / 2).addFluidResult(GAS_MAP.get("ethene"), BUCKET_VOLUME / 4).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 1).addFluidInput(GAS_MAP.get("ethene"), BUCKET_VOLUME / 4).addFluidInput(ACID_MAP.get("sulfuric_acid"), BUCKET_VOLUME / 4).addFluidResult(FLAMMABLE_MAP.get("ethanol"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("sulfur_trioxide"), BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1.5, 2).addFluidInput(GAS_MAP.get("ethene"), BUCKET_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 2).addFluidResult(GAS_MAP.get("ethyne"), BUCKET_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1.5, 2).addFluidInput(GAS_MAP.get("ethene"), BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("ethyne"), BUCKET_VOLUME / 2).addFluidResult(FLAMMABLE_MAP.get("benzene"), BUCKET_VOLUME / 4).addFluidResult(GAS_MAP.get("hydrogen"), BUCKET_VOLUME / 4).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(FLAMMABLE_MAP.get("benzene"), BUCKET_VOLUME / 2).addFluidInput(GAS_MAP.get("oxygen_difluoride"), BUCKET_VOLUME / 4).addFluidResult(FLAMMABLE_MAP.get("fluorobenzene"), BUCKET_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FLAMMABLE_MAP.get("fluorobenzene"), BUCKET_VOLUME).addFluidInput(GAS_MAP.get("sulfur_trioxide"), BUCKET_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("dfdps"), GEM_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FLAMMABLE_MAP.get("benzene"), BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).addFluidResult(FLAMMABLE_MAP.get("difluorobenzene"), BUCKET_VOLUME / 4).addFluidResult(ACID_MAP.get("hydrofluoric_acid"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(FLAMMABLE_MAP.get("difluorobenzene"), BUCKET_VOLUME / 4).addFluidInput(MOLTEN_MAP.get("sodium_sulfide"), INGOT_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("polyphenylene_sulfide"), INGOT_VOLUME / 4).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 4, 2).addFluidInput(FLAMMABLE_MAP.get("difluorobenzene"), BUCKET_VOLUME / 4).addFluidInput(MOLTEN_MAP.get("potassium_sulfide"), INGOT_VOLUME / 4).addFluidResult(MOLTEN_MAP.get("polyphenylene_sulfide"), INGOT_VOLUME / 4).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(GAS_MAP.get("fluoromethane"), BUCKET_VOLUME / 2).addFluidInput(MOLTEN_MAP.get("silicon"), INGOT_VOLUME / 4).addFluidResult(FLAMMABLE_MAP.get("dimethyldifluorosilane"), BUCKET_VOLUME / 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 1).addFluidInput(FLAMMABLE_MAP.get("dimethyldifluorosilane"), BUCKET_VOLUME / 4).addFluidInput(MOLTEN_MAP.get("sodium"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("polydimethylsilylene"), INGOT_VOLUME / 4).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 1).addFluidInput(FLAMMABLE_MAP.get("dimethyldifluorosilane"), BUCKET_VOLUME / 4).addFluidInput(MOLTEN_MAP.get("potassium"), INGOT_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("polydimethylsilylene"), INGOT_VOLUME / 4).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FLAMMABLE_MAP.get("benzene"), BUCKET_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME / 4).addFluidResult(FLAMMABLE_MAP.get("phenol"), BUCKET_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(FLAMMABLE_MAP.get("phenol"), BUCKET_VOLUME / 2).addFluidInput(CUSTOM_FLUID_MAP.get("hydrogen_peroxide"), BUCKET_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("hydroquinone_solution"), GEM_VOLUME / 2).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("hydroquinone_solution"), GEM_VOLUME / 2).addFluidInput(SALT_SOLUTION_MAP.get("sodium_hydroxide_solution"), GEM_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), GEM_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(SALT_SOLUTION_MAP.get("hydroquinone_solution"), GEM_VOLUME / 2).addFluidInput(SALT_SOLUTION_MAP.get("potassium_hydroxide_solution"), GEM_VOLUME).addFluidResult(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), GEM_VOLUME / 2).addFluidResult(Fluids.WATER, BUCKET_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("dfdps"), GEM_VOLUME / 2).addFluidInput(SALT_SOLUTION_MAP.get("sodium_hydroquinone_solution"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("polyethersulfone"), INGOT_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("sodium_fluoride_solution"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("dfdps"), GEM_VOLUME / 2).addFluidInput(SALT_SOLUTION_MAP.get("potassium_hydroquinone_solution"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("polyethersulfone"), INGOT_VOLUME / 2).addFluidResult(SALT_SOLUTION_MAP.get("potassium_fluoride_solution"), GEM_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 2, 1).addFluidInput(GAS_MAP.get("ethene"), BUCKET_VOLUME / 4).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("polytetrafluoroethene"), INGOT_VOLUME / 4).addFluidResult(GAS_MAP.get("hydrogen"), BUCKET_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron"), INGOT_VOLUME / 2).addFluidInput(HOT_GAS_MAP.get("arsenic"), GEM_VOLUME / 2).addFluidResult(MOLTEN_MAP.get("bas"), GEM_VOLUME / 2).save(recipeOutput);

        new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 1, 0.5).addFluidInput(MOLTEN_MAP.get("alugentum"), INGOT_VOLUME / 2).addFluidInput(OXYGEN_TAG, BUCKET_VOLUME * 3).addFluidResult(MOLTEN_MAP.get("alumina"), INGOT_VOLUME).addFluidResult(MOLTEN_MAP.get("silver"), INGOT_VOLUME / 2).save(recipeOutput);

        for (String name : FISSION_FUEL_FLUIDS) {
            new ProcessorRecipeBuilder(ChemicalReactorRecipe.class, 0.5, 0.5).addFluidInput(FISSION_FUEL_MAP.get(name), INGOT_VOLUME / 2).addFluidInput(GAS_MAP.get("fluorine"), BUCKET_VOLUME / 2).addFluidResult(FISSION_FUEL_MAP.get(name + "_fluoride"), INGOT_VOLUME / 2).save(recipeOutput);
        }
    }
}