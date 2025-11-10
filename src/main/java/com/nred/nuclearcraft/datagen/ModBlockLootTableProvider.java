package com.nred.nuclearcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Concat.blockValues;
import static com.nred.nuclearcraft.info.Names.ORES;
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
    }

    private void blocks() {
        for (Block block : blockValues(INGOT_BLOCK_MAP, MATERIAL_BLOCK_MAP, RAW_BLOCK_MAP, FERTILE_ISOTOPE_MAP)) {
            dropSelf(block);
        }
    }

    private void multiblocks() {
        for (Block block : blockValues(MACHINE_MAP, RTG_MAP, BATTERY_MAP, FISSION_REACTOR_MAP, HX_MAP, TURBINE_MAP, QUANTUM_MAP)) {
            dropSelf(block);
        }
    }

    private void blocks_with_save() {
        for (Block block : blockValues(PROCESSOR_MAP, RTG_MAP, BATTERY_MAP, SOLAR_MAP)) {
            add(block, createMachineDrop(block));
        }
    }

    private void collectors() {
        for (Block block : blockValues(COLLECTOR_MAP)) {
            add(block, createMachineDrop(block));
        }
    }

    private void createOreDrops(Block ore, Item item, float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(ore, this.createSilkTouchDispatchTable(ore, this.applyExplosionDecay(ore, LootItem.lootTableItem(item).apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE))))));
    }

    private LootTable.Builder createMachineDrop(Block block) {
        return LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(block).apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.CUSTOM_DATA).include(DataComponents.CUSTOM_NAME)))
                        )
                );
    }

    @Override
    public void generate() {
        ores();
        blocks();
        multiblocks();
        blocks_with_save();
        collectors();

        dropWhenSilkTouch(TRITIUM_LAMP.get());
        dropWhenSilkTouch(SUPERCOLD_ICE.get());
        dropSelf(HEAVY_WATER_MODERATOR.get());
        dropSelf(SOLIDIFIED_CORIUM.get());
        dropSelf(UNIVERSAL_BIN.get());
        dropSelf(MACHINE_INTERFACE.get());
        dropSelf(NUCLEAR_FURNACE.get());
        dropSelf(GLOWING_MUSHROOM.get());
        dropSelf(DECAY_GENERATOR.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        List<Block> all = new ArrayList<>();
        all.addAll(blockValues(ORE_MAP, INGOT_BLOCK_MAP, MATERIAL_BLOCK_MAP, RAW_BLOCK_MAP, FERTILE_ISOTOPE_MAP));
        all.addAll(blockValues(COLLECTOR_MAP, SOLAR_MAP, BATTERY_MAP, RTG_MAP));
        all.addAll(blockValues(PROCESSOR_MAP));
        all.addAll(blockValues(MACHINE_MAP, RTG_MAP, BATTERY_MAP, FISSION_REACTOR_MAP, HX_MAP, TURBINE_MAP, QUANTUM_MAP));
        all.addAll(blockValues(GLOWING_MUSHROOM, TRITIUM_LAMP, HEAVY_WATER_MODERATOR, SUPERCOLD_ICE, SOLIDIFIED_CORIUM, UNIVERSAL_BIN, MACHINE_INTERFACE, NUCLEAR_FURNACE, DECAY_GENERATOR));
        return all;
    }
}