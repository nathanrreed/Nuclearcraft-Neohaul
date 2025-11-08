package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block_entity.UniversalBinEntity;
import com.nred.nuclearcraft.block_entity.battery.BatteryEntity;
import com.nred.nuclearcraft.block_entity.dummy.MachineInterfaceEntity;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.manager.FissionShieldManagerEntity;
import com.nred.nuclearcraft.block_entity.fission.manager.FissionSourceManagerEntity;
import com.nred.nuclearcraft.block_entity.fission.port.*;
import com.nred.nuclearcraft.block_entity.generator.DecayGeneratorEntity;
import com.nred.nuclearcraft.block_entity.generator.TileSolarPanel;
import com.nred.nuclearcraft.block_entity.hx.*;
import com.nred.nuclearcraft.block_entity.machine.*;
import com.nred.nuclearcraft.block_entity.passive.TilePassive;
import com.nred.nuclearcraft.block_entity.processor.NuclearFurnaceEntity;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.*;
import com.nred.nuclearcraft.block_entity.rtg.RTGEntity;
import com.nred.nuclearcraft.block_entity.turbine.*;
import com.nred.nuclearcraft.multiblock.battery.BatteryType;
import com.nred.nuclearcraft.multiblock.fisson.FissionNeutronShieldType;
import com.nred.nuclearcraft.multiblock.fisson.FissionSourceType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterPortType;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import com.nred.nuclearcraft.multiblock.fisson.solid.FissionHeatSinkType;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeType;
import com.nred.nuclearcraft.multiblock.rtg.RTGType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeType;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorStatorType;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.info.Names.COOLANTS;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.Registers.BLOCK_ENTITY_TYPES;

public class BlockEntityRegistration {
    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BasicUpgradableEnergyProcessorEntity<?>>>> PROCESSOR_ENTITY_TYPE = createProcessors();
    public static final Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileSolarPanel>>> SOLAR_PANEL_ENTITY_TYPE = createSolarPanels();

    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractTurbineEntity>>> TURBINE_ENTITY_TYPE = createTurbine();
    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractFissionEntity>>> FISSION_ENTITY_TYPE = createFission();
    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractHeatExchangerEntity>>> HX_ENTITY_TYPE = createHeatExchanger();

    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> MACHINE_ENTITY_TYPE = createMachine();

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends UniversalBinEntity>> UNIVERSAL_BIN_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("universal_bin", () -> BlockEntityType.Builder.of(UniversalBinEntity::new, UNIVERSAL_BIN.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends MachineInterfaceEntity>> MACHINE_INTERFACE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("machine_interface", () -> BlockEntityType.Builder.of(MachineInterfaceEntity::new, MACHINE_INTERFACE.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends DecayGeneratorEntity>> DECAY_GENERATOR_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("decay_generator", () -> BlockEntityType.Builder.of(DecayGeneratorEntity::new, DECAY_GENERATOR.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.CobblestoneGenerator>> COBBLESTONE_GENERATOR_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("cobblestone_generator", () -> BlockEntityType.Builder.of(TilePassive.CobblestoneGenerator::new, COLLECTOR_MAP.get("cobblestone_generator").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.CobblestoneGeneratorCompact>> COBBLESTONE_GENERATOR_COMPACT_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("cobblestone_generator_compact", () -> BlockEntityType.Builder.of(TilePassive.CobblestoneGeneratorCompact::new, COLLECTOR_MAP.get("cobblestone_generator_compact").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.CobblestoneGeneratorDense>> COBBLESTONE_GENERATOR_DENSE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("cobblestone_generator_dense", () -> BlockEntityType.Builder.of(TilePassive.CobblestoneGeneratorDense::new, COLLECTOR_MAP.get("cobblestone_generator_dense").get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.WaterSource>> WATER_SOURCE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("water_source", () -> BlockEntityType.Builder.of(TilePassive.WaterSource::new, COLLECTOR_MAP.get("water_source").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.WaterSourceCompact>> WATER_SOURCE_COMPACT_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("water_source_compact", () -> BlockEntityType.Builder.of(TilePassive.WaterSourceCompact::new, COLLECTOR_MAP.get("water_source_compact").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.WaterSourceDense>> WATER_SOURCE_DENSE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("water_source_dense", () -> BlockEntityType.Builder.of(TilePassive.WaterSourceDense::new, COLLECTOR_MAP.get("water_source_dense").get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.NitrogenCollector>> NITROGEN_COLLECTOR_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("nitrogen_collector", () -> BlockEntityType.Builder.of(TilePassive.NitrogenCollector::new, COLLECTOR_MAP.get("nitrogen_collector").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.NitrogenCollectorCompact>> NITROGEN_COLLECTOR_COMPACT_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("nitrogen_collector_compact", () -> BlockEntityType.Builder.of(TilePassive.NitrogenCollectorCompact::new, COLLECTOR_MAP.get("nitrogen_collector_compact").get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TilePassive.NitrogenCollectorDense>> NITROGEN_COLLECTOR_DENSE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("nitrogen_collector_dense", () -> BlockEntityType.Builder.of(TilePassive.NitrogenCollectorDense::new, COLLECTOR_MAP.get("nitrogen_collector_dense").get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends NuclearFurnaceEntity>> NUCLEAR_FURNACE_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("nuclear_furnace", () -> BlockEntityType.Builder.of(NuclearFurnaceEntity::new, NUCLEAR_FURNACE.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends RTGEntity>> RTG_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("rtg", () -> BlockEntityType.Builder.of((pos, state) -> new RTGEntity(pos, state, ((RTGType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), RTG_MAP.values().stream().map(DeferredHolder::get).toArray(Block[]::new)).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>> BATTERY_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("battery", () -> BlockEntityType.Builder.of((pos, state) -> new BatteryEntity(pos, state, ((BatteryType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), BATTERY_MAP.values().stream().map(DeferredHolder::get).toArray(Block[]::new)).build(null));

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BasicUpgradableEnergyProcessorEntity<?>>>> createProcessors() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BasicUpgradableEnergyProcessorEntity<?>>>> map = new HashMap<>();
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

    private static Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileSolarPanel>>> createSolarPanels() {
        Map<Integer, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileSolarPanel>>> map = new HashMap<>();
        map.put(0, BLOCK_ENTITY_TYPES.register("solar_panel_basic", () -> BlockEntityType.Builder.of(TileSolarPanel.Basic::new, SOLAR_MAP.get("solar_panel_basic").get()).build(null)));
        map.put(1, BLOCK_ENTITY_TYPES.register("solar_panel_advanced", () -> BlockEntityType.Builder.of(TileSolarPanel.Advanced::new, SOLAR_MAP.get("solar_panel_advanced").get()).build(null)));
        map.put(2, BLOCK_ENTITY_TYPES.register("solar_panel_du", () -> BlockEntityType.Builder.of(TileSolarPanel.DU::new, SOLAR_MAP.get("solar_panel_du").get()).build(null)));
        map.put(3, BLOCK_ENTITY_TYPES.register("solar_panel_elite", () -> BlockEntityType.Builder.of(TileSolarPanel.Elite::new, SOLAR_MAP.get("solar_panel_elite").get()).build(null)));
        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractTurbineEntity>>> createTurbine() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractTurbineEntity>>> map = new HashMap<>();
        map.put("controller", BLOCK_ENTITY_TYPES.register("turbine_controller", () -> BlockEntityType.Builder.of(TurbineControllerEntity::new, TURBINE_MAP.get("turbine_controller").get()).build(null)));
        map.put("casing", BLOCK_ENTITY_TYPES.register("turbine_casing", () -> BlockEntityType.Builder.of(TurbineCasingEntity::new, TURBINE_MAP.get("turbine_casing").get()).build(null)));
        map.put("glass", BLOCK_ENTITY_TYPES.register("turbine_glass", () -> BlockEntityType.Builder.of(TurbineGlassEntity::new, TURBINE_MAP.get("turbine_glass").get()).build(null)));
        map.put("rotor_bearing", BLOCK_ENTITY_TYPES.register("turbine_rotor_bearing", () -> BlockEntityType.Builder.of(TurbineRotorBearingEntity::new, TURBINE_MAP.get("turbine_rotor_bearing").get()).build(null)));
        map.put("rotor_shaft", BLOCK_ENTITY_TYPES.register("turbine_rotor_shaft", () -> BlockEntityType.Builder.of(TurbineRotorShaftEntity::new, TURBINE_MAP.get("turbine_rotor_shaft").get()).build(null)));
        map.put("rotor_blade", BLOCK_ENTITY_TYPES.register("turbine_rotor_blade", () -> BlockEntityType.Builder.of((pos, state) -> new TurbineRotorBladeEntity(pos, state, ((TurbineRotorBladeType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), TURBINE_MAP.get("steel_turbine_rotor_blade").get(), TURBINE_MAP.get("extreme_alloy_turbine_rotor_blade").get(), TURBINE_MAP.get("sic_turbine_rotor_blade").get()).build(null)));
        map.put("rotor_stator", BLOCK_ENTITY_TYPES.register("turbine_rotor_stator", () -> BlockEntityType.Builder.of((pos, state) -> new TurbineRotorStatorEntity(pos, state, ((TurbineRotorStatorType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), TURBINE_MAP.get("standard_turbine_rotor_stator").get()).build(null)));
        map.put("outlet", BLOCK_ENTITY_TYPES.register("turbine_outlet", () -> BlockEntityType.Builder.of(TurbineOutletEntity::new, TURBINE_MAP.get("turbine_outlet").get()).build(null)));
        map.put("inlet", BLOCK_ENTITY_TYPES.register("turbine_inlet", () -> BlockEntityType.Builder.of(TurbineInletEntity::new, TURBINE_MAP.get("turbine_inlet").get()).build(null)));
        map.put("dynamo", BLOCK_ENTITY_TYPES.register("turbine_dynamo", () -> BlockEntityType.Builder.of((pos, state) -> new TurbineDynamoCoilEntity(pos, state, ((TurbineDynamoCoilType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), TURBINE_MAP.get("magnesium_turbine_dynamo_coil").get(), TURBINE_MAP.get("beryllium_turbine_dynamo_coil").get(), TURBINE_MAP.get("aluminum_turbine_dynamo_coil").get(), TURBINE_MAP.get("gold_turbine_dynamo_coil").get(), TURBINE_MAP.get("copper_turbine_dynamo_coil").get(), TURBINE_MAP.get("silver_turbine_dynamo_coil").get()).build(null)));
        map.put("coil_connector", BLOCK_ENTITY_TYPES.register("turbine_coil_connector", () -> BlockEntityType.Builder.of(TurbineCoilConnectorEntity::new, TURBINE_MAP.get("turbine_coil_connector").get()).build(null)));
        map.put("computer_port", BLOCK_ENTITY_TYPES.register("turbine_computer_port", () -> BlockEntityType.Builder.of(TurbineComputerPortEntity::new, TURBINE_MAP.get("turbine_computer_port").get()).build(null)));
        map.put("redstone_port", BLOCK_ENTITY_TYPES.register("turbine_redstone_port", () -> BlockEntityType.Builder.of(TurbineRedstonePortEntity::new, TURBINE_MAP.get("turbine_redstone_port").get()).build(null)));
        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractHeatExchangerEntity>>> createHeatExchanger() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractHeatExchangerEntity>>> map = new HashMap<>();
        map.put("heat_exchanger_controller", BLOCK_ENTITY_TYPES.register("heat_exchanger_controller", () -> BlockEntityType.Builder.of(HeatExchangerControllerEntity::new, HX_MAP.get("heat_exchanger_controller").get()).build(null)));
        map.put("condenser_controller", BLOCK_ENTITY_TYPES.register("condenser_controller", () -> BlockEntityType.Builder.of(CondenserControllerEntity::new, HX_MAP.get("condenser_controller").get()).build(null)));
        map.put("casing", BLOCK_ENTITY_TYPES.register("hx_casing", () -> BlockEntityType.Builder.of(HeatExchangerCasingEntity::new, HX_MAP.get("heat_exchanger_casing").get()).build(null)));
        map.put("glass", BLOCK_ENTITY_TYPES.register("hx_glass", () -> BlockEntityType.Builder.of(HeatExchangerGlassEntity::new, HX_MAP.get("heat_exchanger_glass").get()).build(null)));
        map.put("tube", BLOCK_ENTITY_TYPES.register("hx_tube", () -> BlockEntityType.Builder.of((pos, state) -> new HeatExchangerTubeEntity(pos, state, ((HeatExchangerTubeType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), HX_MAP.get("copper_heat_exchanger_tube").get(), HX_MAP.get("hard_carbon_heat_exchanger_tube").get(), HX_MAP.get("thermoconducting_alloy_heat_exchanger_tube").get()).build(null)));
        map.put("baffle", BLOCK_ENTITY_TYPES.register("hx_baffle", () -> BlockEntityType.Builder.of(HeatExchangerBaffleEntity::new, HX_MAP.get("heat_exchanger_shell_baffle").get()).build(null)));
        map.put("inlet", BLOCK_ENTITY_TYPES.register("hx_inlet", () -> BlockEntityType.Builder.of(HeatExchangerInletEntity::new, HX_MAP.get("heat_exchanger_inlet").get()).build(null)));
        map.put("outlet", BLOCK_ENTITY_TYPES.register("hx_outlet", () -> BlockEntityType.Builder.of(HeatExchangerOutletEntity::new, HX_MAP.get("heat_exchanger_outlet").get()).build(null)));
        map.put("redstone_port", BLOCK_ENTITY_TYPES.register("hx_redstone_port", () -> BlockEntityType.Builder.of(HeatExchangerRedstonePortEntity::new, HX_MAP.get("heat_exchanger_redstone_port").get()).build(null)));
        map.put("computer_port", BLOCK_ENTITY_TYPES.register("hx_computer_port", () -> BlockEntityType.Builder.of(HeatExchangerComputerPortEntity::new, HX_MAP.get("heat_exchanger_computer_port").get()).build(null)));
        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractFissionEntity>>> createFission() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractFissionEntity>>> map = new HashMap<>();
        map.put("solid_fuel_fission_controller", BLOCK_ENTITY_TYPES.register("solid_fuel_fission_controller", () -> BlockEntityType.Builder.of(SolidFissionControllerEntity::new, FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").get()).build(null)));
        map.put("molten_salt_fission_controller", BLOCK_ENTITY_TYPES.register("molten_salt_fission_controller", () -> BlockEntityType.Builder.of(SaltFissionControllerEntity::new, FISSION_REACTOR_MAP.get("molten_salt_fission_controller").get()).build(null)));
        map.put("casing", BLOCK_ENTITY_TYPES.register("fission_casing", () -> BlockEntityType.Builder.of(FissionCasingEntity::new, FISSION_REACTOR_MAP.get("fission_casing").get()).build(null)));
        map.put("glass", BLOCK_ENTITY_TYPES.register("fission_glass", () -> BlockEntityType.Builder.of(FissionGlassEntity::new, FISSION_REACTOR_MAP.get("fission_glass").get()).build(null)));
        map.put("vent", BLOCK_ENTITY_TYPES.register("fission_vent", () -> BlockEntityType.Builder.of(FissionVentEntity::new, FISSION_REACTOR_MAP.get("fission_vent").get()).build(null)));
        map.put("conductor", BLOCK_ENTITY_TYPES.register("fission_conductor", () -> BlockEntityType.Builder.of(FissionConductorEntity::new, FISSION_REACTOR_MAP.get("fission_conductor").get()).build(null)));

        map.put("shield", BLOCK_ENTITY_TYPES.register("fission_shield", () -> BlockEntityType.Builder.of((pos, state) -> new FissionShieldEntity(pos, state, ((FissionNeutronShieldType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), FISSION_REACTOR_MAP.get("boron_silver_shield").get()).build(null)));
        map.put("source", BLOCK_ENTITY_TYPES.register("fission_source", () -> BlockEntityType.Builder.of((pos, state) -> new FissionSourceEntity(pos, state, ((FissionSourceType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), FISSION_REACTOR_MAP.get("radium_beryllium_source").get(), FISSION_REACTOR_MAP.get("polonium_beryllium_source").get(), FISSION_REACTOR_MAP.get("californium_source").get()).build(null)));

        map.put("cell", BLOCK_ENTITY_TYPES.register("fission_cell", () -> BlockEntityType.Builder.of(SolidFissionCellEntity::new, FISSION_REACTOR_MAP.get("fission_fuel_cell").get()).build(null)));
        map.put("cell_port", BLOCK_ENTITY_TYPES.register("fission_cell_port", () -> BlockEntityType.Builder.of(FissionCellPortEntity::new, FISSION_REACTOR_MAP.get("fission_fuel_cell_port").get()).build(null)));

        map.put("cooler", BLOCK_ENTITY_TYPES.register("fission_cooler", () -> BlockEntityType.Builder.of(FissionCoolerEntity::new, FISSION_REACTOR_MAP.get("fission_cooler").get()).build(null)));
        map.put("cooler_port", BLOCK_ENTITY_TYPES.register("fission_cooler_port", () -> BlockEntityType.Builder.of(FissionCoolerPortEntity::new, FISSION_REACTOR_MAP.get("fission_cooler_port").get()).build(null)));

        map.put("irradiator", BLOCK_ENTITY_TYPES.register("fission_irradiator", () -> BlockEntityType.Builder.of(FissionIrradiatorEntity::new, FISSION_REACTOR_MAP.get("fission_irradiator").get()).build(null)));
        map.put("irradiator_port", BLOCK_ENTITY_TYPES.register("fission_irradiator_port", () -> BlockEntityType.Builder.of(FissionIrradiatorPortEntity::new, FISSION_REACTOR_MAP.get("fission_irradiator_port").get()).build(null)));

        map.put("vessel", BLOCK_ENTITY_TYPES.register("fission_vessel", () -> BlockEntityType.Builder.of(SaltFissionVesselEntity::new, FISSION_REACTOR_MAP.get("fission_fuel_vessel").get()).build(null)));
        map.put("vessel_port", BLOCK_ENTITY_TYPES.register("fission_vessel_port", () -> BlockEntityType.Builder.of(FissionVesselPortEntity::new, FISSION_REACTOR_MAP.get("fission_fuel_vessel_port").get()).build(null)));

        map.put("heat_sink", BLOCK_ENTITY_TYPES.register("fission_heat_sink", () -> BlockEntityType.Builder.of((pos, state) -> new SolidFissionHeatSinkEntity(pos, state, ((FissionHeatSinkType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), Stream.concat(COOLANTS.stream(), Stream.of("water")).map(name -> FISSION_REACTOR_MAP.get(name + "_fission_heat_sink").get()).toArray(Block[]::new)).build(null)));
        map.put("coolant_heater", BLOCK_ENTITY_TYPES.register("fission_coolant_heater", () -> BlockEntityType.Builder.of((pos, state) -> new SaltFissionHeaterEntity(pos, state, ((FissionCoolantHeaterType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), Stream.concat(COOLANTS.stream(), Stream.of("standard")).map(name -> FISSION_REACTOR_MAP.get(name + "_fission_coolant_heater").get()).toArray(Block[]::new)).build(null)));
        map.put("coolant_heater_port", BLOCK_ENTITY_TYPES.register("fission_coolant_heater_port", () -> BlockEntityType.Builder.of((pos, state) -> new FissionHeaterPortEntity(pos, state, ((FissionCoolantHeaterPortType) ((MultiblockPartBlock<?, ?>) state.getBlock()).getMultiblockVariant().get())), Stream.concat(COOLANTS.stream(), Stream.of("standard")).map(name -> FISSION_REACTOR_MAP.get(name + "_fission_coolant_heater_port").get()).toArray(Block[]::new)).build(null)));

        map.put("monitor", BLOCK_ENTITY_TYPES.register("fission_monitor", () -> BlockEntityType.Builder.of(FissionMonitorEntity::new, FISSION_REACTOR_MAP.get("fission_monitor").get()).build(null)));
        map.put("shield_manager", BLOCK_ENTITY_TYPES.register("fission_shield_manager", () -> BlockEntityType.Builder.of(FissionShieldManagerEntity::new, FISSION_REACTOR_MAP.get("fission_shield_manager").get()).build(null)));
        map.put("source_manager", BLOCK_ENTITY_TYPES.register("fission_source_manager", () -> BlockEntityType.Builder.of(FissionSourceManagerEntity::new, FISSION_REACTOR_MAP.get("fission_source_manager").get()).build(null)));

        map.put("computer_port", BLOCK_ENTITY_TYPES.register("fission_computer_port", () -> BlockEntityType.Builder.of(FissionComputerPortEntity::new, FISSION_REACTOR_MAP.get("fission_computer_port").get()).build(null)));
        map.put("power_port", BLOCK_ENTITY_TYPES.register("fission_power_port", () -> BlockEntityType.Builder.of(FissionPowerPortEntity::new, FISSION_REACTOR_MAP.get("fission_power_port").get()).build(null)));

        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> createMachine() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> map = new HashMap<>();
        map.put("computer_port", BLOCK_ENTITY_TYPES.register("machine_computer_port", () -> BlockEntityType.Builder.of(MachineComputerPortEntity::new, MACHINE_MAP.get("large_machine_computer_port").get()).build(null)));
        map.put("frame", BLOCK_ENTITY_TYPES.register("machine_frame", () -> BlockEntityType.Builder.of(MachineFrameEntity::new, MACHINE_MAP.get("large_machine_frame").get()).build(null)));
        map.put("glass", BLOCK_ENTITY_TYPES.register("machine_glass", () -> BlockEntityType.Builder.of(MachineGlassEntity::new, MACHINE_MAP.get("large_machine_glass").get()).build(null)));
        map.put("power_port", BLOCK_ENTITY_TYPES.register("machine_power_port", () -> BlockEntityType.Builder.of(MachinePowerPortEntity::new, MACHINE_MAP.get("large_machine_power_port").get()).build(null)));
        map.put("redstone_port", BLOCK_ENTITY_TYPES.register("machine_redstone_port", () -> BlockEntityType.Builder.of(MachineRedstonePortEntity::new, MACHINE_MAP.get("large_machine_redstone_port").get()).build(null)));
        map.put("process_port", BLOCK_ENTITY_TYPES.register("machine_process_port", () -> BlockEntityType.Builder.of(MachineProcessPortEntity::new, MACHINE_MAP.get("large_machine_process_port").get()).build(null)));
        map.put("reservoir_port", BLOCK_ENTITY_TYPES.register("machine_reservoir_port", () -> BlockEntityType.Builder.of(MachineReservoirPortEntity::new, MACHINE_MAP.get("large_machine_reservoir_port").get()).build(null)));

        // Electrolyzer
        map.put("electrolyzer_controller", BLOCK_ENTITY_TYPES.register("electrolyzer_controller", () -> BlockEntityType.Builder.of(ElectrolyzerControllerEntity::new, MACHINE_MAP.get("electrolyzer_controller").get()).build(null)));
        map.put("electrolyzer_cathode_terminal", BLOCK_ENTITY_TYPES.register("electrolyzer_cathode_terminal", () -> BlockEntityType.Builder.of(ElectrolyzerCathodeTerminalEntity::new, MACHINE_MAP.get("electrolyzer_cathode_terminal").get()).build(null)));
        map.put("electrolyzer_anode_terminal", BLOCK_ENTITY_TYPES.register("electrolyzer_anode_terminal", () -> BlockEntityType.Builder.of(ElectrolyzerAnodeTerminalEntity::new, MACHINE_MAP.get("electrolyzer_anode_terminal").get()).build(null)));

        // Distiller
        map.put("distiller_controller", BLOCK_ENTITY_TYPES.register("distiller_controller", () -> BlockEntityType.Builder.of(DistillerControllerEntity::new, MACHINE_MAP.get("distiller_controller").get()).build(null)));
        map.put("distiller_sieve_tray", BLOCK_ENTITY_TYPES.register("distiller_sieve_tray", () -> BlockEntityType.Builder.of(DistillerSieveTrayEntity::new, MACHINE_MAP.get("distiller_sieve_tray").get()).build(null)));
        map.put("distiller_reflux_unit", BLOCK_ENTITY_TYPES.register("distiller_reflux_unit", () -> BlockEntityType.Builder.of(DistillerRefluxUnitEntity::new, MACHINE_MAP.get("distiller_reflux_unit").get()).build(null)));
        map.put("distiller_reboiling_unit", BLOCK_ENTITY_TYPES.register("distiller_reboiling_unit", () -> BlockEntityType.Builder.of(DistillerReboilingUnitEntity::new, MACHINE_MAP.get("distiller_reboiling_unit").get()).build(null)));
        map.put("distiller_liquid_distributor", BLOCK_ENTITY_TYPES.register("distiller_liquid_distributor", () -> BlockEntityType.Builder.of(DistillerLiquidDistributorEntity::new, MACHINE_MAP.get("distiller_liquid_distributor").get()).build(null)));

        // Infiltrator
        map.put("infiltrator_controller", BLOCK_ENTITY_TYPES.register("infiltrator_controller", () -> BlockEntityType.Builder.of(InfiltratorControllerEntity::new, MACHINE_MAP.get("infiltrator_controller").get()).build(null)));
        map.put("infiltrator_pressure_chamber", BLOCK_ENTITY_TYPES.register("infiltrator_pressure_chamber", () -> BlockEntityType.Builder.of(InfiltratorPressureChamberEntity::new, MACHINE_MAP.get("infiltrator_pressure_chamber").get()).build(null)));
        map.put("infiltrator_heating_unit", BLOCK_ENTITY_TYPES.register("infiltrator_heating_unit", () -> BlockEntityType.Builder.of(InfiltratorHeatingUnitEntity::new, MACHINE_MAP.get("infiltrator_heating_unit").get()).build(null)));

        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> createHeatDistiller() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> map = new HashMap<>();

        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> createElectrolyzer() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> map = new HashMap<>();

        return map;
    }

    private static Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> createInfiltrator() {
        Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends AbstractMachineEntity>>> map = new HashMap<>();

        return map;
    }

    public static void init() {
    }
}