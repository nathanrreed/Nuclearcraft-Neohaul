package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.SizedChanceItemIngredient;
import com.nred.nuclearcraft.recipe.processor.AlloyFurnaceRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class AlloyFurnaceRecipeProvider {
    public AlloyFurnaceRecipeProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(ingotDusts("copper", 3, "tin", 1)).addItemResult(ALLOY_MAP.get("bronze"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(ingotDust("iron", 1), new SizedChanceItemIngredient(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(tag(Tags.Items.DUSTS, "coal")), new Ingredient.TagValue(tag(Tags.Items.DUSTS, "graphite")), new Ingredient.TagValue(tag(Tags.Items.INGOTS, "graphite")), new Ingredient.ItemValue(new ItemStack(Items.COAL)))), 2))).addItemResult(ALLOY_MAP.get("steel"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1.5).addItemInputs(ingotDusts("steel", 1, "boron", 1)).addItemResult(ALLOY_MAP.get("ferroboron"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1.5).addItemInputs(List.of(SizedChanceItemIngredient.of(ALLOY_MAP.get("ferroboron"), 1), ingotDust("lithium", 1))).addItemResult(ALLOY_MAP.get("tough"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 2).addItemInputs(List.of(ingotDust("graphite", 1), gemDust("diamond", 1))).addItemResult(ALLOY_MAP.get("hard_carbon"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(ingotDusts("magnesium", 1, "boron", 2)).addItemResult(ALLOY_MAP.get("magnesium_diboride"), 3).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1).addItemInputs(ingotDusts("lithium", 1, "manganese_dioxide", 1)).addItemResult(ALLOY_MAP.get("lithium_manganese_dioxide"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addItemInputs(ingotDusts("copper", 3, "silver", 1)).addItemResult(ALLOY_MAP.get("shibuichi"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addItemInputs(ingotDusts("tin", 3, "silver", 1)).addItemResult(ALLOY_MAP.get("tin_silver"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addItemInputs(ingotDusts("lead", 3, "platinum", 1)).addItemResult(ALLOY_MAP.get("lead_platinum"), 4).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addItemInputs(List.of(SizedChanceItemIngredient.of(ALLOY_MAP.get("tough"), 1), SizedChanceItemIngredient.of(ALLOY_MAP.get("hard_carbon"), 1))).addItemResult(ALLOY_MAP.get("extreme"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1.5).addItemInputs(List.of(SizedChanceItemIngredient.of(ALLOY_MAP.get("extreme"), 1), SizedChanceItemIngredient.of(GEM_MAP.get("boron_arsenide"), 1))).addItemResult(ALLOY_MAP.get("thermoconducting"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 4, 1).addItemInputs(ingotDusts("zirconium", 7, "tin", 1)).addItemResult(ALLOY_MAP.get("zircaloy"), 8).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addItemInputs(List.of(SizedChanceItemIngredient.of(tag(Tags.Items.GEMS, "silicon"), 1), ingotDust("graphite", 1))).addItemResult(ALLOY_MAP.get("silicon_carbide"), 2).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 8, 2).addItemInputs(List.of(ingotDust("iron", 15), new SizedChanceItemIngredient(Ingredient.of(COMPOUND_MAP.get("c_mn_blend")), 1))).addItemResult(ALLOY_MAP.get("hsla_steel"), 16).save(recipeOutput);
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 8, 2).addItemInputs(List.of(new SizedChanceItemIngredient(Ingredient.of(INGOT_MAP.get("zirconium")), 1), new SizedChanceItemIngredient(Ingredient.of(FISSION_DUST_MAP.get("molybdenum")), 15))).addItemResult(ALLOY_MAP.get("zirconium_molybdenum"), 16).save(recipeOutput);
//        if (!ModCheck.qmdLoaded()) { TODO
//            addAlloyIngotIngotRecipes("Nickel", 1, "Chromium", 1, "Nichrome", 2, 1D, 1D);
//        }
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 2, 2).addItemInputs(List.of(ingotDust("nichrome", 3))).addItemInput(new SizedChanceItemIngredient(Ingredient.of(FISSION_DUST_MAP.get("molybdenum")), 1)).addItemResult(ALLOY_MAP.get("hastelloy"), 4).save(ingotExists(recipeOutput, "nichrome"));

        // Thermal Foundation (Modern Industrialization)
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 0.5).addItemInputs(List.of(ingotDust("gold", 1), ingotDust("silver", 1))).addItemResult(ingot("electrum", 2)).save(ingotExists(recipeOutput, "electrum"), "electrum");
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1.5).addItemInputs(List.of(ingotDust("iron", 2), ingotDust("nickel", 1))).addItemResult(ingot("invar", 3)).save(ingotExists(recipeOutput, "invar"), "invar");

        // EnderIO
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(ingotDust("iron", 1), ingotDust("copper", 1))).addItemResult(ingot("conductive_alloy", 2)).save(ingotExists(recipeOutput, "conductive_alloy"), "conductive_alloy_ingot");
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(Items.REDSTONE, 1), ingotDust("copper", 1))).addItemResult(ingot("redstone_alloy", 2)).save(ingotExists(recipeOutput, "redstone_alloy"), "redstone_alloy_ingot");
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 1).addItemInputs(List.of(ingotDust("iron", 1), SizedChanceItemIngredient.of(Items.ENDER_PEARL, 1))).addItemResult(ingot("pulsating_alloy", 2)).save(ingotExists(recipeOutput, "pulsating_alloy"), "pulsating_alloy_ingot");
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1.5, 0.5).addItemInputs(List.of(SizedChanceItemIngredient.of(ItemTags.SOUL_FIRE_BASE_BLOCKS, 1), ingotDust("gold", 1))).addItemResult(ingot("soularium", 2)).save(ingotExists(recipeOutput, "soularium"), "soularium_ingot");

        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1.5).addItemInputs(List.of(gemDust("diamond", 1), nugget("pulsating_alloy", 8))).addItemResult(gem("pulsating_crystal", 1)).save(gemExists(recipeOutput, "pulsating_crystal"), "pulsating_crystal");
        new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1.5).addItemInputs(List.of(gemDust("emerald", 1), nugget("vibrant_alloy", 8))).addItemResult(gem("vibrant_crystal", 2)).save(gemExists(recipeOutput, "vibrant_crystal"), "vibrant_crystal");

        // Isotopes
        for (String isotope : List.of("241", "242", "243")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(AMERICIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(AMERICIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "americium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(AMERICIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(AMERICIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "americium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("247", "248")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(BERKELIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(BERKELIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "berkelium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(BERKELIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(BERKELIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "berkelium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("249", "250", "251", "252")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(CALIFORNIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(CALIFORNIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "californium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(CALIFORNIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(CALIFORNIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "californium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("243", "245", "246", "247")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(CURIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(CURIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "curium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(CURIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(CURIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "curium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("236", "237")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(NEPTUNIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(NEPTUNIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "neptunium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(NEPTUNIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(NEPTUNIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "neptunium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("238", "239", "241", "242")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(PLUTONIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(PLUTONIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "plutonium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(PLUTONIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(PLUTONIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "plutonium_" + isotope + "_from_c");
        }
        for (String isotope : List.of("233", "235", "238")) {
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(URANIUM_MAP.get(isotope), 1), ingotDust("zirconium", 1))).addItemResult(URANIUM_MAP.get(isotope + "_za"), 1).save(recipeOutput, "uranium_" + isotope + "_from_za");
            new ProcessorRecipeBuilder(AlloyFurnaceRecipe.class, 1, 1).addItemInputs(List.of(SizedChanceItemIngredient.of(URANIUM_MAP.get(isotope), 1), ingotDust("graphite", 1))).addItemResult(URANIUM_MAP.get(isotope + "_c"), 1).save(recipeOutput, "uranium_" + isotope + "_from_c");
        }
    }
}