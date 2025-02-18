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
import java.util.concurrent.CompletableFuture;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.blockValues;
import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;

class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(blockValues(ORE_MAP, COLLECTOR_MAP, PROCESSOR_MAP).toArray(Block[]::new))
                .add(blockValues(SOLIDIFIED_CORIUM).toArray(Block[]::new));

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(blockValues(ORE_MAP, PROCESSOR_MAP).toArray(Block[]::new));

        tag(BlockTags.NEEDS_DIAMOND_TOOL);

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