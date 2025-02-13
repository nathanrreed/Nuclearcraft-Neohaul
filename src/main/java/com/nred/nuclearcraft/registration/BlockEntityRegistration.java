package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.collector.CollectorEntity;
import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import com.nred.nuclearcraft.block.collector.cobblestone_generator.CobbleGeneratorEntity;
import com.nred.nuclearcraft.block.collector.cobblestone_generator.CobbleGeneratorEntityCompact;
import com.nred.nuclearcraft.block.collector.cobblestone_generator.CobbleGeneratorEntityDense;
import com.nred.nuclearcraft.block.collector.nitrogen_collector.NitrogenCollectorEntity;
import com.nred.nuclearcraft.block.collector.nitrogen_collector.NitrogenCollectorEntityCompact;
import com.nred.nuclearcraft.block.collector.nitrogen_collector.NitrogenCollectorEntityDense;
import com.nred.nuclearcraft.block.collector.water_source.WaterSourceEntity;
import com.nred.nuclearcraft.block.collector.water_source.WaterSourceEntityCompact;
import com.nred.nuclearcraft.block.collector.water_source.WaterSourceEntityDense;
import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.block.processor.rock_crusher.RockCrusherEntity;
import com.nred.nuclearcraft.block.solar.SolarPanelEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.Registers.BLOCK_ENTITY_TYPES;

public class BlockEntityRegistration {
    public static final Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> COBBLE_GENERATOR_TYPES = createCobblestoneCollector();
    public static final Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> WATER_SOURCE_TYPES = createWaterCollector();
    public static final Map<Enum<MACHINE_LEVEL>, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CollectorEntity>>> NITROGEN_COLLECTOR_TYPES = createNitrogenCollector();
    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends ProcessorEntity>>> PROCESSOR_ENTITY_TYPE = createProcessors();
    public static final Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends SolarPanelEntity>>> SOLAR_PANEL_ENTITY_TYPE = createSolarPanels();

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

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends ProcessorEntity>>> createProcessors() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends ProcessorEntity>>> map = new HashMap<>();
        map.put("alloy_furnace", BLOCK_ENTITY_TYPES.register("alloy_furnace", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("alloy_furnace").get()).build(null)));
        map.put("assembler", BLOCK_ENTITY_TYPES.register("assembler", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("assembler").get()).build(null)));
        map.put("centrifuge", BLOCK_ENTITY_TYPES.register("centrifuge", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("centrifuge").get()).build(null)));
        map.put("chemical_reactor", BLOCK_ENTITY_TYPES.register("chemical_reactor", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("chemical_reactor").get()).build(null)));
        map.put("crystallizer", BLOCK_ENTITY_TYPES.register("crystallizer", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("crystallizer").get()).build(null)));
        map.put("decay_hastener", BLOCK_ENTITY_TYPES.register("decay_hastener", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("decay_hastener").get()).build(null)));
        map.put("electric_furnace", BLOCK_ENTITY_TYPES.register("electric_furnace", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("electric_furnace").get()).build(null)));
        map.put("electrolyzer", BLOCK_ENTITY_TYPES.register("electrolyzer", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("electrolyzer").get()).build(null)));
        map.put("fluid_enricher", BLOCK_ENTITY_TYPES.register("fluid_enricher", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("fluid_enricher").get()).build(null)));
        map.put("fluid_extractor", BLOCK_ENTITY_TYPES.register("fluid_extractor", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("fluid_extractor").get()).build(null)));
        map.put("fuel_reprocessor", BLOCK_ENTITY_TYPES.register("fuel_reprocessor", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("fuel_reprocessor").get()).build(null)));
        map.put("fluid_infuser", BLOCK_ENTITY_TYPES.register("fluid_infuser", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("fluid_infuser").get()).build(null)));
        map.put("ingot_former", BLOCK_ENTITY_TYPES.register("ingot_former", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("ingot_former").get()).build(null)));
        map.put("manufactory", BLOCK_ENTITY_TYPES.register("manufactory", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("manufactory").get()).build(null)));
        map.put("melter", BLOCK_ENTITY_TYPES.register("melter", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("melter").get()).build(null)));
        map.put("pressurizer", BLOCK_ENTITY_TYPES.register("pressurizer", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("pressurizer").get()).build(null)));
        map.put("rock_crusher", BLOCK_ENTITY_TYPES.register("rock_crusher", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("rock_crusher").get()).build(null)));
        map.put("fluid_mixer", BLOCK_ENTITY_TYPES.register("fluid_mixer", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("fluid_mixer").get()).build(null)));
        map.put("separator", BLOCK_ENTITY_TYPES.register("separator", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("separator").get()).build(null)));
        map.put("supercooler", BLOCK_ENTITY_TYPES.register("supercooler", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("supercooler").get()).build(null)));
        return map;
    }

    private static Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends SolarPanelEntity>>> createSolarPanels() {
        Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends SolarPanelEntity>>> map = new HashMap<>();
        map.put(0, BLOCK_ENTITY_TYPES.register("solar_panel_basic", () -> BlockEntityType.Builder.of((pos, state) -> new SolarPanelEntity(pos, state, 0), SOLAR_MAP.get("solar_panel_basic").get()).build(null)));
        map.put(1, BLOCK_ENTITY_TYPES.register("solar_panel_advanced", () -> BlockEntityType.Builder.of((pos, state) -> new SolarPanelEntity(pos, state, 1), SOLAR_MAP.get("solar_panel_advanced").get()).build(null)));
        map.put(2, BLOCK_ENTITY_TYPES.register("solar_panel_du", () -> BlockEntityType.Builder.of((pos, state) -> new SolarPanelEntity(pos, state, 2), SOLAR_MAP.get("solar_panel_du").get()).build(null)));
        map.put(3, BLOCK_ENTITY_TYPES.register("solar_panel_elite", () -> BlockEntityType.Builder.of((pos, state) -> new SolarPanelEntity(pos, state, 3), SOLAR_MAP.get("solar_panel_elite").get()).build(null)));
        return map;
    }

    public static void init() {
    }
}