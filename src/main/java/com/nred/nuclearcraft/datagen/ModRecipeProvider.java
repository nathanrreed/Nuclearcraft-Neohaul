package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.recipe.CollectorRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.GASSES;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;
import static net.minecraft.data.recipes.RecipeCategory.BUILDING_BLOCKS;
import static net.minecraft.data.recipes.RecipeCategory.MISC;

class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    private <T extends AbstractCookingRecipe> void smelting(RecipeOutput recipeOutput, RecipeSerializer<T> pSmeltingSerializer, AbstractCookingRecipe.Factory<T> factory, List<Ingredient> ingredients, RecipeCategory pCategory, ItemLike pResult, float pExp, int pSmeltTime, String pGroup, String pRecipeName) {
        for (Ingredient ingredient : ingredients) {
            SimpleCookingRecipeBuilder.generic(
                            ingredient, pCategory, pResult, pExp, pSmeltTime, pSmeltingSerializer, factory
                    ).group(pGroup)
                    .unlockedBy(getHasName(ingredient.getItems()[0].getItem()), has(ingredient.getItems()[0].getItem()))
                    .save(
                            recipeOutput,
                            MODID + ":" + getItemName(pResult) + "_" + pRecipeName + "_" + getItemName(ingredient.getItems()[0].getItem())
                    );
        }
    }

    private void oreSmelting(RecipeOutput recipeOutput, List<Ingredient> ingredients, RecipeCategory pCategory, ItemLike pResult, float pExp, int pSmeltTime, String pGroup, boolean blast) {
        smelting(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, pCategory, pResult, pExp, pSmeltTime, pGroup, "from_smelting");
        if (blast) {
            smelting(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, pCategory, pResult, pExp, pSmeltTime / 2, pGroup, "from_blasting");
        }
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        for (String ore : ORES) {
            oreSmelting(recipeOutput, List.of(Ingredient.of(RAW_MAP.get(ore)), Ingredient.of(DUST_MAP.get(ore)), Ingredient.of(ORE_MAP.get(ore), ORE_MAP.get(ore + "_deepslate"))), MISC, INGOT_MAP.get(ore), 0.25f, 200, ore, true);
        }

        full9Block(recipeOutput, INGOTS, INGOT_MAP, INGOT_BLOCK_MAP);
        full9Block(recipeOutput, RAWS, RAW_MAP, RAW_BLOCK_MAP);

        full9Item(recipeOutput, NUGGETS, NUGGET_MAP, INGOT_MAP);

        parts(recipeOutput);
    }

    private void full9Block(RecipeOutput recipeOutput, List<String> list, HashMap<String, DeferredItem<Item>> itemMap, HashMap<String, DeferredBlock<Block>> resultMap) {
        for (String name : list) {
            ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, resultMap.get(name), 1).requires(itemMap.get(name), 9).unlockedBy(getHasName(itemMap.get(name)), has(itemMap.get(name))).save(recipeOutput, getId(resultMap.get(name).asItem(), itemMap.get(name).asItem()));
            ShapelessRecipeBuilder.shapeless(BUILDING_BLOCKS, itemMap.get(name), 9).requires(resultMap.get(name), 1).unlockedBy(getHasName(resultMap.get(name)), has(resultMap.get(name))).save(recipeOutput, getId(itemMap.get(name).asItem(), resultMap.get(name).asItem()));
        }
    }

    private void full9Item(RecipeOutput recipeOutput, List<String> list, HashMap<String, DeferredItem<Item>> itemMap, HashMap<String, DeferredItem<Item>> resultMap) {
        for (String name : list) {
            ShapelessRecipeBuilder.shapeless(MISC, resultMap.get(name), 1).requires(itemMap.get(name), 9).unlockedBy(getHasName(itemMap.get(name)), has(itemMap.get(name))).save(recipeOutput, getId(resultMap.get(name).asItem(), itemMap.get(name).asItem()));
            ShapelessRecipeBuilder.shapeless(MISC, itemMap.get(name), 9).requires(resultMap.get(name), 1).unlockedBy(getHasName(resultMap.get(name)), has(resultMap.get(name))).save(recipeOutput, getId(itemMap.get(name).asItem(), resultMap.get(name).asItem()));
        }
    }

    private String getId(Item result, Item input) {
        return MODID + ":" + ResourceLocation.parse(result.toString()).getPath() + "_from_" + ResourceLocation.parse(input.toString()).getPath();
    }

    private void parts(RecipeOutput recipeOutput) {
        ItemLike result = PART_MAP.get("basic_plating");
        TagKey<Item> tag = tag(Tags.Items.INGOTS, "lead");

        // Parts
        ShapedRecipeBuilder.shaped(MISC, result, 2).pattern("LG").pattern("GL")
                .define('L', tag(Tags.Items.INGOTS, "lead")).define('G', tag(Tags.Items.DUSTS, "graphite"))
                .unlockedBy(getHasName(result), has(tag)).save(recipeOutput, MODID + ":basic_plating_1");
        ShapedRecipeBuilder.shaped(MISC, result, 2).pattern("GL").pattern("LG")
                .define('L', tag).define('G', tag(Tags.Items.DUSTS, "graphite"))
                .unlockedBy(getHasName(Ingredient.of(tag).getItems()[0].getItem()), has(tag)).save(recipeOutput, MODID + ":basic_plating_2");

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("advanced_plating")).pattern(" R ").pattern("TPT").pattern(" R ")
                .define('R', Items.REDSTONE).define('P', PART_MAP.get("basic_plating")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("du_plating")).pattern("SUS").pattern("UPU").pattern("SUS")
                .define('S', GEM_DUST_MAP.get("sulfur")).define('P', PART_MAP.get("advanced_plating")).define('U', URANIUM_MAP.get("238"))
                .unlockedBy(getHasName(PART_MAP.get("advanced_plating")), has(PART_MAP.get("advanced_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("elite_plating")).pattern("CBC").pattern("BPB").pattern("CBC")
                .define('C', COMPOUND_MAP.get("crystal_binder")).define('P', PART_MAP.get("du_plating")).define('B', INGOT_MAP.get("boron"))
                .unlockedBy(getHasName(PART_MAP.get("du_plating")), has(PART_MAP.get("du_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("copper_solenoid"), 2).pattern("CC").pattern("II").pattern("CC")
                .define('C', tag(Tags.Items.INGOTS, "copper")).define('I', tag(Tags.Items.INGOTS, "iron"))
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("magnesium_diboride_solenoid"), 2).pattern("MM").pattern("MM").pattern("MM")
                .define('M', ALLOY_MAP.get("magnesium_diboride"))
                .unlockedBy(getHasName(ALLOY_MAP.get("magnesium_diboride")), has(ALLOY_MAP.get("magnesium_diboride"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("servomechanism"), 1).pattern("F F").pattern("RSR").pattern("SCS")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('C', tag(Tags.Items.INGOTS, "copper")).define('R', Items.REDSTONE).define('F', ALLOY_MAP.get("ferroboron"))
                .unlockedBy(getHasName(ALLOY_MAP.get("ferroboron")), has(ALLOY_MAP.get("ferroboron"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("electric_motor"), 1).pattern("SSG").pattern("CCI").pattern("SSG")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('I', tag(Tags.Items.INGOTS, "iron")).define('G', Items.GOLD_NUGGET).define('C', PART_MAP.get("copper_solenoid"))
                .unlockedBy(getHasName(PART_MAP.get("copper_solenoid")), has(PART_MAP.get("copper_solenoid"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_MAP.get("liner_actuator"), 1).pattern("  S").pattern("FP ").pattern("CF ")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('P', Items.PISTON).define('C', tag(Tags.Items.INGOTS, "copper")).define('F', ALLOY_MAP.get("ferroboron"))
                .unlockedBy(getHasName(ALLOY_MAP.get("ferroboron")), has(ALLOY_MAP.get("ferroboron"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("machine_chassis"), 1).pattern("LSL").pattern("STS").pattern("LSL")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('L', tag(Tags.Items.INGOTS, "lead")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(ALLOY_MAP.get("tough")), has(ALLOY_MAP.get("tough"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("empty_frame"), 1).pattern("PTP").pattern("I I").pattern("PTP")
                .define('T', tag(Tags.Items.INGOTS, "tin")).define('I', tag(Tags.Items.INGOTS, "iron")).define('P', PART_MAP.get("basic_plating"))
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("steel_chassis"), 1).pattern("STS").pattern("TCT").pattern("STS")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('C', tag(Tags.Items.INGOTS, "copper")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(ALLOY_MAP.get("tough")), has(ALLOY_MAP.get("tough"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, PART_BLOCK_MAP.get("empty_heat_sink"), 8).pattern("PSP").pattern("T T").pattern("PSP")
                .define('S', tag(Tags.Items.INGOTS, "steel")).define('P', PART_MAP.get("advanced_plating")).define('T', ALLOY_MAP.get("tough"))
                .unlockedBy(getHasName(PART_MAP.get("advanced_plating")), has(PART_MAP.get("advanced_plating"))).save(recipeOutput);

        // Upgrades
        ShapedRecipeBuilder.shaped(MISC, UPGRADE_MAP.get("speed"), 1).pattern("LRL").pattern("RWR").pattern("LRL")
                .define('L', Items.LAPIS_LAZULI).define('R', Items.REDSTONE).define('W', Items.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, UPGRADE_MAP.get("energy"), 1).pattern("OQO").pattern("QWQ").pattern("OQO")
                .define('Q', tag(Tags.Items.DUSTS, "quartz")).define('O', tag(Tags.Items.DUSTS, "obsidian")).define('W', Items.LIGHT_WEIGHTED_PRESSURE_PLATE)
                .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI)).save(recipeOutput);


        // Compounds
        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("crystal_binder"), 2)
                .requires(ingredient(Tags.Items.DUSTS, "rhodochrosite"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "obsidian"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "magnesium"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "calcium_sulfate"), 1)
                .unlockedBy(getHasName(GEM_DUST_MAP.get("rhodochrosite")), has(GEM_DUST_MAP.get("rhodochrosite"))).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("energetic_blend"), 2)
                .requires(Items.GLOWSTONE_DUST, 1)
                .requires(Items.REDSTONE, 1)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("dimensional_blend"), 2)
                .requires(ingredient(Tags.Items.DUSTS, "end_stone"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "obsidian"), 4)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(MISC, COMPOUND_MAP.get("c_mn_blend"), 2)
                .requires(ingredient(Tags.Items.DUSTS, "graphite"), 1)
                .requires(ingredient(Tags.Items.DUSTS, "manganese"), 1)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE)).save(recipeOutput);

        // Simple Machines
        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator"), 1).pattern("PTP").pattern("W L").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', tag(Tags.Items.INGOTS, "tin")).define('W', Items.WATER_BUCKET).define('L', Items.LAVA_BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput, MODID + ":cobblestone_generator_1");
        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator"), 1).pattern("PTP").pattern("L W").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', tag(Tags.Items.INGOTS, "tin")).define('W', Items.WATER_BUCKET).define('L', Items.LAVA_BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput, MODID + ":cobblestone_generator_2");

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator_compact"), 1).pattern("CCC").pattern("CBC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("cobblestone_generator")).define('B', tag(Tags.Items.INGOTS, "bronze"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("cobblestone_generator")), has(COLLECTOR_MAP.get("cobblestone_generator"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("cobblestone_generator_dense"), 1).pattern("CCC").pattern("CGC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("cobblestone_generator_compact")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("cobblestone_generator_compact")), has(COLLECTOR_MAP.get("cobblestone_generator_compact"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("water_source"), 1).pattern("PTP").pattern("W W").pattern("PTP")
                .define('P', PART_MAP.get("basic_plating")).define('T', tag(Tags.Items.INGOTS, "tin")).define('W', Items.WATER_BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("basic_plating")), has(PART_MAP.get("basic_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("water_source_compact"), 1).pattern("CCC").pattern("CBC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("water_source")).define('B', tag(Tags.Items.INGOTS, "bronze"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("water_source")), has(COLLECTOR_MAP.get("water_source"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("water_source_dense"), 1).pattern("CCC").pattern("CGC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("water_source_compact")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("water_source_compact")), has(COLLECTOR_MAP.get("water_source_compact"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("nitrogen_collector"), 1).pattern("PBP").pattern("E E").pattern("PBP")
                .define('P', PART_MAP.get("advanced_plating")).define('B', tag(Tags.Items.INGOTS, "beryllium")).define('E', Items.BUCKET)
                .unlockedBy(getHasName(PART_MAP.get("advanced_plating")), has(PART_MAP.get("advanced_plating"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("nitrogen_collector_compact"), 1).pattern("CCC").pattern("CBC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("nitrogen_collector")).define('B', tag(Tags.Items.INGOTS, "bronze"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("nitrogen_collector")), has(COLLECTOR_MAP.get("nitrogen_collector"))).save(recipeOutput);

        ShapedRecipeBuilder.shaped(MISC, COLLECTOR_MAP.get("nitrogen_collector_dense"), 1).pattern("CCC").pattern("CGC").pattern("CCC")
                .define('C', COLLECTOR_MAP.get("nitrogen_collector_compact")).define('G', tag(Tags.Items.INGOTS, "gold"))
                .unlockedBy(getHasName(COLLECTOR_MAP.get("nitrogen_collector_compact")), has(COLLECTOR_MAP.get("nitrogen_collector_compact"))).save(recipeOutput);

        new CollectorRecipeBuilder(new ItemStack(Items.COBBLESTONE), MACHINE_LEVEL.BASE, 0.125).save(recipeOutput, MODID + ":cobblestone_generator_rate");
        new CollectorRecipeBuilder(new ItemStack(Items.COBBLESTONE), MACHINE_LEVEL.COMPACT, 1.0).save(recipeOutput, MODID + ":cobblestone_generator_compact_rate");
        new CollectorRecipeBuilder(new ItemStack(Items.COBBLESTONE), MACHINE_LEVEL.DENSE, 8.0).save(recipeOutput, MODID + ":cobblestone_generator_dense_rate");

        new CollectorRecipeBuilder(new FluidStack(Fluids.WATER, 10), MACHINE_LEVEL.BASE).save(recipeOutput, MODID + ":water_source_rate");
        new CollectorRecipeBuilder(new FluidStack(Fluids.WATER, 80), MACHINE_LEVEL.COMPACT).save(recipeOutput, MODID + ":water_source_compact_rate");
        new CollectorRecipeBuilder(new FluidStack(Fluids.WATER, 640), MACHINE_LEVEL.DENSE).save(recipeOutput, MODID + ":water_source_dense_rate");

        new CollectorRecipeBuilder(new FluidStack(GASSES.get("nitrogen").still, 5), MACHINE_LEVEL.BASE).save(recipeOutput, MODID + ":nitrogen_collector_rate");
        new CollectorRecipeBuilder(new FluidStack(GASSES.get("nitrogen").still, 40), MACHINE_LEVEL.COMPACT).save(recipeOutput, MODID + ":nitrogen_collector_compact_rate");
        new CollectorRecipeBuilder(new FluidStack(GASSES.get("nitrogen").still, 320), MACHINE_LEVEL.DENSE).save(recipeOutput, MODID + ":nitrogen_collector_dense_rate");
    }

    private TagKey<Item> tag(TagKey<Item> tag, String name) {
        return ItemTags.create(tag.location().withSuffix("/" + name));
    }

    private Ingredient ingredient(TagKey<Item> tag, String name) {
        return Ingredient.of(ItemTags.create(tag.location().withSuffix("/" + name)));
    }
}