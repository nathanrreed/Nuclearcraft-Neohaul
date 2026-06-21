package com.nred.nuclearcraft.handler;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block.tile.info.BlockEntityInfoBlock;
import com.nred.nuclearcraft.block.tile.info.SimpleTileInfoBlock;
import com.nred.nuclearcraft.block_entity.UniversalBinEntity;
import com.nred.nuclearcraft.block_entity.dummy.MachineInterfaceEntity;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.*;
import com.nred.nuclearcraft.block_entity.generator.DecayGeneratorEntity;
import com.nred.nuclearcraft.block_entity.generator.TileSolarPanel;
import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.DecayPoolControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.block_entity.passive.TilePassive;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.ProcessorEntityImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.UpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorBlockInfo;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilderImpl.BasicProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilderImpl.BasicUpgradableProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.block_entity.radiation.GeigerCounterEntity;
import com.nred.nuclearcraft.block_entity.radiation.RadiationScrubberEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerCategoryInfo;
import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerProcessorCategoryInfo;
import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerSimpleCategoryInfo;
import com.nred.nuclearcraft.compat.recipe_viewer.info.builder.RecipeViewerSimpleCategoryInfoBuilder;
import com.nred.nuclearcraft.menu.multiblock.controller.*;
import com.nred.nuclearcraft.menu.multiblock.port.*;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.util.DataMapHelper;
import com.nred.nuclearcraft.util.ModCheck;
import com.nred.nuclearcraft.util.StreamHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.config.NCConfig.processor_power;
import static com.nred.nuclearcraft.config.NCConfig.processor_time;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.*;
import static com.nred.nuclearcraft.registration.BlockRegistration.*;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.*;
import static com.nred.nuclearcraft.util.ContainerInfoHelper.bigSlot;
import static com.nred.nuclearcraft.util.ContainerInfoHelper.standardSlot;

public class BlockEntityInfoHandler {
    public static final Object2ObjectMap<String, BlockEntityInfoBlock<?>> BLOCK_ENTITY_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
    public static final Object2ObjectMap<String, BlockEntityMenuInfo<?>> BLOCK_ENTITY_MENU_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();
    public static BlockEntityType.BlockEntitySupplier<? extends UniversalBinEntity> UNIVERSAL_BIN_ENTITY_FACTORY;

    public static final Object2ObjectMap<String, RecipeViewerCategoryInfo> RECIPE_VIEWER_CATEGORY_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();

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

        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("manufactory", ManufactoryEntity.class, ManufactoryEntity::new, ManufactoryMenu::new).setParticles("crit", "red_dust").setDefaultProcessTime(() -> processor_time[0]).setDefaultProcessPower(() -> processor_power[0]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("separator", SeparatorEntity.class, SeparatorEntity::new, SeparatorMenu::new).setParticles("red_dust", "smoke").setDefaultProcessTime(() -> processor_time[1]).setDefaultProcessPower(() -> processor_power[1]).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31), bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("decay_hastener", DecayHastenerEntity.class, DecayHastenerEntity::new, DecayHastenerMenu::new).setParticles("red_dust").setDefaultProcessTime(() -> processor_time[2]).setDefaultProcessPower(() -> processor_power[2]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("fuel_reprocessor", FuelReprocessorEntity.class, FuelReprocessorEntity::new, FuelReprocessorMenu::new).setParticles("red_dust", "smoke").setDefaultProcessTime(() -> processor_time[3]).setDefaultProcessPower(() -> processor_power[3]).standardExtend(0, 12).setItemInputSlots(standardSlot(30, 41)).setItemOutputSlots(standardSlot(86, 31), standardSlot(106, 31), standardSlot(126, 31), standardSlot(146, 31), standardSlot(86, 51), standardSlot(106, 51), standardSlot(126, 51), standardSlot(146, 51)).setProgressBarGuiXYWHUV(48, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("alloy_furnace", AlloyFurnaceEntity.class, AlloyFurnaceEntity::new, AlloyFurnaceMenu::new).setParticles("red_dust", "smoke").setDefaultProcessTime(() -> processor_time[4]).setDefaultProcessPower(() -> processor_power[4]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("fluid_infuser", InfuserEntity.class, InfuserEntity::new, InfuserMenu::new).setParticles("portal", "red_dust").setDefaultProcessTime(() -> processor_time[5]).setDefaultProcessPower(() -> processor_power[5]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("melter", MelterEntity.class, MelterEntity::new, MelterMenu::new).setParticles("flame", "lava").setDefaultProcessTime(() -> processor_time[6]).setDefaultProcessPower(() -> processor_power[6]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("supercooler", SupercoolerEntity.class, SupercoolerEntity::new, SupercoolerMenu::new).setParticles("smoke", "snowflake").setDefaultProcessTime(() -> processor_time[7]).setDefaultProcessPower(() -> processor_power[7]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("electrolyzer", ElectrolyzerEntity.class, ElectrolyzerEntity::new, ElectrolyzerMenu::new).setParticles("red_dust", "splash").setDefaultProcessTime(() -> processor_time[8]).setDefaultProcessPower(() -> processor_power[8]).setLosesProgress(true).standardExtend(0, 12).setFluidInputSlots(standardSlot(50, 41)).setFluidOutputSlots(standardSlot(106, 31), standardSlot(126, 31), standardSlot(106, 51), standardSlot(126, 51)).setProgressBarGuiXYWHUV(68, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("assembler", AssemblerEntity.class, AssemblerEntity::new, AssemblerMenu::new).setParticles("crit", "smoke").setDefaultProcessTime(() -> processor_time[9]).setDefaultProcessPower(() -> processor_power[9]).standardExtend(0, 12).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31), standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("ingot_former", IngotFormerEntity.class, IngotFormerEntity::new, IngotFormerMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[10]).setDefaultProcessPower(() -> processor_power[10]).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("pressurizer", PressurizerEntity.class, PressurizerEntity::new, PressurizerMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[11]).setDefaultProcessPower(() -> processor_power[11]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("chemical_reactor", ChemicalReactorEntity.class, ChemicalReactorEntity::new, ChemicalReactorMenu::new).setParticles("red_dust").setDefaultProcessTime(() -> processor_time[12]).setDefaultProcessPower(() -> processor_power[12]).setLosesProgress(true).setFluidInputSlots(standardSlot(32, 35), standardSlot(52, 35)).setFluidOutputSlots(bigSlot(108, 31), bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("fluid_mixer", SaltMixerEntity.class, SaltMixerEntity::new, SaltMixerMenu::new).setParticles("end_rod", "red_dust").setDefaultProcessTime(() -> processor_time[13]).setDefaultProcessPower(() -> processor_power[13]).setFluidInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("crystallizer", CrystallizerEntity.class, CrystallizerEntity::new, CrystallizerMenu::new).setParticles("ominous_spawning").setDefaultProcessTime(() -> processor_time[14]).setDefaultProcessPower(() -> processor_power[14]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("fluid_enricher", EnricherEntity.class, EnricherEntity::new, EnricherMenu::new).setParticles("ominous_spawning", "splash").setDefaultProcessTime(() -> processor_time[15]).setDefaultProcessPower(() -> processor_power[15]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("fluid_extractor", ExtractorEntity.class, ExtractorEntity::new, ExtractorMenu::new).setParticles("ominous_spawning", "red_dust").setDefaultProcessTime(() -> processor_time[16]).setDefaultProcessPower(() -> processor_power[16]).setLosesProgress(true).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("centrifuge", CentrifugeEntity.class, CentrifugeEntity::new, CentrifugeMenu::new).setParticles("ominous_spawning", "end_rod").setDefaultProcessTime(() -> processor_time[17]).setDefaultProcessPower(() -> processor_power[17]).standardExtend(0, 12).setFluidInputSlots(standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("rock_crusher", RockCrusherEntity.class, RockCrusherEntity::new, RockCrusherMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[18]).setDefaultProcessPower(() -> processor_power[18]).setItemInputSlots(standardSlot(38, 35)).setItemOutputSlots(standardSlot(94, 35), standardSlot(114, 35), standardSlot(134, 35)).setProgressBarGuiXYWHUV(56, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorMenuInfoBuilder<>("electric_furnace", ElectricFurnaceEntity.class, ElectricFurnaceEntity::new, ElectricFurnaceMenu::new).setParticles("red_dust", "smoke").setDefaultProcessTime(() -> processor_time[19]).setDefaultProcessPower(() -> processor_power[19]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));

        registerProcessorInfo(new BasicProcessorMenuInfoBuilder<>("radiation_scrubber", RadiationScrubberEntity.class, RadiationScrubberEntity::new, RadiationScrubberMenu::new).setDefaultProcessPower(() -> 1).setConsumesInputs(true).setItemInputSlots(standardSlot(32, 35)).setFluidInputSlots(standardSlot(52, 35)).setItemOutputSlots(bigSlot(108, 31)).setFluidOutputSlots(bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 35, 37, 16, 176, 3).setMachineConfigGuiXY(-1, -1).setRedstoneControlGuiXY(27, 63));

        registerContainerInfo(new BlockEntityMenuInfo<>("electrolyzer_controller", ElectrolyzerControllerEntity.class, ElectrolyzerControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("distiller_controller", DistillerControllerEntity.class, DistillerControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("infiltrator_controller", InfiltratorControllerEntity.class, InfiltratorControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("decay_pool_controller", DecayPoolControllerEntity.class, DecayPoolControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("heat_exchanger_controller", HeatExchangerControllerEntity.class, HeatExchangerControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("condenser_controller", CondenserControllerEntity.class, CondenserControllerMenu::new));

        registerContainerInfo(new BlockEntityMenuInfo<>("pebble_fission_controller", PebbleFissionControllerEntity.class, PebbleFissionControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("solid_fission_controller", SolidFissionControllerEntity.class, SolidFissionControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("salt_fission_controller", SaltFissionControllerEntity.class, SaltFissionControllerMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("turbine_controller", TurbineControllerEntity.class, TurbineControllerMenu::new));

        registerContainerInfo(new BasicProcessorMenuInfoBuilder<>("fission_irradiator", FissionIrradiatorEntity.class, FissionIrradiatorEntity::new, FissionIrradiatorMenu::new).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).buildContainerInfo());
        registerContainerInfo(new BasicProcessorMenuInfoBuilder<>("pebble_fission", PebbleFissionChamberEntity.class, PebbleFissionChamberEntity::new, PebbleFissionChamberMenu::new).setRecipeHandlerName("pebble_fission").setConsumesInputs(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).buildContainerInfo());
        registerContainerInfo(new BasicProcessorMenuInfoBuilder<>("pebble_fission_cooler", PebbleFissionCoolerEntity.class, (a, b) -> {
            throw new RuntimeException("Wrong method used : pebble_fission_cooler");
        }, PebbleFissionCoolerMenu::new).setRecipeHandlerName("salt_cooling").setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildContainerInfo());
        registerContainerInfo(new BasicProcessorMenuInfoBuilder<>("solid_fission", SolidFissionCellEntity.class, SolidFissionCellEntity::new, SolidFissionCellMenu::new).setRecipeHandlerName("solid_fission").setConsumesInputs(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).buildContainerInfo());
        registerContainerInfo(new BasicProcessorMenuInfoBuilder<>("salt_fission", SaltFissionVesselEntity.class, SaltFissionVesselEntity::new, SaltFissionVesselMenu::new).setRecipeHandlerName("salt_fission").setConsumesInputs(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildContainerInfo());
        registerContainerInfo(new BasicProcessorMenuInfoBuilder<>("salt_cooling", SaltFissionHeaterEntity.class, (a, b) -> {
            throw new RuntimeException("Wrong method used : salt_cooling");
        }, SaltFissionHeaterMenu::new).setRecipeHandlerName("coolant_heater").setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).buildContainerInfo());

        registerContainerInfo(new BlockEntityMenuInfo<>("fission_irradiator_port", FissionIrradiatorPortEntity.class, FissionIrradiatorPortMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("fission_chamber_port", FissionChamberPortEntity.class, FissionChamberPortMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("fission_cooler_port", FissionCoolerPortEntity.class, FissionCoolerPortMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("fission_cell_port", FissionCellPortEntity.class, FissionCellPortMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("fission_vessel_port", FissionVesselPortEntity.class, FissionVesselPortMenu::new));
        registerContainerInfo(new BlockEntityMenuInfo<>("fission_heater_port", FissionHeaterPortEntity.class, FissionHeaterPortMenu::new));
    }

    public static void init() {
        if (ModCheck.jeiLoaded() || ModCheck.emiLoaded()) {
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("alloy_furnace", List.of(PROCESSOR_MAP.get("alloy_furnace"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("assembler", List.of(PROCESSOR_MAP.get("assembler"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("centrifuge", List.of(PROCESSOR_MAP.get("centrifuge"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("chemical_reactor", List.of(PROCESSOR_MAP.get("chemical_reactor"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("crystallizer", List.of(PROCESSOR_MAP.get("crystallizer"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("decay_hastener", List.of(PROCESSOR_MAP.get("decay_hastener"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("electric_furnace", List.of(PROCESSOR_MAP.get("electric_furnace"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("electrolyzer", List.of(PROCESSOR_MAP.get("electrolyzer"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("fluid_infuser", List.of(PROCESSOR_MAP.get("fluid_infuser"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("fluid_enricher", List.of(PROCESSOR_MAP.get("fluid_enricher"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("fluid_extractor", List.of(PROCESSOR_MAP.get("fluid_extractor"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("fluid_mixer", List.of(PROCESSOR_MAP.get("fluid_mixer"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("fuel_reprocessor", List.of(PROCESSOR_MAP.get("fuel_reprocessor"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("ingot_former", List.of(PROCESSOR_MAP.get("ingot_former"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("manufactory", List.of(PROCESSOR_MAP.get("manufactory"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("melter", List.of(PROCESSOR_MAP.get("melter"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("pressurizer", List.of(PROCESSOR_MAP.get("pressurizer"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("rock_crusher", List.of(PROCESSOR_MAP.get("rock_crusher"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("separator", List.of(PROCESSOR_MAP.get("separator"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("supercooler", List.of(PROCESSOR_MAP.get("supercooler"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("radiation_scrubber", List.of(RADIATION_SCRUBBER)));

            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("collector", COLLECTOR_MAP.values().stream().toList()).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("decay_generator", List.of(DECAY_GENERATOR)).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));

            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("machine_diaphragm", DataMapHelper.get(MACHINE_DIAPHRAGM_DATA)).setItemInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("machine_sieve_assembly", DataMapHelper.get(MACHINE_SIEVE_ASSEMBLY_DATA)).setItemInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("multiblock_electrolyzer", List.of(MACHINE_MAP.get("electrolyzer_controller"))).standardExtend(0, 12).setItemInputSlots(standardSlot(13, 31), standardSlot(33, 31)).setFluidInputSlots(standardSlot(13, 51), standardSlot(33, 51)).setItemOutputSlots(standardSlot(103, 31), standardSlot(123, 31), standardSlot(103, 51), standardSlot(123, 51)).setFluidOutputSlots(standardSlot(143, 31), standardSlot(163, 31), standardSlot(143, 51), standardSlot(163, 51)).setProgressBarGuiXYWHUV(51, 30, 51, 38, 196, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("electrolyzer_cathode", List.of(MACHINE_MAP.get("electrolyzer_cathode_terminal"))).setItemInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("electrolyzer_anode", List.of(MACHINE_MAP.get("electrolyzer_anode_terminal"))).setItemInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("multiblock_distiller", List.of(MACHINE_MAP.get("distiller_controller"))).setFluidInputSlots(standardSlot(20, 41), standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(156, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51), standardSlot(156, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("multiblock_infiltrator", List.of(MACHINE_MAP.get("infiltrator_controller"))).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31)).setFluidInputSlots(standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("infiltrator_pressure_fluid", List.of(MACHINE_MAP.get("infiltrator_pressure_chamber"))).setFluidInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("multiblock_decay_pool", Lists.newArrayList(MACHINE_MAP.get("decay_pool_controller"))).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("decay_pool_heat_source", Lists.newArrayList(MACHINE_MAP.get("decay_pool_container"))).setItemInputSlots(standardSlot(32, 35)).setFluidInputSlots(standardSlot(52, 35)).setItemOutputSlots(bigSlot(108, 31)).setFluidOutputSlots(bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 35, 37, 16, 176, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("fission_irradiator", List.of(FISSION_REACTOR_MAP.get("fission_irradiator"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("pebble_fission", List.of(FISSION_REACTOR_MAP.get("pebble_bed_fission_controller"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("solid_fission", List.of(FISSION_REACTOR_MAP.get("solid_fuel_fission_controller"))));
            registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>("salt_fission", List.of(FISSION_REACTOR_MAP.get("molten_salt_fission_controller"))));

            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("fission_moderator", DataMapHelper.get(FISSION_MODERATOR_DATA)).setItemInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("fission_reflector", DataMapHelper.get(FISSION_REFLECTOR_DATA)).setItemInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("gas_cooling", FISSION_ENTITY_TYPE.get("cooler").get().getValidBlocks().stream().toList()).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("fission_heating", List.of(FISSION_REACTOR_MAP.get("fission_vent"))).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("salt_cooling", FISSION_ENTITY_TYPE.get("coolant_heater").get().getValidBlocks().stream().toList()).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("fission_emergency_cooling", List.of(FISSION_REACTOR_MAP.get("fission_vent"))).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
            Set<Block> hx_tubes = HX_ENTITY_TYPE.get("tube").get().getValidBlocks();
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("heat_exchanger", Stream.concat(Stream.of(HX_MAP.get("heat_exchanger_controller")), hx_tubes.stream()).toList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("condenser", Stream.concat(Stream.of(HX_MAP.get("condenser_controller")), hx_tubes.stream()).toList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("condenser_dissipation_fluid", List.of(HX_MAP.get("heat_exchanger_inlet"))).setFluidInputSlots(standardSlot(86, 35)).disableProgressBar());
            registerRecipeViewerCategoryInfo(new RecipeViewerSimpleCategoryInfoBuilder<>("turbine", StreamHelper.concatToList(Stream.of(TURBINE_MAP.get("turbine_controller")), TURBINE_ENTITY_TYPE.get("rotor_stator").get().getValidBlocks().stream(), TURBINE_ENTITY_TYPE.get("rotor_blade").get().getValidBlocks().stream())).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        }
    }

    public static <T> void register(Map<String, T> map, String name, T value) {
        T prev = map.put(name, value);
        if (prev != null) {
            throw new IllegalArgumentException("Registry name \"" + name + "\" already taken for type \"" + value.getClass().getSimpleName() + "\"!");
        }
    }

    public static void registerBlockTileInfo(BlockEntityInfoBlock<?> info) {
        register(BLOCK_ENTITY_INFO_MAP, info.name, info);
    }

    public static void registerContainerInfo(BlockEntityMenuInfo<?> info) {
        register(BLOCK_ENTITY_MENU_INFO_MAP, info.name, info);
    }

    public static void registerProcessorInfo(ProcessorMenuInfoBuilder<?, ?, ?, ?, ?> builder) {
        registerBlockTileInfo(builder.buildBlockInfo());
        registerContainerInfo(builder.buildContainerInfo());
    }

    public static void registerRecipeViewerCategoryInfo(RecipeViewerCategoryInfo info) {
        register(RECIPE_VIEWER_CATEGORY_INFO_MAP, info.getName(), info);
    }

    public static void registerRecipeViewerCategoryInfo(RecipeViewerSimpleCategoryInfoBuilder<?> builder) {
        RecipeViewerSimpleCategoryInfo<?> info = builder.buildCategoryInfo();
        registerRecipeViewerCategoryInfo(info);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends SimpleTileInfoBlock<TILE>> INFO getBlockSimpleTileInfo(String name) {
        return (INFO) BLOCK_ENTITY_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends ProcessorBlockInfo<TILE>> INFO getProcessorBlockInfo(String name) {
        return (INFO) BLOCK_ENTITY_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends BlockEntityMenuInfo<TILE>> INFO getTileContainerInfo(String name) {
        return (INFO) BLOCK_ENTITY_MENU_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <CATEGORY_INFO extends RecipeViewerCategoryInfo> CATEGORY_INFO getRecipeViewerCategoryInfo(String name) {
        return (CATEGORY_INFO) RECIPE_VIEWER_CATEGORY_INFO_MAP.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO, RECIPE>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO, RECIPE>, RECIPE extends BasicRecipe> INFO getProcessorMenuInfo(String name) {
        return (INFO) BLOCK_ENTITY_MENU_INFO_MAP.get(name);
    }

    public static boolean getProcessorMenuUpgradable(String name) {
        return getProcessorMenuInfo(name) instanceof UpgradableProcessorMenuInfo<?, ?, ?, ?>;
    }
}