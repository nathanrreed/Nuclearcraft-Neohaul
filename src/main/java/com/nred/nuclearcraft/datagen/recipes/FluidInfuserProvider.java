package com.nred.nuclearcraft.datagen.recipes;

import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidInfuserRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

import static com.nred.nuclearcraft.helpers.RecipeHelpers.tags;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.registration.FluidRegistration.GAS_MAP;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

public class FluidInfuserProvider {
    public FluidInfuserProvider(RecipeOutput recipeOutput) {
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("manganese"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(INGOT_MAP.get("manganese_oxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(DUST_MAP.get("manganese"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(DUST_MAP.get("manganese_oxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("manganese_oxide"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(INGOT_MAP.get("manganese_dioxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(DUST_MAP.get("manganese_oxide"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(DUST_MAP.get("manganese_dioxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("zirconium"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(INGOT_MAP.get("zirconia"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(DUST_MAP.get("zirconium"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(DUST_MAP.get("zirconia"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(INGOT_MAP.get("tin"), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(INGOT_MAP.get("tin_oxide"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.25, 0.5).addItemInput(Ingredient.of(Items.ICE, Items.PACKED_ICE), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), 50).addItemResult(SUPERCOLD_ICE, 1).save(recipeOutput);

        // Fission Coolant ports

        // Heat Sinks

        // RadAway

        // Heavy Water Moderator

        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(Fluids.WATER, 2000).addItemResult(COLLECTOR_MAP.get("water_source"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(COLLECTOR_MAP.get("water_source"), 1).addFluidInput(Fluids.LAVA, 1000).addItemResult(COLLECTOR_MAP.get("cobblestone_generator"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(Tags.Items.GLASS_BLOCKS, 1).addFluidInput(GAS_MAP.get("tritium"), 1000).addItemResult(TRITIUM_LAMP, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(tags(List.of(Tags.Items.SANDS, Tags.Items.SANDSTONE_BLOCKS), 1)).addFluidInput(CUSTOM_FLUID_MAP.get("ender"), 250).addItemResult(Items.END_STONE, 1).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.WHITE_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.WHITE_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.ORANGE_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.ORANGE_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.MAGENTA_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.MAGENTA_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.LIGHT_BLUE_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.LIGHT_BLUE_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.YELLOW_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.YELLOW_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.LIME_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.LIME_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.PINK_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.PINK_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.GRAY_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.GRAY_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.LIGHT_GRAY_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.LIGHT_GRAY_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.CYAN_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.CYAN_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.PURPLE_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.PURPLE_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.BLUE_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.BLUE_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.BROWN_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.BROWN_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.GREEN_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.GREEN_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.RED_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.RED_CONCRETE, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 0.5, 0.5).addItemInput(Items.BLACK_CONCRETE_POWDER, 1).addFluidInput(Fluids.WATER, 1000).addItemResult(Items.BLACK_CONCRETE, 1).save(recipeOutput);

        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(Ingredient.of(Items.DIRT, Items.GRASS_BLOCK), 1).addFluidInput(Fluids.WATER, 2000).addItemResult(Items.CLAY, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(Items.BRICK, 1).addFluidInput(Fluids.WATER, 2000).addItemResult(Items.CLAY_BALL, 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 2, 1).addItemInput(Items.TERRACOTTA, 1).addFluidInput(Fluids.WATER, 4000).addItemResult(Items.CLAY, 1).save(recipeOutput, "clay_from_terracotta");
    }
}