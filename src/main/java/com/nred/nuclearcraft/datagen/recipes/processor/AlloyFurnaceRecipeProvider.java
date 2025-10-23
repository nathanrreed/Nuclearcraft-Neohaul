package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.AlloyFurnaceRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.List;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class AlloyFurnaceRecipeProvider {
    public AlloyFurnaceRecipeProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(ingotDusts("copper", 3, "tin", 1)).addItemResult(ALLOY_MAP.get("bronze"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(ingotDust("iron", 1), new SizedIngredient(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(tag(Tags.Items.DUSTS, "coal")), new Ingredient.TagValue(tag(Tags.Items.DUSTS, "graphite")), new Ingredient.TagValue(tag(Tags.Items.INGOTS, "graphite")), new Ingredient.ItemValue(new ItemStack(Items.COAL)))), 2))).addItemResult(ALLOY_MAP.get("steel"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1.5).addItemInput(ingotDusts("steel", 1, "boron", 1)).addItemResult(ALLOY_MAP.get("ferroboron"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1.5).addItemInput(List.of(SizedIngredient.of(ALLOY_MAP.get("ferroboron"), 1), ingotDust("lithium", 1))).addItemResult(ALLOY_MAP.get("tough"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 2).addItemInput(List.of(ingotDust("graphite", 1), gemDust("diamond", 1))).addItemResult(ALLOY_MAP.get("hard_carbon"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(ingotDusts("magnesium", 1, "boron", 2)).addItemResult(ALLOY_MAP.get("magnesium_diboride"), 3).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1).addItemInput(ingotDusts("lithium", 1, "manganese_dioxide", 1)).addItemResult(ALLOY_MAP.get("lithium_manganese_dioxide"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addItemInput(ingotDusts("copper", 3, "silver", 1)).addItemResult(ALLOY_MAP.get("shibuichi"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addItemInput(ingotDusts("tin", 3, "silver", 1)).addItemResult(ALLOY_MAP.get("tin_silver"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addItemInput(List.of(SizedIngredient.of(ALLOY_MAP.get("tough"), 1), SizedIngredient.of(ALLOY_MAP.get("hard_carbon"), 1))).addItemResult(ALLOY_MAP.get("extreme"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1.5).addItemInput(List.of(SizedIngredient.of(ALLOY_MAP.get("extreme"), 1), SizedIngredient.of(GEM_MAP.get("boron_arsenide"), 1))).addItemResult(ALLOY_MAP.get("thermoconducting"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 4, 1).addItemInput(ingotDusts("zirconium", 7, "tin", 1)).addItemResult(ALLOY_MAP.get("zircaloy"), 8).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addItemInput(List.of(SizedIngredient.of(tag(Tags.Items.GEMS, "silicon"), 1), ingotDust("graphite", 1))).addItemResult(ALLOY_MAP.get("silicon_carbide"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 8, 2).addItemInput(List.of(ingotDust("iron", 15), new SizedIngredient(Ingredient.of(COMPOUND_MAP.get("c_mn_blend")), 1))).addItemResult(ALLOY_MAP.get("hsla_steel"), 16).save(recipeOutput);

        for (String isotope : List.of("241", "242", "243")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(AMERICIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(AMERICIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "americium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(AMERICIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(AMERICIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "americium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("247", "248")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(BERKELIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(BERKELIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "berkelium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(BERKELIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(BERKELIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "berkelium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("249", "250", "251", "252")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(CALIFORNIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(CALIFORNIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "californium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(CALIFORNIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(CALIFORNIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "californium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("243", "245", "246", "247")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(CURIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(CURIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "curium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(CURIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(CURIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "curium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("236", "237")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(NEPTUNIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(NEPTUNIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "neptunium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(NEPTUNIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(NEPTUNIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "neptunium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("238", "239", "241", "242")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(PLUTONIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(PLUTONIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "plutonium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(PLUTONIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(PLUTONIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "plutonium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("233", "235", "238")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(URANIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(URANIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "uranium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInput(List.of(SizedIngredient.of(URANIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(URANIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "uranium_" + isotope + "_from_c");
        }
    }
}