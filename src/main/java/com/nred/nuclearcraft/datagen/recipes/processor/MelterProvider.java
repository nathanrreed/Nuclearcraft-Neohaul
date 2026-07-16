package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.info.NCFluid;
import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.MelterRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;

import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.qmdNotLoaded;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.tag;
import static com.nred.nuclearcraft.registration.BlockRegistration.INGOT_BLOCK_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static com.nred.nuclearcraft.util.FluidStackHelper.*;


public class MelterProvider {
    public void createSet(RecipeOutput recipeOutput, String name, Map<String, NCFluid> map) {
        createSet(recipeOutput, name, map, name);
    }

    public void createSet(RecipeOutput recipeOutput, String name, Map<String, NCFluid> map, String result) {
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(tag(Tags.Items.INGOTS, name), 1).addFluidResult(map.get(result), INGOT_VOLUME).save(recipeOutput, "molten_" + name);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(tag(Tags.Items.DUSTS, name), 1).addFluidResult(map.get(result), INGOT_VOLUME).save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag(Tags.Items.DUSTS, name)))), "molten_" + name + "_from_dust");
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(tag(Tags.Items.RAW_MATERIALS, name), 1).addFluidResult(map.get(result), 33, INGOT_VOLUME * 2, INGOT_VOLUME, INGOT_VOLUME / 4).save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag(Tags.Items.RAW_MATERIALS, name)))), "molten_" + name + "_from_raw");
        new ProcessorRecipeBuilder(MelterRecipe.class, 9, 1).addItemInput(tag(Tags.Items.STORAGE_BLOCKS, name), 1).addFluidResult(map.get(result), INGOT_BLOCK_VOLUME).save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag(Tags.Items.STORAGE_BLOCKS, name)))), "molten_" + name + "_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 9, 1).addItemInput(tag(Tags.Items.STORAGE_BLOCKS, "raw_" + name), 1).addFluidResult(map.get(result), INGOT_BLOCK_VOLUME).save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag(Tags.Items.STORAGE_BLOCKS, "raw_" + name)))), "molten_" + name + "_from_raw_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 1.0 / 9, 0.25).addItemInput(tag(Tags.Items.NUGGETS, name), 1).addFluidResult(map.get(result), NUGGET_VOLUME).save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag(Tags.Items.NUGGETS, name)))), "molten_" + name + "_from_nugget");
        new ProcessorRecipeBuilder(MelterRecipe.class, 1.25, 1.5).addItemInput(tag(Tags.Items.ORES, name), 1).addFluidResult(map.get(result), INGOT_VOLUME * 3).save(recipeOutput.withConditions(new NotCondition(new TagEmptyCondition(tag(Tags.Items.ORES, name)))), "molten_" + name + "_from_ore");
    }

    public void createGemSet(RecipeOutput recipeOutput, String name) {
        createGemSet(recipeOutput, name, 9);
    }

    public void createGemSet(RecipeOutput recipeOutput, String name, int amount) {
        if (!MOLTEN_MAP.containsKey(name)) return;
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(tag(Tags.Items.GEMS, name), 1).addFluidResult(MOLTEN_MAP.get(name), GEM_VOLUME).save(recipeOutput, "molten_" + name + "_from_gem");
        new ProcessorRecipeBuilder(MelterRecipe.class, amount, 1).addItemInput(tag(Tags.Items.STORAGE_BLOCKS, name), 1).addFluidResult(MOLTEN_MAP.get(name), GEM_VOLUME * amount).save(recipeOutput, "molten_" + name + "_from_block");
    }

    public MelterProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(tag(Tags.Items.DUSTS, "sulfur"), 1).addFluidResult(MOLTEN_MAP.get("sulfur"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("sodium_hydroxide"), 1).addFluidResult(MOLTEN_MAP.get("naoh"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("potassium_hydroxide"), 1).addFluidResult(MOLTEN_MAP.get("koh"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("barium_sulfide"), 1).addFluidResult(MOLTEN_MAP.get("barium_sulfide"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("arsenic"), 1).addFluidResult(HOT_GAS_MAP.get("arsenic"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(tag(Tags.Items.GEMS, "boron_arsenide"), 1).addFluidResult(MOLTEN_MAP.get("bas"), GEM_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.PRISMARINE_SHARD, Items.PRISMARINE_CRYSTALS), 1).addFluidResult(MOLTEN_MAP.get("prismarine"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 0.5).addItemInput(Items.SLIME_BALL, 1).addFluidResult(MOLTEN_MAP.get("slime"), INGOT_VOLUME).save(recipeOutput, "molten_slime_from_slime");
        new ProcessorRecipeBuilder(MelterRecipe.class, 9, 0.5).addItemInput(Items.SLIME_BLOCK, 1).addFluidResult(MOLTEN_MAP.get("slime"), INGOT_BLOCK_VOLUME).save(recipeOutput, "molten_slime_from_block");

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(GEM_MAP.get("silicon"), 1).addFluidResult(MOLTEN_MAP.get("silicon"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PART_MAP.get("sintered_steel"), 1).addFluidResult(MOLTEN_MAP.get("steel"), INGOT_VOLUME).save(recipeOutput);

        createSet(recipeOutput, "silicon_carbide", HOT_GAS_MAP, "sic_vapor");
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PART_MAP.get("silicon_carbide_fiber"), 1).addFluidResult(HOT_GAS_MAP.get("sic_vapor"), INGOT_VOLUME).save(recipeOutput, "sic_vapor_brick_from_silicon_carbide_fiber");

        for (String isotope : List.of("10", "11")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(BORON_MAP.get(isotope), 1).addFluidResult(MOLTEN_MAP.get("boron_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("6", "7")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(LITHIUM_MAP.get(isotope), 1).addFluidResult(MOLTEN_MAP.get("lithium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }

        for (String alloy : List.of("steel", "ferroboron", "tough", "hard_carbon", "hastelloy", "lead_platinum")) {
            createSet(recipeOutput, alloy, MOLTEN_MAP);
        }

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PART_MAP.get("polydimethylsilylene"), 1).addFluidResult(MOLTEN_MAP.get("polydimethylsilylene"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PART_MAP.get("polymethylsilylene_methylene"), 1).addFluidResult(MOLTEN_MAP.get("polymethylsilylene_methylene"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(Tags.Items.GEMS_QUARTZ, 1).addFluidResult(MOLTEN_MAP.get("quartz"), GEM_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 4, 1).addItemInput(Items.QUARTZ_BLOCK, 1).addFluidResult(MOLTEN_MAP.get("quartz"), GEM_VOLUME * 4).save(recipeOutput, "molten_quartz_from_block");

        createGemSet(recipeOutput, "lapis");
        createGemSet(recipeOutput, "diamond");
        createGemSet(recipeOutput, "emerald");

        new ProcessorRecipeBuilder(MelterRecipe.class, 2, 2).addItemInput(Items.OBSIDIAN, 1).addFluidResult(MOLTEN_MAP.get("obsidian"), INGOT_VOLUME * 2).save(recipeOutput, "molten_obsidian_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 2).addItemInput(GEM_DUST_MAP.get("obsidian"), 1).addFluidResult(MOLTEN_MAP.get("obsidian"), INGOT_VOLUME / 2).save(recipeOutput, "molten_obsidian_from_dust");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 1.5).addItemInput(Items.NETHERRACK, 1).addFluidResult(MOLTEN_MAP.get("nether_brick"), INGOT_VOLUME / 2).save(recipeOutput, "molten_nether_brick_from_netherrack");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 1.5).addItemInput(Items.NETHER_BRICK, 1).addFluidResult(MOLTEN_MAP.get("nether_brick"), INGOT_VOLUME / 2).save(recipeOutput, "molten_nether_brick_from_brick");
        new ProcessorRecipeBuilder(MelterRecipe.class, 2, 1.5).addItemInput(Items.NETHER_BRICKS, 1).addFluidResult(MOLTEN_MAP.get("nether_brick"), INGOT_VOLUME * 2).save(recipeOutput, "molten_nether_brick_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 2, 2).addItemInput(Ingredient.of(Items.END_STONE, Items.END_STONE_BRICKS), 1).addFluidResult(MOLTEN_MAP.get("end_stone"), INGOT_VOLUME * 2).save(recipeOutput, "molten_end_stone_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 2).addItemInput(GEM_DUST_MAP.get("end_stone"), 1).addFluidResult(MOLTEN_MAP.get("end_stone"), INGOT_VOLUME / 2).save(recipeOutput, "molten_end_stone_from_dust");
        new ProcessorRecipeBuilder(MelterRecipe.class, 2, 1.5).addItemInput(Items.PURPUR_BLOCK, 1).addFluidResult(MOLTEN_MAP.get("purpur"), INGOT_VOLUME * 2).save(recipeOutput, "molten_purpur_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 1.5).addItemInput(Items.POPPED_CHORUS_FRUIT, 1).addFluidResult(MOLTEN_MAP.get("purpur"), INGOT_VOLUME / 2).save(recipeOutput, "molten_purpur_from_fruit");

        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 0.5).addItemInput(Items.ICE, 1).addFluidResult(Fluids.WATER, BUCKET_VOLUME).save(recipeOutput, "water_from_ice");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 0.5).addItemInput(Items.PACKED_ICE, 1).addFluidResult(Fluids.WATER, BUCKET_VOLUME).save(recipeOutput, "water_from_packed_ice");

        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(COMPOUND_MAP.get("alugentum"), 1).addFluidResult(MOLTEN_MAP.get("alugentum"), INGOT_VOLUME).save(recipeOutput);
//   TODO     new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("barium_oxide"), 1).addFluidResult(MOLTEN_MAP.get("barium_oxide"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("nickel_oxide"), 1).addFluidResult(MOLTEN_MAP.get("nickel_oxide"), INGOT_VOLUME).save(recipeOutput);

        for (String name : INGOT_MAP.keySet()) {
            if (MOLTEN_MAP.containsKey(name)) {
                createSet(recipeOutput, name, MOLTEN_MAP);
            }
        }

        createSet(recipeOutput, "iron", MOLTEN_MAP);
        createSet(recipeOutput, "copper", MOLTEN_MAP);
        createSet(recipeOutput, "gold", MOLTEN_MAP);

        for (String gem : GEM_MAP.keySet()) {
            createGemSet(recipeOutput, gem);
        }
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PART_MAP.get("polyethersulfone"), 1).addFluidResult(MOLTEN_MAP.get("polyethersulfone"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PART_MAP.get("polytetrafluoroethene"), 1).addFluidResult(MOLTEN_MAP.get("polytetrafluoroethene"), INGOT_VOLUME).save(recipeOutput);

        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 1).addItemInput(Items.REDSTONE, 1).addFluidResult(MOLTEN_MAP.get("redstone"), REDSTONE_DUST_VOLUME).save(recipeOutput, "molten_redstone_from_dust");
        new ProcessorRecipeBuilder(MelterRecipe.class, 2, 1).addItemInput(Items.REDSTONE_BLOCK, 1).addFluidResult(MOLTEN_MAP.get("redstone"), REDSTONE_BLOCK_VOLUME).save(recipeOutput, "molten_redstone_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 1).addItemInput(Items.GLOWSTONE_DUST, 1).addFluidResult(MOLTEN_MAP.get("glowstone"), GLOWSTONE_DUST_VOLUME).save(recipeOutput, "molten_glowstone_from_dust");
        new ProcessorRecipeBuilder(MelterRecipe.class, 2, 1).addItemInput(Items.GLOWSTONE, 1).addFluidResult(MOLTEN_MAP.get("glowstone"), GLOWSTONE_BLOCK_VOLUME).save(recipeOutput, "molten_glowstone_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 1.5).addItemInput(Items.ENDER_PEARL, 1).addFluidResult(CUSTOM_FLUID_MAP.get("ender"), ENDER_PEARL_VOLUME).save(recipeOutput, "molten_ender_from_pearl");

        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 1).addItemInput(Ingredient.of(Items.COAL, GEM_DUST_MAP.get("coal")), 1).addFluidResult(MOLTEN_MAP.get("coal"), COAL_DUST_VOLUME).save(recipeOutput, "molten_coal_from_coal");
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 1).addItemInput(INGOT_MAP.get("graphite"), 1).addFluidResult(MOLTEN_MAP.get("coal"), COAL_DUST_VOLUME).save(qmdNotLoaded(recipeOutput), "molten_coal_from_graphite");
        new ProcessorRecipeBuilder(MelterRecipe.class, 4.5, 1).addItemInput(Items.COAL_BLOCK, 1).addFluidResult(MOLTEN_MAP.get("coal"), COAL_BLOCK_VOLUME).save(recipeOutput, "molten_coal_from_block");
        new ProcessorRecipeBuilder(MelterRecipe.class, 4.5, 1).addItemInput(INGOT_BLOCK_MAP.get("graphite"), 1).addFluidResult(MOLTEN_MAP.get("coal"), COAL_BLOCK_VOLUME).save(qmdNotLoaded(recipeOutput), "molten_coal_from_graphite_block");

        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 0.5).addItemInput(FOOD_MAP.get("ground_cocoa_nibs"), 1).addFluidResult(CHOCOLATE_MAP.get("chocolate_liquor"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 0.5).addItemInput(FOOD_MAP.get("cocoa_butter"), 1).addFluidResult(CHOCOLATE_MAP.get("cocoa_butter"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 0.5).addItemInput(FOOD_MAP.get("unsweetened_chocolate"), 1).addFluidResult(CHOCOLATE_MAP.get("unsweetened_chocolate"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 0.5).addItemInput(FOOD_MAP.get("dark_chocolate"), 1).addFluidResult(CHOCOLATE_MAP.get("dark_chocolate"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.25, 0.5).addItemInput(FOOD_MAP.get("milk_chocolate"), 1).addFluidResult(CHOCOLATE_MAP.get("milk_chocolate"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 0.5).addItemInput(Items.SUGAR, 1).addFluidResult(SUGAR_MAP.get("sugar"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 0.5).addItemInput(FOOD_MAP.get("gelatin"), 1).addFluidResult(SUGAR_MAP.get("gelatin"), INGOT_VOLUME).save(recipeOutput);
        new ProcessorRecipeBuilder(MelterRecipe.class, 0.5, 0.5).addItemInput(FOOD_MAP.get("marshmallow"), 1).addFluidResult(SUGAR_MAP.get("marshmallow"), INGOT_VOLUME).save(recipeOutput);

        for (String isotope : List.of("241", "242", "243")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(AMERICIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("americium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("247", "248")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(BERKELIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("berkelium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("249", "250", "251", "252")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(CALIFORNIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("californium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("243", "245", "246", "247")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(CURIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("curium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("236", "237")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(NEPTUNIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("neptunium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("238", "239", "241", "242")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(PLUTONIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("plutonium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }
        for (String isotope : List.of("233", "235", "238")) {
            new ProcessorRecipeBuilder(MelterRecipe.class, 1, 1).addItemInput(URANIUM_MAP.get(isotope), 1).addFluidResult(FISSION_FUEL_MAP.get("uranium_" + isotope), INGOT_VOLUME).save(recipeOutput);
        }

        for (String dust : FISSION_DUST_MAP.keySet()) {
            if (!FISSION_FLUID_MAP.containsKey(dust)) continue;
            createSet(recipeOutput, dust, FISSION_FLUID_MAP);
        }

        // TODO add other mods recipes
    }
}