package com.nred.nuclearcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.info.Names.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.RAW_MAP;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    private void ores() {
        for (String ore : ORES) {
            createOreDrops(ORE_MAP.get(ore).get(), RAW_MAP.get(ore).asItem(), 1, 2);
            createOreDrops(ORE_MAP.get(ore + "_deepslate").get(), RAW_MAP.get(ore).asItem(), 1, 2);
        }

        simpleBlocks(INGOTS, INGOT_BLOCK_MAP);
        simpleBlocks(RAWS, RAW_BLOCK_MAP);
    }

    private void simpleBlocks(List<String> list, HashMap<String, DeferredBlock<Block>> map) {
        for (String name : list) {
            dropSelf(map.get(name).get());
        }
    }

    private void createOreDrops(Block ore, Item item, float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(ore, this.createSilkTouchDispatchTable(ore, this.applyExplosionDecay(ore, LootItem.lootTableItem(item).apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE))))));
    }

    @Override
    public void generate() {
        ores();
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        List<Block> all = new ArrayList<>();
        all.addAll(Stream.of(ORE_MAP.values(), INGOT_BLOCK_MAP.values(), RAW_BLOCK_MAP.values()).flatMap(Collection::stream).map(DeferredBlock::get).toList());
        return all;
    }
}