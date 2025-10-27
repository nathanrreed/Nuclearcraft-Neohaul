package com.nred.nuclearcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.blockValues;
import static com.nred.nuclearcraft.helpers.Concat.blockValuesExcluding;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;

class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(blockValues(ORE_MAP, INGOT_BLOCK_MAP, MATERIAL_BLOCK_MAP, RAW_BLOCK_MAP, FERTILE_ISOTOPE_MAP, COLLECTOR_MAP, SOLAR_MAP, RTG_MAP, TURBINE_MAP, FISSION_REACTOR_MAP, BATTERY_MAP, PROCESSOR_MAP).toArray(Block[]::new))
                .add(blockValues(TRITIUM_LAMP, HEAVY_WATER_MODERATOR, SUPERCOLD_ICE, SOLIDIFIED_CORIUM, UNIVERSAL_BIN, MACHINE_INTERFACE, NUCLEAR_FURNACE, DECAY_GENERATOR).toArray(Block[]::new));

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(blockValuesExcluding(Set.of("tin", "lead"), ORE_MAP).toArray(Block[]::new));

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ORE_MAP.get("tin").get(), ORE_MAP.get("lead").get());

        simpleTag(INGOTS, INGOT_BLOCK_MAP, Tags.Blocks.STORAGE_BLOCKS);
        simpleTag(FERTILE_ISOTOPES, FERTILE_ISOTOPE_MAP, Tags.Blocks.STORAGE_BLOCKS);
        simpleTag(RAWS, RAW_BLOCK_MAP, Tags.Blocks.STORAGE_BLOCKS, "raw_");
    }

    private void simpleTag(List<String> list, HashMap<String, DeferredBlock<Block>> map, TagKey<Block> tag) {
        simpleTag(list, map, tag, "");
    }

    private void simpleTag(List<String> list, HashMap<String, DeferredBlock<Block>> map, TagKey<Block> tag, String prefix) {
        for (String name : list) {
            tag(tag).add(map.get(name).get());
            tag(BlockTags.create(tag.location().withSuffix("/" + prefix + name))).add(map.get(name).get());
        }
    }
}