package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.SeparatorRecipe;
import net.minecraft.data.recipes.RecipeOutput;

import java.util.List;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.ingotDust;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class SeparatorProvider {
    public SeparatorProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("uranium", 10)).addItemResult(URANIUM_MAP.get("238"), 9).addItemResult(URANIUM_MAP.get("235"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("boron", 12)).addItemResult(BORON_MAP.get("11"), 9).addItemResult(BORON_MAP.get("10"), 3).save(recipeOutput);
        new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(ingotDust("lithium", 10)).addItemResult(LITHIUM_MAP.get("7"), 9).addItemResult(LITHIUM_MAP.get("6"), 1).save(recipeOutput);

        for (String isotope : List.of("241", "242", "243")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(AMERICIUM_MAP.get(isotope + "_za"), 1).addItemResult(AMERICIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "americium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(AMERICIUM_MAP.get(isotope + "_c"), 1).addItemResult(AMERICIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "americium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("247", "248")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(BERKELIUM_MAP.get(isotope + "_za"), 1).addItemResult(BERKELIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "berkelium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(BERKELIUM_MAP.get(isotope + "_c"), 1).addItemResult(BERKELIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "berkelium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("249", "250", "251", "252")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(CALIFORNIUM_MAP.get(isotope + "_za"), 1).addItemResult(CALIFORNIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "californium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(CALIFORNIUM_MAP.get(isotope + "_c"), 1).addItemResult(CALIFORNIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "californium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("243", "245", "246", "247")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(CURIUM_MAP.get(isotope + "_za"), 1).addItemResult(CURIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "curium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(CURIUM_MAP.get(isotope + "_c"), 1).addItemResult(CURIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "curium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("236", "237")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(NEPTUNIUM_MAP.get(isotope + "_za"), 1).addItemResult(NEPTUNIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "neptunium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(NEPTUNIUM_MAP.get(isotope + "_c"), 1).addItemResult(NEPTUNIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "neptunium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("238", "239", "241", "242")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(PLUTONIUM_MAP.get(isotope + "_za"), 1).addItemResult(PLUTONIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "plutonium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(PLUTONIUM_MAP.get(isotope + "_c"), 1).addItemResult(PLUTONIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "plutonium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("233", "235", "238")) {
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(URANIUM_MAP.get(isotope + "_za"), 1).addItemResult(URANIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("zirconium"), 1).save(recipeOutput, "uranium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(SeparatorRecipe.class, 1, 1).addItemInput(URANIUM_MAP.get(isotope + "_c"), 1).addItemResult(URANIUM_MAP.get(isotope), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput, "uranium_" + isotope + "_from_c");
        }
    }
}