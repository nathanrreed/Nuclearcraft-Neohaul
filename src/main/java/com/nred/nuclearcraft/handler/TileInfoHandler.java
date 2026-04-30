package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block.tile.info.SimpleTileInfoBlock;
import com.nred.nuclearcraft.block.tile.info.TileInfoBlock;
import com.nred.nuclearcraft.block_entity.UniversalBinEntity;
import com.nred.nuclearcraft.block_entity.dummy.MachineInterfaceEntity;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCellPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionIrradiatorPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.block_entity.generator.DecayGeneratorEntity;
import com.nred.nuclearcraft.block_entity.generator.TileSolarPanel;
import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.block_entity.passive.TilePassive;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorBlockInfo;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicUpgradableProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.radiation.GeigerCounterEntity;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.menu.multiblock.controller.*;
import com.nred.nuclearcraft.menu.multiblock.port.FissionCellPortMenu;
import com.nred.nuclearcraft.menu.multiblock.port.FissionHeaterPortMenu;
import com.nred.nuclearcraft.menu.multiblock.port.FissionIrradiatorPortMenu;
import com.nred.nuclearcraft.menu.multiblock.port.FissionVesselPortMenu;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.ModCheck;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Map;

import static com.nred.nuclearcraft.config.NCConfig.processor_power;
import static com.nred.nuclearcraft.config.NCConfig.processor_time;
import static com.nred.nuclearcraft.util.ContainerInfoHelper.bigSlot;
import static com.nred.nuclearcraft.util.ContainerInfoHelper.standardSlot;

public class TileInfoHandler {
    public static final Object2ObjectMap<String, TileInfoBlock<?>> BLOCK_TILE_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
    public static final Object2ObjectMap<String, TileContainerInfo<?>> TILE_CONTAINER_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
    public static BlockEntityType.BlockEntitySupplier<? extends UniversalBinEntity> UNIVERSAL_BIN_ENTITY_FACTORY;

    public static void preInit() {
        registerBlockTileInfo(new SimpleTileInfoBlock<>("machine_interface", MachineInterfaceEntity.class, MachineInterfaceEntity::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("decay_generator", DecayGeneratorEntity.class, DecayGeneratorEntity::new));

        if (ModCheck.mekanismLoaded()) {
            UNIVERSAL_BIN_ENTITY_FACTORY = com.nred.nuclearcraft.block_entity.ChemicalUniversalBinEntity::new;
            registerBlockTileInfo(new SimpleTileInfoBlock<>("bin", com.nred.nuclearcraft.block_entity.ChemicalUniversalBinEntity.class, com.nred.nuclearcraft.block_entity.ChemicalUniversalBinEntity::new));
        } else {
            UNIVERSAL_BIN_ENTITY_FACTORY = UniversalBinEntity::new;
            registerBlockTileInfo(new SimpleTileInfoBlock<>("bin", UniversalBinEntity.class, UniversalBinEntity::new));
        }

        registerBlockTileInfo(new SimpleTileInfoBlock<>("solar_panel_basic", TileSolarPanel.Basic.class, TileSolarPanel.Basic::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("solar_panel_advanced", TileSolarPanel.Advanced.class, TileSolarPanel.Advanced::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("solar_panel_du", TileSolarPanel.DU.class, TileSolarPanel.DU::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("solar_panel_elite", TileSolarPanel.Elite.class, TileSolarPanel.Elite::new));

        registerBlockTileInfo(new SimpleTileInfoBlock<>("cobblestone_generator", TilePassive.CobblestoneGenerator.class, TilePassive.CobblestoneGenerator::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("cobblestone_generator_compact", TilePassive.CobblestoneGeneratorCompact.class, TilePassive.CobblestoneGeneratorCompact::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("cobblestone_generator_dense", TilePassive.CobblestoneGeneratorDense.class, TilePassive.CobblestoneGeneratorDense::new));

        registerBlockTileInfo(new SimpleTileInfoBlock<>("water_source", TilePassive.WaterSource.class, TilePassive.WaterSource::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("water_source_compact", TilePassive.WaterSourceCompact.class, TilePassive.WaterSourceCompact::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("water_source_dense", TilePassive.WaterSourceDense.class, TilePassive.WaterSourceDense::new));

        registerBlockTileInfo(new SimpleTileInfoBlock<>("nitrogen_collector", TilePassive.NitrogenCollector.class, TilePassive.NitrogenCollector::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("nitrogen_collector_compact", TilePassive.NitrogenCollectorCompact.class, TilePassive.NitrogenCollectorCompact::new));
        registerBlockTileInfo(new SimpleTileInfoBlock<>("nitrogen_collector_dense", TilePassive.NitrogenCollectorDense.class, TilePassive.NitrogenCollectorDense::new));

        registerBlockTileInfo(new SimpleTileInfoBlock<>("geiger_block", GeigerCounterEntity.class, GeigerCounterEntity::new));

        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("manufactory", ManufactoryEntity.class, ManufactoryEntity::new, ManufactoryMenu::new).setParticles("crit", "reddust").setDefaultProcessTime(() -> processor_time[0]).setDefaultProcessPower(() -> processor_power[0]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("separator", SeparatorEntity.class, SeparatorEntity::new, SeparatorMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[1]).setDefaultProcessPower(() -> processor_power[1]).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31), bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("decay_hastener", DecayHastenerEntity.class, DecayHastenerEntity::new, DecayHastenerMenu::new).setParticles("reddust").setDefaultProcessTime(() -> processor_time[2]).setDefaultProcessPower(() -> processor_power[2]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("fuel_reprocessor", FuelReprocessorEntity.class, FuelReprocessorEntity::new, FuelReprocessorMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[3]).setDefaultProcessPower(() -> processor_power[3]).standardExtend(0, 12).setItemInputSlots(standardSlot(30, 41)).setItemOutputSlots(standardSlot(86, 31), standardSlot(106, 31), standardSlot(126, 31), standardSlot(146, 31), standardSlot(86, 51), standardSlot(106, 51), standardSlot(126, 51), standardSlot(146, 51)).setProgressBarGuiXYWHUV(48, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("alloy_furnace", AlloyFurnaceEntity.class, AlloyFurnaceEntity::new, AlloyFurnaceMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[4]).setDefaultProcessPower(() -> processor_power[4]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("fluid_infuser", InfuserEntity.class, InfuserEntity::new, InfuserMenu::new).setParticles("portal", "reddust").setDefaultProcessTime(() -> processor_time[5]).setDefaultProcessPower(() -> processor_power[5]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("melter", MelterEntity.class, MelterEntity::new, MelterMenu::new).setParticles("flame", "lava").setDefaultProcessTime(() -> processor_time[6]).setDefaultProcessPower(() -> processor_power[6]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("supercooler", SupercoolerEntity.class, SupercoolerEntity::new, SupercoolerMenu::new).setParticles("smoke", "snowshovel").setDefaultProcessTime(() -> processor_time[7]).setDefaultProcessPower(() -> processor_power[7]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("electrolyzer", ElectrolyzerEntity.class, ElectrolyzerEntity::new, ElectrolyzerMenu::new).setParticles("reddust", "splash").setDefaultProcessTime(() -> processor_time[8]).setDefaultProcessPower(() -> processor_power[8]).setLosesProgress(true).standardExtend(0, 12).setFluidInputSlots(standardSlot(50, 41)).setFluidOutputSlots(standardSlot(106, 31), standardSlot(126, 31), standardSlot(106, 51), standardSlot(126, 51)).setProgressBarGuiXYWHUV(68, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("assembler", AssemblerEntity.class, AssemblerEntity::new, AssemblerMenu::new).setParticles("crit", "smoke").setDefaultProcessTime(() -> processor_time[9]).setDefaultProcessPower(() -> processor_power[9]).standardExtend(0, 12).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31), standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("ingot_former", IngotFormerEntity.class, IngotFormerEntity::new, IngotFormerMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[10]).setDefaultProcessPower(() -> processor_power[10]).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("pressurizer", PressurizerEntity.class, PressurizerEntity::new, PressurizerMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[11]).setDefaultProcessPower(() -> processor_power[11]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("chemical_reactor", ChemicalReactorEntity.class, ChemicalReactorEntity::new, ChemicalReactorMenu::new).setParticles("reddust").setDefaultProcessTime(() -> processor_time[12]).setDefaultProcessPower(() -> processor_power[12]).setLosesProgress(true).setFluidInputSlots(standardSlot(32, 35), standardSlot(52, 35)).setFluidOutputSlots(bigSlot(108, 31), bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("fluid_mixer", SaltMixerEntity.class, SaltMixerEntity::new, SaltMixerMenu::new).setParticles("endRod", "reddust").setDefaultProcessTime(() -> processor_time[13]).setDefaultProcessPower(() -> processor_power[13]).setFluidInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("crystallizer", CrystallizerEntity.class, CrystallizerEntity::new, CrystallizerMenu::new).setParticles("depthsuspend").setDefaultProcessTime(() -> processor_time[14]).setDefaultProcessPower(() -> processor_power[14]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("fluid_enricher", EnricherEntity.class, EnricherEntity::new, EnricherMenu::new).setParticles("depthsuspend", "splash").setDefaultProcessTime(() -> processor_time[15]).setDefaultProcessPower(() -> processor_power[15]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("fluid_extractor", ExtractorEntity.class, ExtractorEntity::new, ExtractorMenu::new).setParticles("depthsuspend", "reddust").setDefaultProcessTime(() -> processor_time[16]).setDefaultProcessPower(() -> processor_power[16]).setLosesProgress(true).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("centrifuge", CentrifugeEntity.class, CentrifugeEntity::new, CentrifugeMenu::new).setParticles("depthsuspend", "endRod").setDefaultProcessTime(() -> processor_time[17]).setDefaultProcessPower(() -> processor_power[17]).standardExtend(0, 12).setFluidInputSlots(standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("rock_crusher", RockCrusherEntity.class, RockCrusherEntity::new, RockCrusherMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[18]).setDefaultProcessPower(() -> processor_power[18]).setItemInputSlots(standardSlot(38, 35)).setItemOutputSlots(standardSlot(94, 35), standardSlot(114, 35), standardSlot(134, 35)).setProgressBarGuiXYWHUV(56, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("electric_furnace", ElectricFurnaceEntity.class, ElectricFurnaceEntity::new, ElectricFurnaceMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[19]).setDefaultProcessPower(() -> processor_power[19]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));

        registerProcessorInfo(new BasicProcessorContainerInfoBuilder<>("radiation_scrubber", RadiationScrubberEntity.class, RadiationScrubberEntity::new, RadiationScrubberMenu::new).setDefaultProcessPower(() -> 1).setConsumesInputs(true).setItemInputSlots(standardSlot(32, 35)).setFluidInputSlots(standardSlot(52, 35)).setItemOutputSlots(bigSlot(108, 31)).setFluidOutputSlots(bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 35, 37, 16, 176, 3).setMachineConfigGuiXY(-1, -1).setRedstoneControlGuiXY(27, 63));

        registerContainerInfo(new TileContainerInfo<>("electrolyzer_controller", ElectrolyzerControllerEntity.class, ElectrolyzerControllerMenu::new));
        registerContainerInfo(new TileContainerInfo<>("distiller_controller", DistillerControllerEntity.class, DistillerControllerMenu::new));
        registerContainerInfo(new TileContainerInfo<>("infiltrator_controller", InfiltratorControllerEntity.class, InfiltratorControllerMenu::new));
        registerContainerInfo(new TileContainerInfo<>("heat_exchanger_controller", HeatExchangerControllerEntity.class, HeatExchangerControllerMenu::new));
        registerContainerInfo(new TileContainerInfo<>("condenser_controller", CondenserControllerEntity.class, CondenserControllerMenu::new));

        registerContainerInfo(new TileContainerInfo<>("solid_fission_controller", SolidFissionControllerEntity.class, SolidFissionControllerMenu::new));
        registerContainerInfo(new TileContainerInfo<>("salt_fission_controller", SaltFissionControllerEntity.class, SaltFissionControllerMenu::new));
        registerContainerInfo(new TileContainerInfo<>("turbine_controller", TurbineControllerEntity.class, TurbineControllerMenu::new));

        registerContainerInfo(new BasicProcessorContainerInfoBuilder<>("fission_irradiator", FissionIrradiatorEntity.class, FissionIrradiatorEntity::new, FissionIrradiatorMenu::new).setConsumesInputs(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle().setStandardJeiAlternateTexture().buildContainerInfo());
        registerContainerInfo(new BasicProcessorContainerInfoBuilder<>("solid_fission_cell", SolidFissionCellEntity.class, SolidFissionCellEntity::new, SolidFissionCellMenu::new).setRecipeHandlerName("solid_fission").setConsumesInputs(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle().buildContainerInfo());
        registerContainerInfo(new BasicProcessorContainerInfoBuilder<>("salt_fission_vessel", SaltFissionVesselEntity.class, SaltFissionVesselEntity::new, SaltFissionVesselMenu::new).setRecipeHandlerName("salt_fission").setConsumesInputs(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle().buildContainerInfo());
        registerContainerInfo(new BasicProcessorContainerInfoBuilder<>("salt_fission_heater", SaltFissionHeaterEntity.class, (a, b) -> {
            throw new RuntimeException("Wrong method used : salt_fission_heater");
        }, SaltFissionHeaterMenu::new).setRecipeHandlerName("coolant_heater").setConsumesInputs(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle().buildContainerInfo());

        registerContainerInfo(new TileContainerInfo<>("fission_irradiator_port", FissionIrradiatorPortEntity.class, FissionIrradiatorPortMenu::new));
        registerContainerInfo(new TileContainerInfo<>("fission_cell_port", FissionCellPortEntity.class, FissionCellPortMenu::new));
        registerContainerInfo(new TileContainerInfo<>("fission_vessel_port", FissionVesselPortEntity.class, FissionVesselPortMenu::new));
        registerContainerInfo(new TileContainerInfo<>("fission_heater_port", FissionHeaterPortEntity.class, FissionHeaterPortMenu::new));

//        registerContainerInfo(new BasicProcessorContainerInfoBuilder<>("fission_cooler", FissionCoolerEntity.class, FissionCoolerEntity::new, FissionCoolerMenu::new).setRecipeHandlerName("fission_cooler").setConsumesInputs(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle().buildContainerInfo());
//        registerContainerInfo(new TileContainerInfo<>("fission_cooler_port", FissionCoolerPortEntity.class, FissionCoolerPortMenu::new));
    }

    public static void init() {
    }

    public static <T> void register(Map<String, T> map, String name, T value) {
        T prev = map.put(name, value);
        if (prev != null) {
            throw new IllegalArgumentException("Registry name \"" + name + "\" already taken for type \"" + value.getClass().getSimpleName() + "\"!");
        }
    }

    public static void registerBlockTileInfo(TileInfoBlock<?> info) {
        register(BLOCK_TILE_INFO_MAP, info.name, info);
    }

    public static void registerContainerInfo(TileContainerInfo<?> info) {
        register(TILE_CONTAINER_INFO_MAP, info.name, info);
    }

    public static void registerProcessorInfo(ProcessorContainerInfoBuilder<?, ?, ?, ?> builder) {
        registerBlockTileInfo(builder.buildBlockInfo());
        registerContainerInfo(builder.buildContainerInfo());
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends SimpleTileInfoBlock<TILE>> INFO getBlockSimpleTileInfo(String name) {
        return (INFO) BLOCK_TILE_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends ProcessorBlockInfo<TILE>> INFO getProcessorBlockInfo(String name) {
        return (INFO) BLOCK_TILE_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends TileContainerInfo<TILE>> INFO getTileContainerInfo(String name) {
        return (INFO) TILE_CONTAINER_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> INFO getProcessorContainerInfo(String name) {
        return (INFO) TILE_CONTAINER_INFO_MAP.get(name);
    }
}