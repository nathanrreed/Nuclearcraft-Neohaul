package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.block.processor.Processor.PROCESSOR_ON;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, existingFileHelper);
    }

    @Override
    public void registerStatesAndModels() {
        for (var ore : ORES) {
            blockWithItemOverlay(ORE_MAP.get(ore), Blocks.STONE, "ore");
            blockWithItemOverlay(ORE_MAP.get(ore + "_deepslate"), ORE_MAP.get(ore), Blocks.DEEPSLATE, "ore");
        }
        simpleBlocks(INGOTS, INGOT_BLOCK_MAP, "ingot_block");
        simpleBlocks(MATERIAL_BLOCKS, MATERIAL_BLOCK_MAP, "material_block");
        simpleBlocks(RAWS, RAW_BLOCK_MAP, "raw_block");
        simpleBlocks(FERTILE_ISOTOPES, FERTILE_ISOTOPE_MAP, "fertile_isotope");
        blockWithItem(SUPERCOLD_ICE);
        blockWithItem(SOLIDIFIED_CORIUM);
        blockWithItem(UNIVERSAL_BIN);
        blockWithItem(MACHINE_INTERFACE);
        blockWithItem(TRITIUM_LAMP);
        crossBlock(GLOWING_MUSHROOM);

        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            for (String machine : List.of("cobblestone_generator", "water_source", "nitrogen_collector")) {
                String name = machine + (level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase());
                blockWithItem(name, COLLECTOR_MAP.get(name), "collectors");
            }
        }

        for (String typeName : SOLAR_MAP.keySet()) {
            blockSidesAndTop(typeName, SOLAR_MAP.get(typeName), "solar");
        }

        for (String typeName : BATTERY_MAP.keySet()) {
            blockSidesAndTop(BATTERY_MAP.get(typeName), typeName, "top_in", "side_in");
        }

        for (String typeName : PROCESSOR_MAP.keySet()) {
            simpleMachineModel(typeName, PROCESSOR_MAP.get(typeName), "processor");
        }

        fluids();
        turbine();
    }

    private void crossBlock(DeferredBlock<Block> deferredBlock) {
        String location = BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath();
        simpleBlock(deferredBlock.get(), models().cross(location, blockTexture(deferredBlock.get())).renderType("cutout"));
        itemModels().getBuilder("item/" + location).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", ncLoc("block/" + location));
    }

    private void fluids() {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            simpleBlock(fluid.block.get(), models().cubeAll(fluid.block.get().getName().getString(), fluid.client.getStillTexture()));
        }
    }

    private void turbine() {
        blockMachine("controller", TURBINE_MAP.get("turbine_controller"), "turbine");
        blockWithItem("wall", TURBINE_MAP.get("turbine_casing"), "turbine/casing");
        blockWithItem("glass", TURBINE_MAP.get("turbine_glass"), "turbine");
        blockWithItem("coil_connector", TURBINE_MAP.get("turbine_coil_connector"), "turbine");
        blockWithItem("rotor_bearing", TURBINE_MAP.get("turbine_rotor_bearing"), "turbine");
        blockSidesAndTop("rotor_shaft", TURBINE_MAP.get("turbine_rotor_shaft"), "turbine", "_end");
        blockSidesAndTop("inlet", TURBINE_MAP.get("turbine_inlet"), "turbine"); // TODO ADD FACING
        blockSidesAndTop("outlet", TURBINE_MAP.get("turbine_outlet"), "turbine");

        blockWithItem("magnesium", TURBINE_MAP.get("magnesium_turbine_dynamo_coil"), "turbine/dynamo_coil");
        blockWithItem("beryllium", TURBINE_MAP.get("beryllium_turbine_dynamo_coil"), "turbine/dynamo_coil");
        blockWithItem("aluminum", TURBINE_MAP.get("aluminum_turbine_dynamo_coil"), "turbine/dynamo_coil");
        blockWithItem("gold", TURBINE_MAP.get("gold_turbine_dynamo_coil"), "turbine/dynamo_coil");
        blockWithItem("copper", TURBINE_MAP.get("copper_turbine_dynamo_coil"), "turbine/dynamo_coil");
        blockWithItem("silver", TURBINE_MAP.get("silver_turbine_dynamo_coil"), "turbine/dynamo_coil");

        turbineBladeWithItem("steel", TURBINE_MAP.get("steel_turbine_rotor_blade"), "turbine/rotor_blade");
        turbineBladeWithItem("extreme", TURBINE_MAP.get("extreme_alloy_turbine_rotor_blade"), "turbine/rotor_blade");
        turbineBladeWithItem("sic_sic_cmc", TURBINE_MAP.get("sic_turbine_rotor_blade"), "turbine/rotor_blade");
        turbineBladeWithItem("rotor_stator", TURBINE_MAP.get("standard_turbine_rotor_stator"), "turbine");
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map, String folder) {
        for (String name : list) {
            blockWithItem(name, map.get(name), folder);
        }
    }

    private void simpleBlocks(HashMap<String, DeferredBlock<Block>> map, String folder) {
        simpleBlocks(map.keySet().stream().toList(), map, folder);
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        Block block = deferredBlock.get();
        ModelFile model = cubeAll(deferredBlock.get());
        simpleBlock(block, model);
        itemModels().getBuilder("item/" + BuiltInRegistries.BLOCK.getKey(block).getPath()).parent(model);
    }

    private void blockWithItemOverlay(DeferredBlock<Block> deferredBlock, Block underlay, String folder) {
        blockWithItemOverlay(deferredBlock, deferredBlock, underlay, folder);
    }

    private void blockWithItemOverlay(DeferredBlock<Block> deferredBlock, DeferredBlock<Block> shadow, Block underlay, String folder) {
        Block block = deferredBlock.get();
        String oreTexture = blockTexture(shadow.get()).getPath().replace(ModelProvider.BLOCK_FOLDER + "/", ModelProvider.BLOCK_FOLDER + "/" + folder + "/").replace("_ore", "");
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/cube_all_overlayed")).texture("all", blockTexture(underlay)).texture("overlay", modLoc(oreTexture));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder, String top) {
        blockSidesAndTop(deferredBlock, folder, top, "_side");
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder, String top, String side) {
        Block block = deferredBlock.get();
        String base = ModelProvider.BLOCK_FOLDER + "/" + folder + "/";
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/top_sides")).texture("top", modLoc(base + top)).texture("sides", modLoc(base + side));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(String name, DeferredBlock<Block> deferredBlock, String folder, String top) {
        Block block = deferredBlock.get();
        String base = ModelProvider.BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/top_sides")).texture("top", modLoc(base + top)).texture("sides", modLoc(base + "_side"));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(String name, DeferredBlock<Block> deferredBlock, String folder) {
        blockSidesAndTop(name, deferredBlock, folder, "_top");
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder) {
        blockSidesAndTop(deferredBlock, folder, "_top");
    }

    private void blockMachine(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String base = ModelProvider.BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_bottom")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front_off"));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockWithItem(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String texture = ModelProvider.BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc(texture));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void turbineBladeWithItem(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String texture = ModelProvider.BLOCK_FOLDER + "/" + folder + "/" + name;

        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc(name.contains("stator") ? "block/turbine_rotor_stator" : "block/turbine_rotor_blade")).texture("texture", texture);
        directionalBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void simpleMachineModel(String name, DeferredBlock<Block> deferredBlock, String folder) {
        ModelFile modelOn = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath() + "_on", modLoc("block/processor")).texture("front", modLoc("block/" + folder + "/" + name + "_front_on"));
        ModelFile modelOff = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath() + "_off", modLoc("block/processor")).texture("front", modLoc("block/" + folder + "/" + name + "_front_off"));
        horizontalBlock(deferredBlock.get(), state -> state.getValue(PROCESSOR_ON) ? modelOn : modelOff);

        simpleBlockItem(deferredBlock.get(), modelOff);
    }
}