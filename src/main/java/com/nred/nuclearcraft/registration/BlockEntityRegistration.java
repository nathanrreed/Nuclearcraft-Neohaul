package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.batteries.BatteryEntity;
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
import com.nred.nuclearcraft.block.machine_interface.MachineInterfaceEntity;
import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.block.processor.alloy_furnace.AlloyFurnaceEntity;
import com.nred.nuclearcraft.block.processor.assembler.AssemblerEntity;
import com.nred.nuclearcraft.block.processor.centrifuge.CentrifugeEntity;
import com.nred.nuclearcraft.block.processor.chemical_reactor.ChemicalReactorEntity;
import com.nred.nuclearcraft.block.processor.crystallizer.CrystallizerEntity;
import com.nred.nuclearcraft.block.processor.decay_hastener.DecayHastenerEntity;
import com.nred.nuclearcraft.block.processor.electric_furnace.ElectricFurnaceEntity;
import com.nred.nuclearcraft.block.processor.electrolyzer.ElectrolyzerEntity;
import com.nred.nuclearcraft.block.processor.fluid_enricher.EnricherEntity;
import com.nred.nuclearcraft.block.processor.fluid_extractor.ExtractorEntity;
import com.nred.nuclearcraft.block.processor.fluid_infuser.InfuserEntity;
import com.nred.nuclearcraft.block.processor.fluid_mixer.SaltMixerEntity;
import com.nred.nuclearcraft.block.processor.fuel_reprocessor.FuelReprocessorEntity;
import com.nred.nuclearcraft.block.processor.ingot_former.IngotFormerEntity;
import com.nred.nuclearcraft.block.processor.manufactory.ManufactoryEntity;
import com.nred.nuclearcraft.block.processor.melter.MelterEntity;
import com.nred.nuclearcraft.block.processor.pressurizer.PressurizerEntity;
import com.nred.nuclearcraft.block.processor.rock_crusher.RockCrusherEntity;
import com.nred.nuclearcraft.block.processor.separator.SeparatorEntity;
import com.nred.nuclearcraft.block.processor.supercooler.SupercoolerEntity;
import com.nred.nuclearcraft.block.solar.SolarPanelEntity;
import com.nred.nuclearcraft.block.turbine.*;
import com.nred.nuclearcraft.block.universal_bin.UniversalBinEntity;
import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorStatorType;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
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
    public static final Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>>> BATTERY_ENTITY_TYPE = createBatteries();
    //    public static final Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends Turbine>>> TURBINE_ENTITY_TYPE = createTurbines(); TODO
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineControllerEntity>> TURBINE_CONTROLLER = BLOCK_ENTITY_TYPES.register("turbine_controller", () -> BlockEntityType.Builder.of(TurbineControllerEntity::new, TURBINE_MAP.get("turbine_controller").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineCasingEntity>> TURBINE_CASING = BLOCK_ENTITY_TYPES.register("turbine_casing", () -> BlockEntityType.Builder.of(TurbineCasingEntity::new, TURBINE_MAP.get("turbine_casing").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineRotorShaftEntity>> TURBINE_ROTOR_SHAFT = BLOCK_ENTITY_TYPES.register("turbine_rotor_shaft", () -> BlockEntityType.Builder.of(TurbineRotorShaftEntity::new, TURBINE_MAP.get("turbine_rotor_shaft").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineRotorBearingEntity>> TURBINE_ROTOR_BEARING = BLOCK_ENTITY_TYPES.register("turbine_rotor_bearing", () -> BlockEntityType.Builder.of(TurbineRotorBearingEntity::new, TURBINE_MAP.get("turbine_rotor_bearing").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineDynamoCoilEntity>> TURBINE_DYNAMO = BLOCK_ENTITY_TYPES.register("turbine_dynamo", () -> BlockEntityType.Builder.of((pos, state) -> new TurbineDynamoCoilEntity(pos, state, ((TurbineDynamoCoilType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), TURBINE_MAP.get("magnesium_turbine_dynamo_coil").get(), TURBINE_MAP.get("beryllium_turbine_dynamo_coil").get(), TURBINE_MAP.get("aluminum_turbine_dynamo_coil").get(), TURBINE_MAP.get("gold_turbine_dynamo_coil").get(), TURBINE_MAP.get("copper_turbine_dynamo_coil").get(), TURBINE_MAP.get("silver_turbine_dynamo_coil").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineCoilConnectorEntity>> TURBINE_COIL_CONNECTOR = BLOCK_ENTITY_TYPES.register("connector", () -> BlockEntityType.Builder.of(TurbineCoilConnectorEntity::new, TURBINE_MAP.get("turbine_coil_connector").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineRotorBladeEntity>> TURBINE_ROTOR_BLADE = BLOCK_ENTITY_TYPES.register("turbine_rotor_blade", () -> BlockEntityType.Builder.of((pos, state) -> new TurbineRotorBladeEntity(pos, state, ((TurbineRotorBladeType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), TURBINE_MAP.get("steel_turbine_rotor_blade").get(), TURBINE_MAP.get("extreme_alloy_turbine_rotor_blade").get(), TURBINE_MAP.get("sic_turbine_rotor_blade").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineRotorStatorEntity>> TURBINE_ROTOR_STATOR = BLOCK_ENTITY_TYPES.register("turbine_rotor_stator", () -> BlockEntityType.Builder.of((pos, state) -> new TurbineRotorStatorEntity(pos, state, ((TurbineRotorStatorType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), TURBINE_MAP.get("standard_turbine_rotor_stator").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineOutletEntity>> TURBINE_OUTLET = BLOCK_ENTITY_TYPES.register("turbine_outlet", () -> BlockEntityType.Builder.of(TurbineOutletEntity::new, TURBINE_MAP.get("turbine_outlet").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineInletEntity>> TURBINE_INLET = BLOCK_ENTITY_TYPES.register("turbine_inlet", () -> BlockEntityType.Builder.of(TurbineInletEntity::new, TURBINE_MAP.get("turbine_inlet").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineGlassEntity>> TURBINE_GLASS = BLOCK_ENTITY_TYPES.register("turbine_glass", () -> BlockEntityType.Builder.of(TurbineGlassEntity::new, TURBINE_MAP.get("turbine_glass").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineComputerPortEntity>> TURBINE_COMPUTER_PORT = BLOCK_ENTITY_TYPES.register("turbine_computer_port", () -> BlockEntityType.Builder.of(TurbineComputerPortEntity::new, TURBINE_MAP.get("turbine_computer_port").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TurbineRedstonePortEntity>> TURBINE_REDSTONE_PORT = BLOCK_ENTITY_TYPES.register("turbine_redstone_port", () -> BlockEntityType.Builder.of(TurbineRedstonePortEntity::new, TURBINE_MAP.get("turbine_redstone_port").get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends UniversalBinEntity>> UNIVERSAL_BIN_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("universal_bin", () -> BlockEntityType.Builder.of(UniversalBinEntity::new, UNIVERSAL_BIN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends MachineInterfaceEntity>> MACHINE_INTERFACE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("machine_interface", () -> BlockEntityType.Builder.of(MachineInterfaceEntity::new, MACHINE_INTERFACE.get()).build(null));

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
        map.put("alloy_furnace", BLOCK_ENTITY_TYPES.register("alloy_furnace", () -> BlockEntityType.Builder.of(AlloyFurnaceEntity::new, PROCESSOR_MAP.get("alloy_furnace").get()).build(null)));
        map.put("assembler", BLOCK_ENTITY_TYPES.register("assembler", () -> BlockEntityType.Builder.of(AssemblerEntity::new, PROCESSOR_MAP.get("assembler").get()).build(null)));
        map.put("centrifuge", BLOCK_ENTITY_TYPES.register("centrifuge", () -> BlockEntityType.Builder.of(CentrifugeEntity::new, PROCESSOR_MAP.get("centrifuge").get()).build(null)));
        map.put("chemical_reactor", BLOCK_ENTITY_TYPES.register("chemical_reactor", () -> BlockEntityType.Builder.of(ChemicalReactorEntity::new, PROCESSOR_MAP.get("chemical_reactor").get()).build(null)));
        map.put("crystallizer", BLOCK_ENTITY_TYPES.register("crystallizer", () -> BlockEntityType.Builder.of(CrystallizerEntity::new, PROCESSOR_MAP.get("crystallizer").get()).build(null)));
        map.put("decay_hastener", BLOCK_ENTITY_TYPES.register("decay_hastener", () -> BlockEntityType.Builder.of(DecayHastenerEntity::new, PROCESSOR_MAP.get("decay_hastener").get()).build(null)));
        map.put("electric_furnace", BLOCK_ENTITY_TYPES.register("electric_furnace", () -> BlockEntityType.Builder.of(ElectricFurnaceEntity::new, PROCESSOR_MAP.get("electric_furnace").get()).build(null)));
        map.put("electrolyzer", BLOCK_ENTITY_TYPES.register("electrolyzer", () -> BlockEntityType.Builder.of(ElectrolyzerEntity::new, PROCESSOR_MAP.get("electrolyzer").get()).build(null)));
        map.put("fluid_enricher", BLOCK_ENTITY_TYPES.register("fluid_enricher", () -> BlockEntityType.Builder.of(EnricherEntity::new, PROCESSOR_MAP.get("fluid_enricher").get()).build(null)));
        map.put("fluid_extractor", BLOCK_ENTITY_TYPES.register("fluid_extractor", () -> BlockEntityType.Builder.of(ExtractorEntity::new, PROCESSOR_MAP.get("fluid_extractor").get()).build(null)));
        map.put("fuel_reprocessor", BLOCK_ENTITY_TYPES.register("fuel_reprocessor", () -> BlockEntityType.Builder.of(FuelReprocessorEntity::new, PROCESSOR_MAP.get("fuel_reprocessor").get()).build(null)));
        map.put("fluid_infuser", BLOCK_ENTITY_TYPES.register("fluid_infuser", () -> BlockEntityType.Builder.of(InfuserEntity::new, PROCESSOR_MAP.get("fluid_infuser").get()).build(null)));
        map.put("ingot_former", BLOCK_ENTITY_TYPES.register("ingot_former", () -> BlockEntityType.Builder.of(IngotFormerEntity::new, PROCESSOR_MAP.get("ingot_former").get()).build(null)));
        map.put("manufactory", BLOCK_ENTITY_TYPES.register("manufactory", () -> BlockEntityType.Builder.of(ManufactoryEntity::new, PROCESSOR_MAP.get("manufactory").get()).build(null)));
        map.put("melter", BLOCK_ENTITY_TYPES.register("melter", () -> BlockEntityType.Builder.of(MelterEntity::new, PROCESSOR_MAP.get("melter").get()).build(null)));
        map.put("pressurizer", BLOCK_ENTITY_TYPES.register("pressurizer", () -> BlockEntityType.Builder.of(PressurizerEntity::new, PROCESSOR_MAP.get("pressurizer").get()).build(null)));
        map.put("rock_crusher", BLOCK_ENTITY_TYPES.register("rock_crusher", () -> BlockEntityType.Builder.of(RockCrusherEntity::new, PROCESSOR_MAP.get("rock_crusher").get()).build(null)));
        map.put("fluid_mixer", BLOCK_ENTITY_TYPES.register("fluid_mixer", () -> BlockEntityType.Builder.of(SaltMixerEntity::new, PROCESSOR_MAP.get("fluid_mixer").get()).build(null)));
        map.put("separator", BLOCK_ENTITY_TYPES.register("separator", () -> BlockEntityType.Builder.of(SeparatorEntity::new, PROCESSOR_MAP.get("separator").get()).build(null)));
        map.put("supercooler", BLOCK_ENTITY_TYPES.register("supercooler", () -> BlockEntityType.Builder.of(SupercoolerEntity::new, PROCESSOR_MAP.get("supercooler").get()).build(null)));
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

    private static Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>>> createBatteries() {
        Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>>> map = new HashMap<>();
        map.put(0, BLOCK_ENTITY_TYPES.register("basic_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 0), BATTERY_MAP.get("basic_voltaic_pile").get()).build(null)));
        map.put(1, BLOCK_ENTITY_TYPES.register("advanced_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 1), BATTERY_MAP.get("advanced_voltaic_pile").get()).build(null)));
        map.put(2, BLOCK_ENTITY_TYPES.register("du_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 2), BATTERY_MAP.get("du_voltaic_pile").get()).build(null)));
        map.put(3, BLOCK_ENTITY_TYPES.register("elite_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 3), BATTERY_MAP.get("elite_voltaic_pile").get()).build(null)));
        map.put(10, BLOCK_ENTITY_TYPES.register("basic_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 10), BATTERY_MAP.get("basic_lithium_ion_battery").get()).build(null)));
        map.put(11, BLOCK_ENTITY_TYPES.register("advanced_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 11), BATTERY_MAP.get("advanced_lithium_ion_battery").get()).build(null)));
        map.put(12, BLOCK_ENTITY_TYPES.register("du_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 12), BATTERY_MAP.get("du_lithium_ion_battery").get()).build(null)));
        map.put(13, BLOCK_ENTITY_TYPES.register("elite_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 13), BATTERY_MAP.get("elite_lithium_ion_battery").get()).build(null)));
        return map;
    }

//    private static Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>>> createTurbines() {
//        Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>>> map = new HashMap<>();
//        map.put(0, BLOCK_ENTITY_TYPES.register("basic_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 0), BATTERY_MAP.get("basic_voltaic_pile").get()).build(null)));
//        map.put(1, BLOCK_ENTITY_TYPES.register("advanced_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 1), BATTERY_MAP.get("advanced_voltaic_pile").get()).build(null)));
//        map.put(2, BLOCK_ENTITY_TYPES.register("du_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 2), BATTERY_MAP.get("du_voltaic_pile").get()).build(null)));
//        map.put(3, BLOCK_ENTITY_TYPES.register("elite_voltaic_pile", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 3), BATTERY_MAP.get("elite_voltaic_pile").get()).build(null)));
//        map.put(10, BLOCK_ENTITY_TYPES.register("basic_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 10), BATTERY_MAP.get("basic_lithium_ion_battery").get()).build(null)));
//        map.put(11, BLOCK_ENTITY_TYPES.register("advanced_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 11), BATTERY_MAP.get("advanced_lithium_ion_battery").get()).build(null)));
//        map.put(12, BLOCK_ENTITY_TYPES.register("du_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 12), BATTERY_MAP.get("du_lithium_ion_battery").get()).build(null)));
//        map.put(13, BLOCK_ENTITY_TYPES.register("elite_lithium_ion_battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, 13), BATTERY_MAP.get("elite_lithium_ion_battery").get()).build(null)));
//        return map;
//    }

    public static void init() {
    }
}