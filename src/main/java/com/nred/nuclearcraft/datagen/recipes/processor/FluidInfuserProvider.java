package com.nred.nuclearcraft.datagen.recipes.processor;

import com.nred.nuclearcraft.recipe.ProcessorRecipeBuilder;
import com.nred.nuclearcraft.recipe.processor.FluidInfuserRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.HashMap;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.tags;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
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

        // Fission Heater Port
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), 1000).addItemResult(FISSION_REACTOR_MAP.get("liquid_nitrogen_fission_coolant_heater_port"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), 1000).addItemResult(FISSION_REACTOR_MAP.get("liquid_helium_fission_coolant_heater_port"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(MOLTEN_MAP.get("enderium"), 1000).addItemResult(FISSION_REACTOR_MAP.get("enderium_fission_coolant_heater_port"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("cryotheum"), 1000).addItemResult(FISSION_REACTOR_MAP.get("cryotheum_fission_coolant_heater_port"), 1).save(recipeOutput);

        // Heat Sinks
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(Fluids.WATER, 1000).addItemResult(FISSION_REACTOR_MAP.get("water_fission_heat_sink"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), 1000).addItemResult(FISSION_REACTOR_MAP.get("liquid_nitrogen_fission_heat_sink"), 1).save(recipeOutput, MODID + ":sink_from_liquid_nitrogen");
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), 1000).addItemResult(FISSION_REACTOR_MAP.get("liquid_helium_fission_heat_sink"), 1).save(recipeOutput, MODID + ":sink_from_liquid_helium");
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(MOLTEN_MAP.get("enderium"), 1000).addItemResult(FISSION_REACTOR_MAP.get("enderium_fission_heat_sink"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("cryotheum"), 1000).addItemResult(FISSION_REACTOR_MAP.get("cryotheum_fission_heat_sink"), 1).save(recipeOutput);

        // Heater
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_nitrogen"), 1000).addItemResult(FISSION_REACTOR_MAP.get("liquid_nitrogen_fission_coolant_heater"), 1).save(recipeOutput, MODID + ":heater_from_liquid_nitrogen");
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("liquid_helium"), 1000).addItemResult(FISSION_REACTOR_MAP.get("liquid_helium_fission_coolant_heater"), 1).save(recipeOutput, MODID + ":heater_from_helium_helium");
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(MOLTEN_MAP.get("enderium"), 1000).addItemResult(FISSION_REACTOR_MAP.get("enderium_fission_coolant_heater"), 1).save(recipeOutput);
        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(FISSION_REACTOR_MAP.get("standard_fission_coolant_heater"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("cryotheum"), 1000).addItemResult(FISSION_REACTOR_MAP.get("cryotheum_fission_coolant_heater"), 1).save(recipeOutput);

        // RadAway TODO

        new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(PART_BLOCK_MAP.get("empty_frame"), 1).addFluidInput(CUSTOM_FLUID_MAP.get("heavy_water"), 1000).addItemResult(HEAVY_WATER_MODERATOR, 1).save(recipeOutput);

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

        nitrogenOxygenPair(recipeOutput, List.of("233", "235", "238"), URANIUM_MAP, "uranium_");
        nitrogenOxygenPair(recipeOutput, List.of("236", "237"), NEPTUNIUM_MAP, "neptunium_");
        nitrogenOxygenPair(recipeOutput, List.of("238", "239", "241", "242"), PLUTONIUM_MAP, "plutonium_");
        nitrogenOxygenPair(recipeOutput, List.of("241", "242", "243"), AMERICIUM_MAP, "americium_");
        nitrogenOxygenPair(recipeOutput, List.of("243", "245", "246", "247"), CURIUM_MAP, "curium_");
        nitrogenOxygenPair(recipeOutput, List.of("247", "248"), BERKELIUM_MAP, "berkelium_");
        nitrogenOxygenPair(recipeOutput, List.of("249", "250", "251", "252"), CALIFORNIUM_MAP, "californium_");
    }

    private void nitrogenOxygenPair(RecipeOutput recipeOutput, List<String> types, HashMap<String, DeferredItem<Item>> map, String name) {
        for (String type : types) {
            new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(map.get(type), 1).addFluidInput(GAS_MAP.get("nitrogen"), 1000).addItemResult(map.get(type + "_ni"), 1).save(recipeOutput, name + type + "_from_ni");
            new ProcessorRecipeBuilder(FluidInfuserRecipe.class, 1, 1).addItemInput(map.get(type), 1).addFluidInput(GAS_MAP.get("oxygen"), 1000).addItemResult(map.get(type + "_ox"), 1).save(recipeOutput, name + type + "_from_ox");
        }
    }
}