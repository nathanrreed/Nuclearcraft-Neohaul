package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ItemToItemRecipeBuilder;
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
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 1).addInput(Items.COAL, 1).addResult(GEM_DUST_MAP.get("coal"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.25, 0.5).addInput(GEM_DUST_MAP.get("coal"), 1).addResult(DUST_MAP.get("graphite"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addInput(Items.DIAMOND, 1).addResult(GEM_DUST_MAP.get("diamond"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addInput(GEM_MAP.get("rhodochrosite"), 1).addResult(GEM_DUST_MAP.get("rhodochrosite"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.QUARTZ, 1).addResult(GEM_DUST_MAP.get("quartz"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.PRISMARINE, 1).addResult(Items.PRISMARINE_CRYSTALS, 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addInput(GEM_MAP.get("boron_nitride"), 1).addResult(GEM_DUST_MAP.get("boron_nitride"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addInput(GEM_MAP.get("fluorite"), 1).addResult(GEM_DUST_MAP.get("fluorite"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addInput(GEM_MAP.get("villiaumite"), 1).addResult(GEM_DUST_MAP.get("villiaumite"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.5, 1.5).addInput(GEM_MAP.get("carobbiite"), 1).addResult(GEM_DUST_MAP.get("carobbiite"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(GEM_DUST_MAP.get("villiaumite"), 1).addResult(COMPOUND_MAP.get("sodium_fluoride"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(GEM_DUST_MAP.get("carobbiite"), 1).addResult(COMPOUND_MAP.get("potassium_fluoride"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(ItemTags.SAND, 1).addResult(GEM_MAP.get("silicon"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(PART_MAP.get("sintered_zirconia"), 1).addResult(DUST_MAP.get("zirconia"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 9, 1).addInput(MATERIAL_BLOCK_MAP.get("molybdenum"), 1).addResult(FISSION_DUST_MAP.get("molybdenum"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 2, 1).addInput(Items.OBSIDIAN, 1).addResult(GEM_DUST_MAP.get("obsidian"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.COBBLESTONE, 1).addResult(Items.SAND, 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.GRAVEL, 1).addResult(Items.FLINT, 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.END_STONE, 1).addResult(GEM_DUST_MAP.get("end_stone"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.BLAZE_ROD, 1).addResult(Items.BLAZE_POWDER, 4).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 1).addInput(Items.ROTTEN_FLESH, 4).addResult(Items.LEATHER, 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(Items.SUGAR_CANE, 2).addResult(PART_MAP.get("bioplastic"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.25, 0.5).addInput(Items.WHEAT, 1).addResult(FOOD_MAP.get("flour"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 1).addInput(Items.BONE, 1).addResult(Items.BONE_MEAL, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(FOOD_MAP.get("roasted_cocoa_beans"), 1).addResult(FOOD_MAP.get("ground_cocoa_nibs"), 1).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Items.PORKCHOP, 1).addResult(FOOD_MAP.get("gelatin"), 8).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(ItemTags.FISHES, 1).addResult(FOOD_MAP.get("gelatin"), 4).save(recipeOutput);

        for (String ore : ORES) {
            new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1.25, 1).addInput(ORE_MAP.get(ore), 1).addResult(DUST_MAP.get(ore), 2).save(recipeOutput);
        }

        for (String ingot : INGOT_MAP.keySet()) {
            new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 1, 1).addInput(INGOT_MAP.get(ingot), 1).addResult(DUST_MAP.get(ingot), 1).save(recipeOutput);
        }

        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.25, 0.5).addInput(ItemTags.PLANKS, 1).addResult(Items.STICK, 4).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.ACACIA_PLANKS, 1).addResult(ItemTags.ACACIA_LOGS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.BIRCH_PLANKS, 1).addResult(ItemTags.BIRCH_LOGS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.CRIMSON_PLANKS, 1).addResult(ItemTags.CRIMSON_STEMS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.DARK_OAK_PLANKS, 1).addResult(ItemTags.DARK_OAK_LOGS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.JUNGLE_PLANKS, 1).addResult(ItemTags.JUNGLE_LOGS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.OAK_PLANKS, 1).addResult(ItemTags.OAK_LOGS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.SPRUCE_PLANKS, 1).addResult(ItemTags.SPRUCE_LOGS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.WARPED_PLANKS, 1).addResult(ItemTags.WARPED_STEMS, 6).save(recipeOutput);
        new ItemToItemRecipeBuilder(ManufactoryRecipe.class, 0.5, 0.5).addInput(Blocks.MANGROVE_PLANKS, 1).addResult(ItemTags.MANGROVE_LOGS, 6).save(recipeOutput);
    }
}