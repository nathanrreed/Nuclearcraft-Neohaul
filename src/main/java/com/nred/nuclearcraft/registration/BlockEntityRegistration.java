package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.collector.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.registration.BlockRegistration.COLLECTOR_MAP;
import static com.nred.nuclearcraft.registration.Registers.BLOCK_ENTITY_TYPES;

public class BlockEntityRegistration {
    public static final Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> COBBLE_GENERATOR_TYPES = createCobblestoneCollector();
    public static final Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> WATER_SOURCE_TYPES = createWaterCollector();
    public static final Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> NITROGEN_COLLECTOR_TYPES = createNitrogenCollector();

    private static Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> createCobblestoneCollector() {
        Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> map = new HashMap<>();
        map.put(MACHINE_LEVEL.BASE, BLOCK_ENTITY_TYPES.register("cobblestone_generator", () -> BlockEntityType.Builder.of(CobbleGeneratorEntity::new, COLLECTOR_MAP.get("cobblestone_generator").get()).build(null)));
        map.put(MACHINE_LEVEL.COMPACT, BLOCK_ENTITY_TYPES.register("cobblestone_generator_compact", () -> BlockEntityType.Builder.of(CobbleGeneratorEntityCompact::new, COLLECTOR_MAP.get("cobblestone_generator_compact").get()).build(null)));
        map.put(MACHINE_LEVEL.DENSE, BLOCK_ENTITY_TYPES.register("cobblestone_generator_dense", () -> BlockEntityType.Builder.of(CobbleGeneratorEntityDense::new, COLLECTOR_MAP.get("cobblestone_generator_dense").get()).build(null)));
        return map;
    }

    private static Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> createWaterCollector() {
        Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> map = new HashMap<>();
        map.put(MACHINE_LEVEL.BASE, BLOCK_ENTITY_TYPES.register("water_source", () -> BlockEntityType.Builder.of(WaterSourceEntity::new, COLLECTOR_MAP.get("water_source").get()).build(null)));
        map.put(MACHINE_LEVEL.COMPACT, BLOCK_ENTITY_TYPES.register("water_source_compact", () -> BlockEntityType.Builder.of(WaterSourceEntityCompact::new, COLLECTOR_MAP.get("water_source_compact").get()).build(null)));
        map.put(MACHINE_LEVEL.DENSE, BLOCK_ENTITY_TYPES.register("water_source_dense", () -> BlockEntityType.Builder.of(WaterSourceEntityDense::new, COLLECTOR_MAP.get("water_source_dense").get()).build(null)));
        return map;
    }

    private static Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> createNitrogenCollector() {
        Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> map = new HashMap<>();
        map.put(MACHINE_LEVEL.BASE, BLOCK_ENTITY_TYPES.register("nitrogen_collector", () -> BlockEntityType.Builder.of(NitrogenCollectorEntity::new, COLLECTOR_MAP.get("nitrogen_collector").get()).build(null)));
        map.put(MACHINE_LEVEL.COMPACT, BLOCK_ENTITY_TYPES.register("nitrogen_collector_compact", () -> BlockEntityType.Builder.of(NitrogenCollectorEntityCompact::new, COLLECTOR_MAP.get("nitrogen_collector_compact").get()).build(null)));
        map.put(MACHINE_LEVEL.DENSE, BLOCK_ENTITY_TYPES.register("nitrogen_collector_dense", () -> BlockEntityType.Builder.of(NitrogenCollectorEntityDense::new, COLLECTOR_MAP.get("nitrogen_collector_dense").get()).build(null)));
        return map;
    }

    public static void init() {
    }
}