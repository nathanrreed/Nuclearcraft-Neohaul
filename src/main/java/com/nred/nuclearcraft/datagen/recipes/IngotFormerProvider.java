package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.IngotFormerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import java.util.List;

import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class IngotFormerProvider {
    public IngotFormerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("steel"), 144).addItemResult(ALLOY_MAP.get("steel"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("ferroboron"), 144).addItemResult(ALLOY_MAP.get("ferroboron"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("tough"), 144).addItemResult(ALLOY_MAP.get("tough"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("hard_carbon"), 144).addItemResult(ALLOY_MAP.get("hard_carbon"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lead_platinum"), 144).addItemResult(ALLOY_MAP.get("lead_platinum"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("silicon"), 144).addItemResult(ALLOY_MAP.get("silicon_carbide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("coal"), 100).addItemResult(INGOT_MAP.get("graphite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("manganese_dioxide"), 144).addItemResult(INGOT_MAP.get("manganese_dioxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("beryllium"), 144).addItemResult(INGOT_MAP.get("beryllium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("zirconium"), 144).addItemResult(INGOT_MAP.get("zirconium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polydimethylsilylene"), 144).addItemResult(PART_MAP.get("polydimethylsilylene"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polyethersulfone"), 144).addItemResult(PART_MAP.get("polyethersulfone"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polytetrafluoroethene"), 144).addItemResult(PART_MAP.get("polytetrafluoroethene"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polymethylsilylene_methylene"), 144).addItemResult(PART_MAP.get("polymethylsilylene_methylene"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("thorium"), 144).addItemResult(INGOT_MAP.get("thorium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("uranium"), 144).addItemResult(INGOT_MAP.get("uranium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("iron"), 144).addItemResult(Items.IRON_INGOT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("quartz"), 666).addItemResult(Items.QUARTZ, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("prismarine"), 144).addItemResult(Items.PRISMARINE_SHARD, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lapis"), 666).addItemResult(Items.LAPIS_LAZULI, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("gold"), 144).addItemResult(Items.GOLD_INGOT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("diamond"), 666).addItemResult(Items.DIAMOND, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("emerald"), 666).addItemResult(Items.EMERALD, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("slime"), 144).addItemResult(Items.SLIME_BALL, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("copper"), 144).addItemResult(Items.COPPER_INGOT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("tin"), 144).addItemResult(INGOT_MAP.get("tin"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lead"), 144).addItemResult(INGOT_MAP.get("lead"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron"), 144).addItemResult(INGOT_MAP.get("boron"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lithium"), 144).addItemResult(INGOT_MAP.get("lithium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("magnesium"), 144).addItemResult(INGOT_MAP.get("magnesium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("manganese"), 144).addItemResult(INGOT_MAP.get("manganese"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("aluminum"), 144).addItemResult(INGOT_MAP.get("aluminum"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("silver"), 144).addItemResult(INGOT_MAP.get("silver"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("fluorite"), 666).addItemResult(GEM_MAP.get("fluorite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("villiaumite"), 666).addItemResult(GEM_MAP.get("villiaumite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("carobbiite"), 666).addItemResult(GEM_MAP.get("carobbiite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("bas"), 666).addItemResult(GEM_MAP.get("boron_arsenide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("silicon"), 144).addItemResult(GEM_MAP.get("silicon"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("obsidian"), 288).addItemResult(Items.OBSIDIAN, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("nether_brick"), 72).addItemResult(Items.NETHER_BRICK, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("end_stone"), 288).addItemResult(Items.END_STONE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("purpur"), 288).addItemResult(Items.PURPUR_BLOCK, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(CHOCOLATE_MAP.get("cocoa_butter"), 144).addItemResult(FOOD_MAP.get("cocoa_butter"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(CHOCOLATE_MAP.get("unsweetened_chocolate"), 144).addItemResult(FOOD_MAP.get("unsweetened_chocolate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(CHOCOLATE_MAP.get("dark_chocolate"), 144).addItemResult(FOOD_MAP.get("dark_chocolate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(CHOCOLATE_MAP.get("milk_chocolate"), 144).addItemResult(FOOD_MAP.get("milk_chocolate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(SUGAR_MAP.get("sugar"), 144).addItemResult(Items.SUGAR, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(SUGAR_MAP.get("gelatin"), 144).addItemResult(FOOD_MAP.get("gelatin"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(SUGAR_MAP.get("marshmallow"), 144).addItemResult(FOOD_MAP.get("marshmallow"), 1).save(recipeOutput);

        for (String isotope : List.of("241", "242", "243")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("americium_" + isotope), 144).addItemResult(AMERICIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("247", "248")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("berkelium_" + isotope), 144).addItemResult(BERKELIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("10", "11")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron_" + isotope), 144).addItemResult(BORON_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("249", "250", "251", "252")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("californium_" + isotope), 144).addItemResult(CALIFORNIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("243", "245", "246", "247")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_" + isotope), 144).addItemResult(CURIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("6", "7")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lithium_" + isotope), 144).addItemResult(LITHIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("236", "237")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("neptunium_" + isotope), 144).addItemResult(NEPTUNIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("238", "239", "241", "242")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("plutonium_" + isotope), 144).addItemResult(PLUTONIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("233", "235", "238")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("uranium_" + isotope), 144).addItemResult(URANIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
    }
}