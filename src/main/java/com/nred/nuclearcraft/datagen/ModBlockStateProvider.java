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
        simpleBlocks(RAWS, RAW_BLOCK_MAP, "raw_block");
        blockWithItem(SUPERCOLD_ICE);
        blockWithItem(SOLIDIFIED_CORIUM);

        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            for (String machine : List.of("cobblestone_generator", "water_source", "nitrogen_collector")) {
                String name = machine + (level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase());
                blockWithItem(name, COLLECTOR_MAP.get(name), "collectors");
            }
        }

        for (String typeName : SOLAR_MAP.keySet()) {
            blockSidesAndTop(SOLAR_MAP.get(typeName), "solar");
        }

        for (String typeName : PROCESSOR_MAP.keySet()) {
            simpleMachineModel(typeName, PROCESSOR_MAP.get(typeName), "processor");
        }

        fluids();
    }

    private void fluids() {
        for (Fluids fluid : fluidValues(GASSES, MOLTEN, CUSTOM_FLUID, HOT_GAS, SUGAR, CHOCOLATE, FISSION, STEAM, SALT_SOLUTION, ACID, FLAMMABLE, HOT_COOLANT, COOLANT)) {
            simpleBlock(fluid.block.get(), models().cubeAll(fluid.block.get().getName().getString(), fluid.client.getStillTexture()));
        }
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
        String oreTexture = blockTexture(shadow.get()).getPath().replace(ModelProvider.BLOCK_FOLDER + "/", ModelProvider.BLOCK_FOLDER + "/" + folder + "/");
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/cube_all_overlayed")).texture("all", blockTexture(underlay)).texture("overlay", modLoc(oreTexture));

        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void blockSidesAndTop(DeferredBlock<Block> deferredBlock, String folder) {
        Block block = deferredBlock.get();
        String base = blockTexture(block).getPath().replace(ModelProvider.BLOCK_FOLDER + "/", ModelProvider.BLOCK_FOLDER + "/" + folder + "/");
        ModelFile model = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), modLoc("block/top_sides")).texture("top", modLoc(base + "_top")).texture("sides", modLoc(base + "_side"));

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

    private void simpleMachineModel(String name, DeferredBlock<Block> deferredBlock, String folder) {
        ModelFile modelOn = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath() + "_on", modLoc("block/processor")).texture("front", modLoc("block/" + folder + "/" + name + "_front_on"));
        ModelFile modelOff = models().withExistingParent(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath() + "_off", modLoc("block/processor")).texture("front", modLoc("block/" + folder + "/" + name + "_front_off"));
        horizontalBlock(deferredBlock.get(), state -> state.getValue(PROCESSOR_ON) ? modelOn : modelOff);

        simpleBlockItem(deferredBlock.get(), modelOff);
    }
}