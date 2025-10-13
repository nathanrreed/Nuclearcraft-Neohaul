package com.nred.nuclearcraft.datagen;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.block.processor.Processor.PROCESSOR_ON;
import static com.nred.nuclearcraft.datagen.ModBlockStateProvider.Directionality.None;
import static com.nred.nuclearcraft.datagen.ModBlockStateProvider.Directionality.RotorPart;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static net.neoforged.neoforge.client.model.generators.ModelProvider.BLOCK_FOLDER;

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
        blockWithItem(HEAVY_WATER_MODERATOR, "fission");
        crossBlock(GLOWING_MUSHROOM);

        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            for (String machine : List.of("cobblestone_generator", "water_source", "nitrogen_collector")) {
                String name = machine + (level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase());
                blockWithItem(name, COLLECTOR_MAP.get(name), "collectors");
            }
        }

        for (String typeName : SOLAR_MAP.keySet()) {
            blockSidesAndTop(typeName, SOLAR_MAP.get(typeName), "solar", None);
        }

        for (String typeName : BATTERY_MAP.keySet()) {
            blockSidesAndTop(BATTERY_MAP.get(typeName), typeName, "top_input", "side_input", None);
        }

        for (String typeName : PROCESSOR_MAP.keySet()) {
            processorModel(typeName, PROCESSOR_MAP.get(typeName), "processor");
        }

        fluids();
        turbine();
        fission();
    }

    private void crossBlock(DeferredBlock<Block> deferredBlock) {
        String location = BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath();
        simpleBlock(deferredBlock.get(), models().cross(location, blockTexture(deferredBlock.get())).renderType("cutout"));
        itemModels().getBuilder("item/" + location).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", ncLoc("block/" + location));
    }

    private void fluids() {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            simpleBlock(fluid.block.get(), models().cubeAll(fluid.block.get().getName().getString(), fluid.client.getStillTexture()));
        }
    }

    enum Directionality {
        None,
        Directional,
        RotorPart
    }

    private void turbine() {
        directionalMachine("controller", TURBINE_MAP.get("turbine_controller"), "turbine", ACTIVE);
        booleanBlock("frame", "wall", TURBINE_MAP.get("turbine_casing"), "turbine/casing", FRAME);
        blockWithItemCutout("glass", TURBINE_MAP.get("turbine_glass"), "turbine");
        blockWithItem("coil_connector", TURBINE_MAP.get("turbine_coil_connector"), "turbine");
        blockWithItem("rotor_bearing", TURBINE_MAP.get("turbine_rotor_bearing"), "turbine");
        blockSidesAndTop("rotor_shaft", TURBINE_MAP.get("turbine_rotor_shaft"), "turbine", "_end", RotorPart);
        directionalTop("inlet", TURBINE_MAP.get("turbine_inlet"), "turbine");
        directionalTop("outlet", TURBINE_MAP.get("turbine_outlet"), "turbine");
        blockWithStateItem("redstone_port", TURBINE_MAP.get("turbine_redstone_port"), "turbine", ACTIVE);
        blockWithItem("computer_port", TURBINE_MAP.get("turbine_computer_port"), "turbine");

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

    private void fission() {
        directionalMachine("controller", FISSION_REACTOR_MAP.get("solid_fuel_fission_controller"), "fission/solid", ACTIVE);
        directionalMachine("controller", FISSION_REACTOR_MAP.get("molten_salt_fission_controller"), "fission/salt", ACTIVE);
        booleanBlock("frame", "wall", FISSION_REACTOR_MAP.get("fission_casing"), "fission/casing", FRAME);
        blockWithItemCutout("glass", FISSION_REACTOR_MAP.get("fission_glass"), "fission");
        vent("vent", "_output", "_input", FISSION_REACTOR_MAP.get("fission_vent"), "fission", ACTIVE);
        blockWithItem("conductor", FISSION_REACTOR_MAP.get("fission_conductor"), "fission");

        booleanBlockOverlay("boron_silver", "on", "off", FISSION_REACTOR_MAP.get("boron_silver_shield"), "fission/shield", ACTIVE);

        horizontalBooleanBlock("radium_beryllium", "source", "_on", "_off", FISSION_REACTOR_MAP.get("radium_beryllium_source"), "fission/source", ACTIVE);
        horizontalBooleanBlock("polonium_beryllium", "source", "_on", "_off", FISSION_REACTOR_MAP.get("polonium_beryllium_source"), "fission/source", ACTIVE);
        horizontalBooleanBlock("californium", "source", "_on", "_off", FISSION_REACTOR_MAP.get("californium_source"), "fission/source", ACTIVE);

        blockWithItem("beryllium_carbon", FISSION_REACTOR_MAP.get("beryllium_carbon_reflector"), "fission/reflector");
        blockWithItem("lead_steel", FISSION_REACTOR_MAP.get("lead_steel_reflector"), "fission/reflector");

        blockWithItem("cell", FISSION_REACTOR_MAP.get("fission_cell"), "fission/solid");
        directionalBooleanBlock("cell_out", "cell_in", FISSION_REACTOR_MAP.get("fission_cell_port"), "fission/port", "fission/port", ACTIVE);

        blockWithItem("cooler", FISSION_REACTOR_MAP.get("fission_cooler"), "fission");
        directionalBooleanBlock("cooler_out", "cooler_in", FISSION_REACTOR_MAP.get("fission_cooler_port"), "fission/port", "fission/port", ACTIVE);

        blockWithItem("irradiator", FISSION_REACTOR_MAP.get("fission_irradiator"), "fission");
        directionalBooleanBlock("irradiator_out", "irradiator_in", FISSION_REACTOR_MAP.get("fission_irradiator_port"), "fission/port", "fission/port", ACTIVE);

        blockWithItem("vessel", FISSION_REACTOR_MAP.get("fission_vessel"), "fission/salt");
        directionalBooleanBlock("vessel_out", "vessel_in", FISSION_REACTOR_MAP.get("fission_vessel_port"), "fission/port", "fission/port", ACTIVE);

        blockWithItem("water", FISSION_REACTOR_MAP.get("water_sink"), "fission/solid/sink");
        blockWithItem("standard", FISSION_REACTOR_MAP.get("standard_heater"), "fission/salt/heater");
        directionalBooleanBlockOverlay("standard", "out", "in", FISSION_REACTOR_MAP.get("standard_heater_port"), "fission/port", "fission/port/heater", ACTIVE);

        for (String name : FISSION_HEAT_PARTS) {
            blockWithItem(name, FISSION_REACTOR_MAP.get(name + "_sink"), "fission/solid/sink");
            blockWithItem(name, FISSION_REACTOR_MAP.get(name + "_heater"), "fission/salt/heater");
            directionalBooleanBlockOverlay(name, "out", "in", FISSION_REACTOR_MAP.get(name + "_heater_port"), "fission/port", "fission/port/heater", ACTIVE);
        }

        blockWithItem("computer_port", FISSION_REACTOR_MAP.get("fission_computer_port"), "fission");
    }

    // TODO rename all these functions and merge similar

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

    private void blockWithStateItem(String name, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile modelOn = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_on", modLoc(base + "_on"));
        ModelFile modelOff = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_off", modLoc(base + "_off"));

        horizontalBlock(block, state -> state.getValue(property) ? modelOn : modelOff);
        simpleBlockItem(block, modelOff);
    }

    private void blockWithItemOverlay(DeferredBlock<Block> deferredBlock, Block underlay, String folder) {
        blockWithItemOverlay(deferredBlock, deferredBlock, underlay, folder);
    }

    private void blockWithItemOverlay(DeferredBlock<Block> deferredBlock, DeferredBlock<Block> shadow, Block underlay, String folder) {
        Block block = deferredBlock.get();
        String oreTexture = blockTexture(shadow.get()).getPath().replace(BLOCK_FOLDER + "/", BLOCK_FOLDER + "/" + folder + "/").replace("_ore", "");
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/cube_all_overlayed")).texture("all", blockTexture(underlay)).texture("overlay", modLoc(oreTexture));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder, String top, Directionality directional) {
        blockSidesAndTop(deferredBlock, folder, top, "_side", directional);
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder, String top, String side, Directionality directional) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/";
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/top_sides")).texture("top", modLoc(base + top)).texture("sides", modLoc(base + side));

        switch (directional) {
            case None -> simpleBlock(block, model);
            case Directional -> directionalBlock(block, model);
            case RotorPart -> rotorPartModel(block, $ -> model);
        }
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(String name, DeferredBlock<Block> deferredBlock, String folder, String top, Directionality directional) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/top_sides")).texture("top", modLoc(base + top)).texture("sides", modLoc(base + "_side"));

        switch (directional) {
            case None -> simpleBlock(block, model);
            case Directional -> directionalBlock(block, model);
            case RotorPart -> rotorPartModel(block, $ -> model);
        }
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(String name, DeferredBlock<Block> deferredBlock, String folder, Directionality directional) {
        blockSidesAndTop(name, deferredBlock, folder, "_top", directional);
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder, Directionality directional) {
        blockSidesAndTop(deferredBlock, folder, "_top", directional);
    }

    private void horizontalMachine(String name, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile modelOn = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_on", modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_bottom")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front_on"));
        ModelFile modelOff = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_off", modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_bottom")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front_off"));

        horizontalBlock(block, state -> state.getValue(property) ? modelOn : modelOff);
        simpleBlockItem(block, modelOff);
    }

    private void directionalMachine(String name, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile modelOn = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_on", modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_bottom")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front_on"));
        ModelFile modelOff = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_off", modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_bottom")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front_off"));

        directionalBlock(block, state -> state.getValue(property) ? modelOn : modelOff);
        simpleBlockItem(block, modelOff);
    }

    private void horizontalTop(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_top")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front"));

        horizontalBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void directionalTop(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/machine")).texture("top", modLoc(base + "_top")).texture("bottom", modLoc(base + "_top")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front"));

        directionalBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void booleanBlock(String nameTrue, String nameFalse, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String texture = BLOCK_FOLDER + "/" + folder + "/";
        ModelFile modelTrue = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_true", modLoc(texture + nameTrue));
        ModelFile modelFalse = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_false", modLoc(texture + nameFalse));

        propertyBlock(block, state -> state.getValue(property) ? modelTrue : modelFalse);
        simpleBlockItem(block, modelFalse);
    }

    private void horizontalBooleanBlock(String front, String name, String nameTrue, String nameFalse, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/";
        ModelFile modelTrue = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_true", modLoc("block/machine")).texture("top", modLoc(base + name + "_side")).texture("bottom", modLoc(base + name + "_side")).texture("side", modLoc(base + name + "_side")).texture("back", modLoc(base + name + "_back" + nameTrue)).texture("front", modLoc(base + front));
        ModelFile modelFalse = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_false", modLoc("block/machine")).texture("top", modLoc(base + name + "_side")).texture("bottom", modLoc(base + name + "_side")).texture("side", modLoc(base + name + "_side")).texture("back", modLoc(base + name + "_back" + nameFalse)).texture("front", modLoc(base + front));

        horizontalBlock(block, state -> state.getValue(property) ? modelTrue : modelFalse);
        simpleBlockItem(block, modelFalse);
    }

    private void directionalBooleanBlock(String name_true, String name_false, DeferredBlock<Block> deferredBlock, String sideFolder, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/";
        String side = BLOCK_FOLDER + "/" + sideFolder + "/";
        ModelFile modelTrue = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_true", modLoc("block/machine")).texture("top", modLoc(side + "top")).texture("bottom", modLoc(side + "top")).texture("side", modLoc(side + "side")).texture("back", modLoc(base + name_true)).texture("front", modLoc(base + name_true));
        ModelFile modelFalse = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_false", modLoc("block/machine")).texture("top", modLoc(side + "top")).texture("bottom", modLoc(side + "top")).texture("side", modLoc(side + "side")).texture("back", modLoc(base + name_false)).texture("front", modLoc(base + name_false));
        directionalBlock(block, state -> state.getValue(property) ? modelTrue : modelFalse);
        simpleBlockItem(block, modelFalse);
    }

    private void directionalBooleanBlockOverlay(String name, String name_true, String name_false, DeferredBlock<Block> deferredBlock, String sideFolder, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/";
        String side = BLOCK_FOLDER + "/" + sideFolder + "/";
        ResourceLocation none = modLoc(BLOCK_FOLDER + "/block_invisible");
        ModelFile modelTrue = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_true", modLoc("block/machine_overlayed")).texture("top", modLoc(side + "top")).texture("bottom", modLoc(side + "top")).texture("side", modLoc(side + "side")).texture("back", modLoc(base + name)).texture("front", modLoc(base + name)).texture("back_overlay", modLoc(base + name_true)).texture("front_overlay", modLoc(base + name_true)).texture("top_overlay", none).texture("bottom_overlay", none).texture("side_overlay", none);
        ModelFile modelFalse = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_false", modLoc("block/machine_overlayed")).texture("top", modLoc(side + "top")).texture("bottom", modLoc(side + "top")).texture("side", modLoc(side + "side")).texture("back", modLoc(base + name)).texture("front", modLoc(base + name)).texture("back_overlay", modLoc(base + name_false)).texture("front_overlay", modLoc(base + name_false)).texture("top_overlay", none).texture("bottom_overlay", none).texture("side_overlay", none);

        directionalBlock(block, state -> state.getValue(property) ? modelTrue : modelFalse);
        simpleBlockItem(block, modelFalse);
    }

    private void horizontalFontalBlock(String name, DeferredBlock<Block> deferredBlock, String sideFolder, String folder) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/";
        String side = BLOCK_FOLDER + "/" + sideFolder + "/";
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/machine")).texture("top", modLoc(side + "top")).texture("bottom", modLoc(side + "top")).texture("side", modLoc(side + "side")).texture("back", modLoc(base + name)).texture("front", modLoc(base + name));

        horizontalBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void directionalFontalBlock(String name, DeferredBlock<Block> deferredBlock, String sideFolder, String folder) {
        directionalFontalBlock(name, name, deferredBlock, sideFolder, folder);
    }

    private void directionalFontalBlock(String name_front, String name_back, DeferredBlock<Block> deferredBlock, String sideFolder, String folder) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/";
        String side = BLOCK_FOLDER + "/" + sideFolder + "/";
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/machine")).texture("top", modLoc(side + "top")).texture("bottom", modLoc(side + "top")).texture("side", modLoc(side + "side")).texture("back", modLoc(base + name_back)).texture("front", modLoc(base + name_front));

        directionalBlock(block, model);
        simpleBlockItem(block, model);
    }

    public void directionalBlock(Block block, ModelFile model) {
        directionalBlock(block, model, 180);
    }

    public void directionalBlock(Block block, ModelFile model, int angleOffset) {
        directionalBlock(block, $ -> model, angleOffset);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        directionalBlock(block, modelFunc, 180);
    }

    public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir == Direction.DOWN ? 270 : dir.getAxis().isHorizontal() ? 0 : 90)
                            .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
                            .build();
                });
    }

    private void booleanBlockOverlay(String name, String nameTrue, String nameFalse, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String texture = BLOCK_FOLDER + "/" + folder + "/";
        ModelFile modelTrue = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_true", modLoc("block/cube_all_overlayed")).texture("all", modLoc(texture + name)).texture("overlay", modLoc(texture + nameTrue));
        ModelFile modelFalse = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_false", modLoc("block/cube_all_overlayed")).texture("all", modLoc(texture + name)).texture("overlay", modLoc(texture + nameFalse));

        propertyBlock(block, state -> state.getValue(property) ? modelTrue : modelFalse);
        simpleBlockItem(block, modelFalse);
    }

    private void vent(String nameBase, String nameTrue, String nameFalse, DeferredBlock<Block> deferredBlock, String folder, BooleanProperty property) {
        Block block = deferredBlock.get();
        String base = BLOCK_FOLDER + "/" + folder + "/" + nameBase;
        ModelFile modelTrue = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + nameTrue, modLoc("block/machine")).texture("top", modLoc(base + "_side")).texture("bottom", modLoc(base + "_side")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front" + nameTrue));
        ModelFile modelFalse = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + nameFalse, modLoc("block/machine")).texture("top", modLoc(base + "_side")).texture("bottom", modLoc(base + "_side")).texture("side", modLoc(base + "_side")).texture("back", modLoc(base + "_back")).texture("front", modLoc(base + "_front" + nameFalse));

        directionalBlock(block, state -> state.getValue(property) ? modelTrue : modelFalse);
        simpleBlockItem(block, modelFalse);
    }

    public void propertyBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .build());
    }


    private void blockWithItem(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String texture = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc(texture));
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock, String folder) {
        blockWithItem(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), deferredBlock, folder);
    }

    private void blockWithItemCutout(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String texture = BLOCK_FOLDER + "/" + folder + "/" + name;
        ModelFile model = models().cubeAll(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc(texture)).renderType("cutout");
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void turbineBladeWithItem(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String texture = BLOCK_FOLDER + "/" + folder + "/" + name;

        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc(name.contains("stator") ? "block/turbine_rotor_stator" : "block/turbine_rotor_blade")).texture("texture", texture);
        rotorPartModel(block, $ -> model);
        simpleBlockItem(block, model);
    }

    private void processorModel(String name, DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        ModelFile modelOn = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_on", modLoc("block/processor")).texture("front", modLoc("block/" + folder + "/" + name + "_front_on"));
        ModelFile modelOff = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_off", modLoc("block/processor")).texture("front", modLoc("block/" + folder + "/" + name + "_front_off"));
        horizontalBlock(block, state -> state.getValue(PROCESSOR_ON) ? modelOn : modelOff);

        simpleBlockItem(block, modelOff);
    }

    private void rotorPartModel(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    TurbinePartDir dir = state.getValue(TurbineRotorBladeUtil.DIR);

                    return switch (dir) {
                        case INVISIBLE -> ConfiguredModel.builder().modelFile(models().getExistingFile(modLoc("block_invisible"))).build();
                        case X -> ConfiguredModel.builder().modelFile(modelFunc.apply(state)).rotationX(90).rotationY(90).build();
                        case Y -> ConfiguredModel.builder().modelFile(modelFunc.apply(state)).build();
                        case Z -> ConfiguredModel.builder().modelFile(modelFunc.apply(state)).rotationX(90).build();
                    };
                });
    }
}