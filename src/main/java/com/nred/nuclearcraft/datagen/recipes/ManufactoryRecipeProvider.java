package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.ManufactoryRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static com.nred.nuclearcraft.info.Names.ORES;
import static com.nred.nuclearcraft.registration.BlockRegistration.MATERIAL_BLOCK_MAP;
import static com.nred.nuclearcraft.registration.BlockRegistration.ORE_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class ManufactoryRecipeProvider {
    public ManufactoryRecipeProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 1).addItemInput(Items.COAL, 1).addItemResult(GEM_DUST_MAP.get("coal"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.25, 0.5).addItemInput(GEM_DUST_MAP.get("coal"), 1).addItemResult(DUST_MAP.get("graphite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addItemInput(Items.DIAMOND, 1).addItemResult(GEM_DUST_MAP.get("diamond"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addItemInput(GEM_MAP.get("rhodochrosite"), 1).addItemResult(GEM_DUST_MAP.get("rhodochrosite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.QUARTZ, 1).addItemResult(GEM_DUST_MAP.get("quartz"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.PRISMARINE, 1).addItemResult(Items.PRISMARINE_CRYSTALS, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addItemInput(GEM_MAP.get("boron_nitride"), 1).addItemResult(GEM_DUST_MAP.get("boron_nitride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addItemInput(GEM_MAP.get("fluorite"), 1).addItemResult(GEM_DUST_MAP.get("fluorite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addItemInput(GEM_MAP.get("villiaumite"), 1).addItemResult(GEM_DUST_MAP.get("villiaumite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addItemInput(GEM_MAP.get("carobbiite"), 1).addItemResult(GEM_DUST_MAP.get("carobbiite"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("villiaumite"), 1).addItemResult(COMPOUND_MAP.get("sodium_fluoride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(GEM_DUST_MAP.get("carobbiite"), 1).addItemResult(COMPOUND_MAP.get("potassium_fluoride"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(ItemTags.SAND, 1).addItemResult(GEM_MAP.get("silicon"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(PART_MAP.get("sintered_zirconia"), 1).addItemResult(DUST_MAP.get("zirconia"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 9, 1).addItemInput(MATERIAL_BLOCK_MAP.get("molybdenum"), 1).addItemResult(FISSION_DUST_MAP.get("molybdenum"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 2, 1).addItemInput(Items.OBSIDIAN, 1).addItemResult(GEM_DUST_MAP.get("obsidian"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.COBBLESTONE, 1).addItemResult(Items.SAND, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.GRAVEL, 1).addItemResult(Items.FLINT, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.END_STONE, 1).addItemResult(GEM_DUST_MAP.get("end_stone"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.BLAZE_ROD, 1).addItemResult(Items.BLAZE_POWDER, 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 1).addItemInput(Items.ROTTEN_FLESH, 4).addItemResult(Items.LEATHER, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(Items.SUGAR_CANE, 2).addItemResult(PART_MAP.get("bioplastic"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.25, 0.5).addItemInput(Items.WHEAT, 1).addItemResult(FOOD_MAP.get("flour"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 1).addItemInput(Items.BONE, 1).addItemResult(Items.BONE_MEAL, 6).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(FOOD_MAP.get("roasted_cocoa_beans"), 1).addItemResult(FOOD_MAP.get("ground_cocoa_nibs"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(Items.PORKCHOP, 1).addItemResult(FOOD_MAP.get("gelatin"), 8).save(recipeOutput, "gelatin_from_pork");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.FISHES, 1).addItemResult(FOOD_MAP.get("gelatin"), 4).save(recipeOutput, "gelatin_from_fish");

        for (String ore : ORES) {
            new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1.25, 1).addItemInput(ORE_MAP.get(ore), 1).addItemResult(DUST_MAP.get(ore), 2).save(recipeOutput);
        }

        for (String ingot : INGOT_MAP.keySet()) {
            new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(INGOT_MAP.get(ingot), 1).addItemResult(DUST_MAP.get(ingot), 1).save(recipeOutput, ingot + "_dust_from_ingot");
        }

        for (String raw : RAW_MAP.keySet()) {
            new ProcessorRecipeBuilder(ManufactoryRecipe.class, 1, 1).addItemInput(RAW_MAP.get(raw), 1).addItemResult(DUST_MAP.get(raw), 1).save(recipeOutput, raw + "_dust_from_raw");
        }

        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.25, 0.5).addItemInput(ItemTags.PLANKS, 1).addItemResult(Items.STICK, 4).save(recipeOutput);
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.ACACIA_LOGS, 1).addItemResult(Blocks.ACACIA_PLANKS, 6).save(recipeOutput, "acacia_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.BIRCH_LOGS, 1).addItemResult(Blocks.BIRCH_PLANKS, 6).save(recipeOutput, "birch_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.CRIMSON_STEMS, 1).addItemResult(Blocks.CRIMSON_PLANKS, 6).save(recipeOutput, "crimson_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.DARK_OAK_LOGS, 1).addItemResult(Blocks.DARK_OAK_PLANKS, 6).save(recipeOutput, "dark_oak_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.JUNGLE_LOGS, 1).addItemResult(Blocks.JUNGLE_PLANKS, 6).save(recipeOutput, "jungle_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.OAK_LOGS, 1).addItemResult(Blocks.OAK_PLANKS, 6).save(recipeOutput, "oak_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.SPRUCE_LOGS, 1).addItemResult(Blocks.SPRUCE_PLANKS, 6).save(recipeOutput, "spruce_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.WARPED_STEMS, 1).addItemResult(Blocks.WARPED_PLANKS, 6).save(recipeOutput, "warped_planks_from_logs");
        new ProcessorRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addItemInput(ItemTags.MANGROVE_LOGS, 1).addItemResult(Blocks.MANGROVE_PLANKS, 6).save(recipeOutput, "mangrove_planks_from_logs");
    }
}