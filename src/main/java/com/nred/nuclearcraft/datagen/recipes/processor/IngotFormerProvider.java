package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.IngotFormerRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

import java.util.List;

import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;

public class IngotFormerProvider {
    public IngotFormerProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("steel"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("steel"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("ferroboron"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("ferroboron"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("tough"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("tough"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("hastelloy"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("hastelloy"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("beryllium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("beryllium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("zirconium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("zirconium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("palladium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("palladium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("holmium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("holmium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("dysprosium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("dysprosium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("gadolinium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("gadolinium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polydimethylsilylene"), INGOT_VOLUME).addItemResult(PART_MAP.get("polydimethylsilylene"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polytetrafluoroethene"), INGOT_VOLUME).addItemResult(PART_MAP.get("polytetrafluoroethene"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polymethylsilylene_methylene"), INGOT_VOLUME).addItemResult(PART_MAP.get("polymethylsilylene_methylene"), 1).save(recipeOutput);

        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("thorium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("thorium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("uranium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("uranium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("iron"), INGOT_VOLUME).addItemResult(Items.IRON_INGOT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("quartz"), GEM_VOLUME).addItemResult(Items.QUARTZ, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("prismarine"), INGOT_VOLUME).addItemResult(Items.PRISMARINE_SHARD, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lapis"), GEM_VOLUME).addItemResult(Items.LAPIS_LAZULI, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("gold"), INGOT_VOLUME).addItemResult(Items.GOLD_INGOT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("diamond"), GEM_VOLUME).addItemResult(Items.DIAMOND, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("emerald"), GEM_VOLUME).addItemResult(Items.EMERALD, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("slime"), INGOT_VOLUME).addItemResult(Items.SLIME_BALL, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("copper"), INGOT_VOLUME).addItemResult(Items.COPPER_INGOT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("tin"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("tin"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lead"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("lead"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("boron"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lithium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("lithium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("magnesium"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("magnesium"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("manganese"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("manganese"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("aluminum"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("aluminum"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("silver"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("silver"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("fluorite"), GEM_VOLUME).addItemResult(GEM_MAP.get("fluorite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("villiaumite"), GEM_VOLUME).addItemResult(GEM_MAP.get("villiaumite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("carobbiite"), GEM_VOLUME).addItemResult(GEM_MAP.get("carobbiite"), 1).save(recipeOutput);

        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("hard_carbon"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("hard_carbon"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("manganese_dioxide"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("manganese_dioxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lead_platinum"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("lead_platinum"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("nickel_oxide"), INGOT_VOLUME).addItemResult(INGOT_MAP.get("nickel_oxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 2).addFluidInput(MOLTEN_MAP.get("bas"), GEM_VOLUME).addItemResult(GEM_MAP.get("boron_arsenide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(HOT_GAS_MAP.get("sic_vapor"), INGOT_VOLUME).addItemResult(ALLOY_MAP.get("silicon_carbide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("polyethersulfone"), INGOT_VOLUME).addItemResult(PART_MAP.get("polyethersulfone"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("coal"), COAL_DUST_VOLUME).addItemResult(INGOT_MAP.get("graphite"), 1).save(recipeOutput);

//    TODO    new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("barium_oxide"), INGOT_VOLUME).addItemResult(DUST_MAP.get("barium_oxide"), 1).save(recipeOutput);

        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("silicon"), INGOT_VOLUME).addItemResult(GEM_MAP.get("silicon"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("obsidian"), SEARED_BLOCK_VOLUME).addItemResult(Items.OBSIDIAN, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 1).addFluidInput(MOLTEN_MAP.get("nether_brick"), SEARED_MATERIAL_VOLUME).addItemResult(Items.NETHER_BRICK, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("end_stone"), SEARED_BLOCK_VOLUME).addItemResult(Items.END_STONE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 2, 1).addFluidInput(MOLTEN_MAP.get("purpur"), SEARED_BLOCK_VOLUME).addItemResult(Items.PURPUR_BLOCK, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("cocoa_butter"), INGOT_VOLUME).addItemResult(FOOD_MAP.get("cocoa_butter"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("unsweetened_chocolate"), INGOT_VOLUME).addItemResult(FOOD_MAP.get("unsweetened_chocolate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("dark_chocolate"), INGOT_VOLUME).addItemResult(FOOD_MAP.get("dark_chocolate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(CHOCOLATE_MAP.get("milk_chocolate"), INGOT_VOLUME).addItemResult(FOOD_MAP.get("milk_chocolate"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(SUGAR_MAP.get("sugar"), INGOT_VOLUME).addItemResult(Items.SUGAR, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(SUGAR_MAP.get("gelatin"), INGOT_VOLUME).addItemResult(FOOD_MAP.get("gelatin"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(IngotFormerRecipe.class, 0.5, 0.5).addFluidInput(SUGAR_MAP.get("marshmallow"), INGOT_VOLUME).addItemResult(FOOD_MAP.get("marshmallow"), 1).save(recipeOutput);

        for (String isotope : List.of("241", "242", "243")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("americium_" + isotope), INGOT_VOLUME).addItemResult(AMERICIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("247", "248")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("berkelium_" + isotope), INGOT_VOLUME).addItemResult(BERKELIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("10", "11")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("boron_" + isotope), INGOT_VOLUME).addItemResult(BORON_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("249", "250", "251", "252")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("californium_" + isotope), INGOT_VOLUME).addItemResult(CALIFORNIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("243", "245", "246", "247")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("curium_" + isotope), INGOT_VOLUME).addItemResult(CURIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("6", "7")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(MOLTEN_MAP.get("lithium_" + isotope), INGOT_VOLUME).addItemResult(LITHIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("236", "237")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("neptunium_" + isotope), INGOT_VOLUME).addItemResult(NEPTUNIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("238", "239", "241", "242")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("plutonium_" + isotope), INGOT_VOLUME).addItemResult(PLUTONIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
        for (String isotope : List.of("233", "235", "238")) {
            new ProcessorRecipeBuilder(IngotFormerRecipe.class, 1, 1).addFluidInput(FISSION_FUEL_MAP.get("uranium_" + isotope), INGOT_VOLUME).addItemResult(URANIUM_MAP.get(isotope), 1).save(recipeOutput);
        }
    }
}