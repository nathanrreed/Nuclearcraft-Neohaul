package com.nred.nuclearcraft.datagen.recipes.processor;

import com.google.common.collect.Sets;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.DecayHastenerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class DecayHastenerProvider {
    public DecayHastenerProvider(RecipeOutput recipeOutput) { // TODO change times with radiation
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("thorium"), 1).addItemResult(DUST_MAP.get("lead"), 1).save(recipeOutput, "lead_from_thorium");
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(FISSION_DUST_MAP.get("radium"), 1).addItemResult(DUST_MAP.get("lead"), 1).save(recipeOutput, "lead_from_radium");
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(FISSION_DUST_MAP.get("polonium"), 1).addItemResult(DUST_MAP.get("lead"), 1).save(recipeOutput, "lead_from_polonium");
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(FISSION_DUST_MAP.get("tbp"), 1).addItemResult(FUEL_THORIUM_MAP.get("tbu"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(FISSION_DUST_MAP.get("protactinium_233"), 1).addItemResult(URANIUM_MAP.get("233"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(FISSION_DUST_MAP.get("strontium_90"), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(DecayHastenerRecipe.class, 1, 1).addItemInput(FISSION_DUST_MAP.get("ruthenium_106"), 1).addItemResult(DUST_MAP.get("palladium"), 1).save(recipeOutput);

        addIsotopes(recipeOutput, URANIUM_MAP, "233", FISSION_DUST_MAP, "bismuth", 1, 1);
        addIsotopes(recipeOutput, URANIUM_MAP, "235", DUST_MAP, "lead", 1, 1);
        addIsotopes(recipeOutput, URANIUM_MAP, "238", FISSION_DUST_MAP, "radium", 1, 1);

        addIsotopes(recipeOutput, NEPTUNIUM_MAP, "236", DUST_MAP, "thorium", 1, 1);
        addIsotopes(recipeOutput, NEPTUNIUM_MAP, "237", URANIUM_MAP, "233", 1, 1);

        addIsotopes(recipeOutput, PLUTONIUM_MAP, "238", DUST_MAP, "lead", 1, 1);
        addIsotopes(recipeOutput, PLUTONIUM_MAP, "239", URANIUM_MAP, "235", 1, 1);
        addIsotopes(recipeOutput, PLUTONIUM_MAP, "241", NEPTUNIUM_MAP, "237", 1, 1);
        addIsotopes(recipeOutput, PLUTONIUM_MAP, "242", URANIUM_MAP, "238", 1, 1);

        addIsotopes(recipeOutput, AMERICIUM_MAP, "241", NEPTUNIUM_MAP, "237", 1, 1);
        addIsotopes(recipeOutput, AMERICIUM_MAP, "242", DUST_MAP, "lead", 1, 1);
        addIsotopes(recipeOutput, AMERICIUM_MAP, "243", PLUTONIUM_MAP, "239", 1, 1);

        addIsotopes(recipeOutput, CURIUM_MAP, "243", PLUTONIUM_MAP, "239", 1, 1);
        addIsotopes(recipeOutput, CURIUM_MAP, "245", PLUTONIUM_MAP, "241", 1, 1);
        addIsotopes(recipeOutput, CURIUM_MAP, "246", PLUTONIUM_MAP, "242", 1, 1);
        addIsotopes(recipeOutput, CURIUM_MAP, "247", AMERICIUM_MAP, "243", 1, 1);

        addIsotopes(recipeOutput, BERKELIUM_MAP, "247", AMERICIUM_MAP, "243", 1, 1);
        addIsotopes(recipeOutput, BERKELIUM_MAP, "248", DUST_MAP, "thorium", 1, 1);

        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "249", CURIUM_MAP, "245", 1, 1);
        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "250", CURIUM_MAP, "246", 1, 1);
        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "251", CURIUM_MAP, "247", 1, 1);
        addIsotopes(recipeOutput, CALIFORNIUM_MAP, "252", DUST_MAP, "thorium", 1, 1);
    }

    private static final Set<String> NON_FISSION = Sets.newHashSet("thorium", "lead", "bismuth", "thallium", "radium", "polonium", "tbp", "zirconium", "palladium", "barium", "neodymium", "gadolinium");

    private void addIsotopes(RecipeOutput recipeOutput, Map<String, DeferredItem<Item>> inputMap, String input, Map<String, DeferredItem<Item>> outputMap, String output, double timeModifier, double powerModifier) {
        if (NON_FISSION.contains(output)) {
            new ProcessorRecipeBuilder(DecayHastenerRecipe.class, timeModifier, powerModifier).addItemInput(Ingredient.of(inputMap.get(input), inputMap.get(input + "_ox"), inputMap.get(input + "_ni")), 1).addItemResult(outputMap.get(output), 1).save(recipeOutput, output + "_from_" + input);
        } else {
            for (String suffix : List.of("", "_c", "_ox", "_ni", "_za")) {
                new ProcessorRecipeBuilder(DecayHastenerRecipe.class, timeModifier, powerModifier).addItemInput(inputMap.get(input + suffix), 1).addItemResult(outputMap.get(output + suffix), 1).save(recipeOutput, outputMap.get(output + suffix).getId().getPath() + "_from_" + inputMap.get(input + suffix).getId().getPath());
            }
        }
    }
}