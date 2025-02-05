package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.collector.CobbleGenerator;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.block.collector.NitrogenCollector;
import com.nred.nuclearcraft.block.collector.WaterSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.Registers.BLOCKS;
import static com.nred.nuclearcraft.registration.Registers.ITEMS;

public class BlockRegistration {
    public static final HashMap<String, DeferredBlock<Block>> ORE_MAP = createOres();
    public static final HashMap<String, DeferredBlock<Block>> INGOT_BLOCK_MAP = createBlocks(INGOTS, "block", Blocks.IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> RAW_BLOCK_MAP = createBlocks(RAWS, "raw", "block", Blocks.RAW_IRON_BLOCK);
    public static final HashMap<String, DeferredBlock<Block>> COLLECTOR_MAP = createCollectors();

    private static HashMap<String, DeferredBlock<Block>> createOres() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        for (String ore : ORES) {
            map.put(ore, registerBlockItem(ore, () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F))));
            map.put(ore + "_deepslate", registerBlockItem(ore + "_deepslate_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE))));
        }

        return map;
    }

    private static DeferredBlock<Block> registerBlockItem(String name, Supplier<Block> block) {
        DeferredBlock<Block> toReturn = BLOCKS.register(name, block);
        ITEMS.registerSimpleBlockItem(name, toReturn);
        return toReturn;
    }

    private static HashMap<String, DeferredBlock<Block>> createBlocks(List<String> names, String append, Block copy) {
        return createBlocks(names, "", append, copy);
    }

    private static HashMap<String, DeferredBlock<Block>> createBlocks(List<String> names, String prepend, String append, Block copy) {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        for (String name : names) {
            String reg_name = (!prepend.isEmpty() ? (prepend + "_") : "") + name + "_" + append;
            map.put(name, BLOCKS.register(reg_name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(copy))));
            ITEMS.registerSimpleBlockItem(reg_name, map.get(name));
        }
        return map;
    }

    private static HashMap<String, DeferredBlock<Block>> createCollectors() {
        HashMap<String, DeferredBlock<Block>> map = new HashMap<>();
        for (MACHINE_LEVEL level : MACHINE_LEVEL.values()) {
            String name = "cobblestone_generator" + (level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase());
            map.put(name, BLOCKS.register(name, () -> new CobbleGenerator(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE), level)));
            ITEMS.registerSimpleBlockItem(name, map.get(name));

            name = "water_source" + (level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase());
            map.put(name, BLOCKS.register(name, () -> new WaterSource(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE), level)));
            ITEMS.registerSimpleBlockItem(name, map.get(name));

            name = "nitrogen_collector" + (level.toString().isEmpty() ? "" : "_" + level.toString().toLowerCase());
            map.put(name, BLOCKS.register(name, () -> new NitrogenCollector(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE), level)));
            ITEMS.registerSimpleBlockItem(name, map.get(name));
        }
        return map;
    }

    public static void init() {
    }
}
