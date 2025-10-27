package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block.tile.info.BlockSimpleTileInfo;
import com.nred.nuclearcraft.block.tile.info.BlockTileInfo;
import com.nred.nuclearcraft.block_entity.UniversalBinEntity;
import com.nred.nuclearcraft.block_entity.dummy.MachineInterfaceEntity;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCellPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionIrradiatorPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.block_entity.generator.DecayGeneratorEntity;
import com.nred.nuclearcraft.block_entity.generator.TileSolarPanel;
import com.nred.nuclearcraft.block_entity.passive.TilePassive;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.*;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorBlockInfo;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorContainerInfoBuilderImpl.BasicUpgradableProcessorContainerInfoBuilder;
import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.menu.multiblock.SaltFissionControllerMenu;
import com.nred.nuclearcraft.menu.multiblock.SolidFissionControllerMenu;
import com.nred.nuclearcraft.menu.multiblock.TurbineControllerMenu;
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

import java.util.Map;

import static com.nred.nuclearcraft.config.NCConfig.processor_power;
import static com.nred.nuclearcraft.config.NCConfig.processor_time;
import static com.nred.nuclearcraft.util.ContainerInfoHelper.bigSlot;
import static com.nred.nuclearcraft.util.ContainerInfoHelper.standardSlot;

public class TileInfoHandler {

    public static final Object2ObjectMap<String, BlockTileInfo<?>> BLOCK_TILE_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();

    public static final Object2ObjectMap<String, TileContainerInfo<?>> TILE_CONTAINER_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();

//	public static final Object2ObjectMap<String, JEICategoryInfo<?, ?, ?>> JEI_CATEGORY_INFO_MAP = new Object2ObjectLinkedOpenHashMap<>();

    public static void preInit() {
        registerBlockTileInfo(new BlockSimpleTileInfo<>("machine_interface", MachineInterfaceEntity.class, MachineInterfaceEntity::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("decay_generator", DecayGeneratorEntity.class, DecayGeneratorEntity::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("bin", UniversalBinEntity.class, UniversalBinEntity::new));

        registerBlockTileInfo(new BlockSimpleTileInfo<>("solar_panel_basic", TileSolarPanel.Basic.class, TileSolarPanel.Basic::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("solar_panel_advanced", TileSolarPanel.Advanced.class, TileSolarPanel.Advanced::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("solar_panel_du", TileSolarPanel.DU.class, TileSolarPanel.DU::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("solar_panel_elite", TileSolarPanel.Elite.class, TileSolarPanel.Elite::new));

        registerBlockTileInfo(new BlockSimpleTileInfo<>("cobblestone_generator", TilePassive.CobblestoneGenerator.class, TilePassive.CobblestoneGenerator::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("cobblestone_generator_compact", TilePassive.CobblestoneGeneratorCompact.class, TilePassive.CobblestoneGeneratorCompact::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("cobblestone_generator_dense", TilePassive.CobblestoneGeneratorDense.class, TilePassive.CobblestoneGeneratorDense::new));

        registerBlockTileInfo(new BlockSimpleTileInfo<>("water_source", TilePassive.WaterSource.class, TilePassive.WaterSource::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("water_source_compact", TilePassive.WaterSourceCompact.class, TilePassive.WaterSourceCompact::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("water_source_dense", TilePassive.WaterSourceDense.class, TilePassive.WaterSourceDense::new));

        registerBlockTileInfo(new BlockSimpleTileInfo<>("nitrogen_collector", TilePassive.NitrogenCollector.class, TilePassive.NitrogenCollector::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("nitrogen_collector_compact", TilePassive.NitrogenCollectorCompact.class, TilePassive.NitrogenCollectorCompact::new));
        registerBlockTileInfo(new BlockSimpleTileInfo<>("nitrogen_collector_dense", TilePassive.NitrogenCollectorDense.class, TilePassive.NitrogenCollectorDense::new));

//        registerBlockTileInfo(new BlockSimpleTileInfo<>("geiger_block", TileGeigerCounter.class, TileGeigerCounter::new,)); TODO

//		registerProcessorInfo(new BasicProcessorContainerInfoBuilder<>("nuclear_furnace", NuclearFurnaceTest.class, NuclearFurnaceTest::new)); TODO REMOVE
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("manufactory", ManufactoryEntity.class, ManufactoryEntity::new, ManufactoryMenu::new).setParticles("crit", "reddust").setDefaultProcessTime(() -> processor_time[0]).setDefaultProcessPower(() -> processor_power[0]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("separator", SeparatorEntity.class, SeparatorEntity::new, SeparatorMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[1]).setDefaultProcessPower(() -> processor_power[1]).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31), bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("decay_hastener", DecayHastenerEntity.class, DecayHastenerEntity::new, DecayHastenerMenu::new).setParticles("reddust").setDefaultProcessTime(() -> processor_time[2]).setDefaultProcessPower(() -> processor_power[2]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("fuel_reprocessor", FuelReprocessorEntity.class, FuelReprocessorEntity::new, FuelReprocessorMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[3]).setDefaultProcessPower(() -> processor_power[3]).standardExtend(0, 12).setItemInputSlots(standardSlot(30, 41)).setItemOutputSlots(standardSlot(86, 31), standardSlot(106, 31), standardSlot(126, 31), standardSlot(146, 31), standardSlot(86, 51), standardSlot(106, 51), standardSlot(126, 51), standardSlot(146, 51)).setProgressBarGuiXYWHUV(48, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("alloy_furnace", AlloyFurnaceEntity.class, AlloyFurnaceEntity::new, AlloyFurnaceMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[4]).setDefaultProcessPower(() -> processor_power[4]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("infuser", InfuserEntity.class, InfuserEntity::new, InfuserMenu::new).setParticles("portal", "reddust").setDefaultProcessTime(() -> processor_time[5]).setDefaultProcessPower(() -> processor_power[5]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setItemOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("melter", MelterEntity.class, MelterEntity::new, MelterMenu::new).setParticles("flame", "lava").setDefaultProcessTime(() -> processor_time[6]).setDefaultProcessPower(() -> processor_power[6]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("supercooler", SupercoolerEntity.class, SupercoolerEntity::new, SupercoolerMenu::new).setParticles("smoke", "snowshovel").setDefaultProcessTime(() -> processor_time[7]).setDefaultProcessPower(() -> processor_power[7]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("electrolyzer", ElectrolyzerEntity.class, ElectrolyzerEntity::new, ElectrolyzerMenu::new).setParticles("reddust", "splash").setDefaultProcessTime(() -> processor_time[8]).setDefaultProcessPower(() -> processor_power[8]).setLosesProgress(true).standardExtend(0, 12).setFluidInputSlots(standardSlot(50, 41)).setFluidOutputSlots(standardSlot(106, 31), standardSlot(126, 31), standardSlot(106, 51), standardSlot(126, 51)).setProgressBarGuiXYWHUV(68, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("assembler", AssemblerEntity.class, AssemblerEntity::new, AssemblerMenu::new).setParticles("crit", "smoke").setDefaultProcessTime(() -> processor_time[9]).setDefaultProcessPower(() -> processor_power[9]).standardExtend(0, 12).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31), standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("ingot_former", IngotFormerEntity.class, IngotFormerEntity::new, IngotFormerMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[10]).setDefaultProcessPower(() -> processor_power[10]).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("pressurizer", PressurizerEntity.class, PressurizerEntity::new, PressurizerMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[11]).setDefaultProcessPower(() -> processor_power[11]).setLosesProgress(true).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("chemical_reactor", ChemicalReactorEntity.class, ChemicalReactorEntity::new, ChemicalReactorMenu::new).setParticles("reddust").setDefaultProcessTime(() -> processor_time[12]).setDefaultProcessPower(() -> processor_power[12]).setLosesProgress(true).setFluidInputSlots(standardSlot(32, 35), standardSlot(52, 35)).setFluidOutputSlots(bigSlot(108, 31), bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("salt_mixer", SaltMixerEntity.class, SaltMixerEntity::new, SaltMixerMenu::new).setParticles("endRod", "reddust").setDefaultProcessTime(() -> processor_time[13]).setDefaultProcessPower(() -> processor_power[13]).setFluidInputSlots(standardSlot(46, 35), standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("crystallizer", CrystallizerEntity.class, CrystallizerEntity::new, CrystallizerMenu::new).setParticles("depthsuspend").setDefaultProcessTime(() -> processor_time[14]).setDefaultProcessPower(() -> processor_power[14]).setLosesProgress(true).setFluidInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("enricher", EnricherEntity.class, EnricherEntity::new, EnricherMenu::new).setParticles("depthsuspend", "splash").setDefaultProcessTime(() -> processor_time[15]).setDefaultProcessPower(() -> processor_power[15]).setLosesProgress(true).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("extractor", ExtractorEntity.class, ExtractorEntity::new, ExtractorMenu::new).setParticles("depthsuspend", "reddust").setDefaultProcessTime(() -> processor_time[16]).setDefaultProcessPower(() -> processor_power[16]).setLosesProgress(true).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("centrifuge", CentrifugeEntity.class, CentrifugeEntity::new, CentrifugeMenu::new).setParticles("depthsuspend", "endRod").setDefaultProcessTime(() -> processor_time[17]).setDefaultProcessPower(() -> processor_power[17]).standardExtend(0, 12).setFluidInputSlots(standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("rock_crusher", RockCrusherEntity.class, RockCrusherEntity::new, RockCrusherMenu::new).setParticles("smoke").setDefaultProcessTime(() -> processor_time[18]).setDefaultProcessPower(() -> processor_power[18]).setItemInputSlots(standardSlot(38, 35)).setItemOutputSlots(standardSlot(94, 35), standardSlot(114, 35), standardSlot(134, 35)).setProgressBarGuiXYWHUV(56, 35, 37, 16, 176, 3));
        registerProcessorInfo(new BasicUpgradableProcessorContainerInfoBuilder<>("electric_furnace", ElectricFurnaceEntity.class, ElectricFurnaceEntity::new, ElectricFurnaceMenu::new).setParticles("reddust", "smoke").setDefaultProcessTime(() -> processor_time[19]).setDefaultProcessPower(() -> processor_power[19]).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
//		
//		registerProcessorInfo(new BasicProcessorContainerInfoBuilder<>(Global.MOD_ID, "radiation_scrubber", TileRadiationScrubber.class, TileRadiationScrubber::new, ContainerRadiationScrubber.class, ContainerRadiationScrubber::new, proxy.clientGet(() -> GuiRadiationScrubber.class), proxy.clientGet(() -> GuiRadiationScrubber::new)).setCreativeTab(NCTabs.radiation).setDefaultProcessPower(1).setConsumesInputs(true).setItemInputSlots(standardSlot(32, 35)).setFluidInputSlots(standardSlot(52, 35)).setItemOutputSlots(bigSlot(108, 31)).setFluidOutputSlots(bigSlot(136, 31)).setProgressBarGuiXYWHUV(70, 35, 37, 16, 176, 3).setMachineConfigGuiXY(-1, -1).setRedstoneControlGuiXY(27, 63));
//		
//        registerContainerInfo(new TileContainerInfo<>(Global.MOD_ID, "electrolyzer_controller", TileElectrolyzerController.class, ContainerElectrolyzerController::new, clientGetGuiInfoTileFunction(() -> GuiElectrolyzerController::new)));
//        registerContainerInfo(new TileContainerInfo<>(Global.MOD_ID, "distiller_controller", TileDistillerController.class, ContainerDistillerController::new, clientGetGuiInfoTileFunction(() -> GuiDistillerController::new)));
//        registerContainerInfo(new TileContainerInfo<>(Global.MOD_ID, "infiltrator_controller", TileInfiltratorController.class, ContainerInfiltratorController::new, clientGetGuiInfoTileFunction(() -> GuiInfiltratorController::new)));
//        registerContainerInfo(new TileContainerInfo<>(Global.MOD_ID, "heat_exchanger_controller", TileHeatExchangerController.class, ContainerHeatExchangerController::new, clientGetGuiInfoTileFunction(() -> GuiHeatExchangerController::new)));
//        registerContainerInfo(new TileContainerInfo<>(Global.MOD_ID, "condenser_controller", TileCondenserController.class, ContainerCondenserController::new, clientGetGuiInfoTileFunction(() -> GuiCondenserController::new)));


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
    }

    public static void init() {
        if (ModCheck.jeiLoaded()) { // TODO remove?
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("manufactory", ManufactoryRecipeWrapper.class, ManufactoryRecipeWrapper::new, Lists.newArrayList(NCBlocks.manufactory)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("separator", SeparatorRecipeWrapper.class, SeparatorRecipeWrapper::new, Lists.newArrayList(NCBlocks.separator)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("decay_hastener", DecayHastenerRecipeWrapper.class, DecayHastenerRecipeWrapper::new, Lists.newArrayList(NCBlocks.decay_hastener)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("fuel_reprocessor", FuelReprocessorRecipeWrapper.class, FuelReprocessorRecipeWrapper::new, Lists.newArrayList(NCBlocks.fuel_reprocessor)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("alloy_furnace", AlloyFurnaceRecipeWrapper.class, AlloyFurnaceRecipeWrapper::new, Lists.newArrayList(NCBlocks.alloy_furnace)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("infuser", InfuserRecipeWrapper.class, InfuserRecipeWrapper::new, Lists.newArrayList(NCBlocks.infuser)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("melter", MelterRecipeWrapper.class, MelterRecipeWrapper::new, Lists.newArrayList(NCBlocks.melter)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("supercooler", SupercoolerRecipeWrapper.class, SupercoolerRecipeWrapper::new, Lists.newArrayList(NCBlocks.supercooler)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("electrolyzer", ElectrolyzerRecipeWrapper.class, ElectrolyzerRecipeWrapper::new, Lists.newArrayList(NCBlocks.electrolyzer)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("assembler", AssemblerRecipeWrapper.class, AssemblerRecipeWrapper::new, Lists.newArrayList(NCBlocks.assembler)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("ingot_former", IngotFormerRecipeWrapper.class, IngotFormerRecipeWrapper::new, Lists.newArrayList(NCBlocks.ingot_former)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("pressurizer", PressurizerRecipeWrapper.class, PressurizerRecipeWrapper::new, Lists.newArrayList(NCBlocks.pressurizer)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("chemical_reactor", ChemicalReactorRecipeWrapper.class, ChemicalReactorRecipeWrapper::new, Lists.newArrayList(NCBlocks.chemical_reactor)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("salt_mixer", SaltMixerRecipeWrapper.class, SaltMixerRecipeWrapper::new, Lists.newArrayList(NCBlocks.salt_mixer)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("crystallizer", CrystallizerRecipeWrapper.class, CrystallizerRecipeWrapper::new, Lists.newArrayList(NCBlocks.crystallizer)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("enricher", EnricherRecipeWrapper.class, EnricherRecipeWrapper::new, Lists.newArrayList(NCBlocks.enricher)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("extractor", ExtractorRecipeWrapper.class, ExtractorRecipeWrapper::new, Lists.newArrayList(NCBlocks.extractor)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("centrifuge", CentrifugeRecipeWrapper.class, CentrifugeRecipeWrapper::new, Lists.newArrayList(NCBlocks.centrifuge)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("rock_crusher", RockCrusherRecipeWrapper.class, RockCrusherRecipeWrapper::new, Lists.newArrayList(NCBlocks.rock_crusher)));
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("electric_furnace", ElectricFurnaceRecipeWrapper.class, ElectricFurnaceRecipeWrapper::new, Lists.newArrayList(NCBlocks.electric_furnace)) {
//				
//				public List<ElectricFurnaceRecipeWrapper> getJEIRecipes(IGuiHelper guiHelper) {
//					List<ElectricFurnaceRecipeWrapper> recipes = super.getJEIRecipes(guiHelper);
//					FurnaceRecipes.instance().getSmeltingList().forEach((k, v) -> recipes.add(getJEIRecipe(guiHelper, ElectricFurnaceRecipes.getVanillaFurnaceRecipe(k, v))));
//					return recipes;
//				}
//			});
//			
//			registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("radiation_scrubber", RadiationScrubberRecipeWrapper.class, RadiationScrubberRecipeWrapper::new, Lists.newArrayList(NCBlocks.radiation_scrubber)));
//			
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "collector", CollectorRecipeWrapper.class, CollectorRecipeWrapper::new, NCJEI.registeredCollectors(), Lists.newArrayList()).setItemInputSlots(standardSlot(42, 35)).setItemOutputSlots(bigSlot(98, 31)).setFluidOutputSlots(bigSlot(126, 31)).setProgressBarGuiXYWHUV(60, 34, 37, 18, 176, 3).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "decay_generator", DecayGeneratorRecipeWrapper.class, DecayGeneratorRecipeWrapper::new, Lists.newArrayList(NCBlocks.decay_generator), Lists.newArrayList()).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)));
//			
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "machine_diaphragm", MachineDiaphragmRecipeWrapper.class, MachineDiaphragmRecipeWrapper::new, NCJEI.getMachineDiaphragmCrafters(), Lists.newArrayList()).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "machine_sieve_assembly", MachineSieveAssemblyRecipeWrapper.class, MachineSieveAssemblyRecipeWrapper::new, NCJEI.getMachineSieveAssemblyCrafters(), Lists.newArrayList()).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "multiblock_electrolyzer", MultiblockElectrolyzerRecipeWrapper.class, MultiblockElectrolyzerRecipeWrapper::new, Lists.newArrayList(NCBlocks.electrolyzer_controller), Lists.newArrayList()).standardExtend(0, 12).setItemInputSlots(standardSlot(13, 31), standardSlot(33, 31)).setFluidInputSlots(standardSlot(13, 51), standardSlot(33, 51)).setItemOutputSlots(standardSlot(103, 31), standardSlot(123, 31), standardSlot(103, 51), standardSlot(123, 51)).setFluidOutputSlots(standardSlot(143, 31), standardSlot(163, 31), standardSlot(143, 51), standardSlot(163, 51)).setProgressBarGuiXYWHUV(51, 30, 51, 38, 196, 3).setStandardJeiAlternateTitle().setStandardJeiAlternateTexture());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "electrolyzer_cathode", ElectrolyzerCathodeRecipeWrapper.class, ElectrolyzerCathodeRecipeWrapper::new, Lists.newArrayList(NCBlocks.electrolyzer_cathode_terminal), Lists.newArrayList()).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "electrolyzer_anode", ElectrolyzerAnodeRecipeWrapper.class, ElectrolyzerAnodeRecipeWrapper::new, Lists.newArrayList(NCBlocks.electrolyzer_anode_terminal), Lists.newArrayList()).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "multiblock_distiller", MultiblockDistillerRecipeWrapper.class, MultiblockDistillerRecipeWrapper::new, Lists.newArrayList(NCBlocks.distiller_controller), Lists.newArrayList()).setFluidInputSlots(standardSlot(20, 41), standardSlot(40, 41)).setFluidOutputSlots(standardSlot(96, 31), standardSlot(116, 31), standardSlot(136, 31), standardSlot(156, 31), standardSlot(96, 51), standardSlot(116, 51), standardSlot(136, 51), standardSlot(156, 51)).setProgressBarGuiXYWHUV(58, 30, 37, 38, 176, 3).setStandardJeiAlternateTitle().setStandardJeiAlternateTexture());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "multiblock_infiltrator", MultiblockInfiltratorRecipeWrapper.class, MultiblockInfiltratorRecipeWrapper::new, Lists.newArrayList(NCBlocks.infiltrator_controller), Lists.newArrayList()).setItemInputSlots(standardSlot(46, 31), standardSlot(66, 31)).setFluidInputSlots(standardSlot(46, 51), standardSlot(66, 51)).setItemOutputSlots(bigSlot(122, 37)).setProgressBarGuiXYWHUV(84, 31, 37, 36, 176, 3).setStandardJeiAlternateTitle().setStandardJeiAlternateTexture());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "infiltrator_pressure_fluid", InfiltratorPressureFluidRecipeWrapper.class, InfiltratorPressureFluidRecipeWrapper::new, Lists.newArrayList(NCBlocks.infiltrator_pressure_chamber), Lists.newArrayList()).setFluidInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			
//          registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("fission_irradiator", FissionIrradiatorRecipeWrapper.class, FissionIrradiatorRecipeWrapper::new, Lists.newArrayList(NCBlocks.fission_irradiator)));
//          registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("solid_fission_cell", SolidFissionRecipeWrapper.class, SolidFissionRecipeWrapper::new, Lists.newArrayList(NCBlocks.solid_fission_controller, NCBlocks.solid_fission_cell)));
//          registerJEICategoryInfo(new JEIProcessorCategoryInfo<>("salt_fission_vessel", SaltFissionRecipeWrapper.class, SaltFissionRecipeWrapper::new, Lists.newArrayList(NCBlocks.salt_fission_controller, NCBlocks.salt_fission_vessel)));
//			
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_moderator", FissionModeratorRecipeWrapper.class, FissionModeratorRecipeWrapper::new, NCJEI.getFissionModeratorCrafters(), Lists.newArrayList()).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_reflector", FissionReflectorRecipeWrapper.class, FissionReflectorRecipeWrapper::new, NCJEI.getFissionReflectorCrafters(), Lists.newArrayList()).setItemInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "pebble_fission", PebbleFissionRecipeWrapper.class, PebbleFissionRecipeWrapper::new, Lists.newArrayList(), Lists.newArrayList()).setItemInputSlots(standardSlot(56, 35)).setItemOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_heating", FissionHeatingRecipeWrapper.class, FissionHeatingRecipeWrapper::new, Lists.newArrayList(NCBlocks.fission_vent), Lists.newArrayList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "coolant_heater", CoolantHeaterRecipeWrapper.class, CoolantHeaterRecipeWrapper::new, NCJEI.getCoolantHeaterCrafters(), Lists.newArrayList(getProcessorJEIContainerConnection("salt_fission_heater"))).setItemInputSlots(standardSlot(46, 35)).setFluidInputSlots(standardSlot(66, 35)).setFluidOutputSlots(bigSlot(122, 31)).setProgressBarGuiXYWHUV(84, 35, 37, 16, 176, 3).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "fission_emergency_cooling", FissionEmergencyCoolingRecipeWrapper.class, FissionEmergencyCoolingRecipeWrapper::new, Lists.newArrayList(NCBlocks.fission_vent), Lists.newArrayList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "heat_exchanger", HeatExchangerRecipeWrapper.class, HeatExchangerRecipeWrapper::new, NCJEI.getHeatExchangerCrafters(), Lists.newArrayList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "condenser", CondenserRecipeWrapper.class, CondenserRecipeWrapper::new, NCJEI.getCondenserCrafters(), Lists.newArrayList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "condenser_dissipation_fluid", CondenserDissipationFluidRecipeWrapper.class, CondenserDissipationFluidRecipeWrapper::new, Lists.newArrayList(NCBlocks.heat_exchanger_inlet), Lists.newArrayList()).setFluidInputSlots(standardSlot(86, 35)).disableProgressBar().setStandardJeiAlternateTitle());
//			registerJEICategoryInfo(new JEISimpleCategoryInfoBuilder<>(Global.MOD_ID, "turbine", TurbineRecipeWrapper.class, TurbineRecipeWrapper::new, NCJEI.getTurbineCrafters(), Lists.newArrayList()).setFluidInputSlots(standardSlot(56, 35)).setFluidOutputSlots(bigSlot(112, 31)).setStandardJeiAlternateTitle());
        }
    }

    public static <T> void register(Map<String, T> map, String name, T value) {
        T prev = map.put(name, value);
        if (prev != null) {
            throw new IllegalArgumentException("Registry name \"" + name + "\" already taken for type \"" + value.getClass().getSimpleName() + "\"!");
        }
    }

    public static void registerBlockTileInfo(BlockTileInfo<?> info) {
        register(BLOCK_TILE_INFO_MAP, info.name, info);
    }

    public static void registerContainerInfo(TileContainerInfo<?> info) {
        register(TILE_CONTAINER_INFO_MAP, info.name, info);
    }

    public static void registerProcessorInfo(ProcessorContainerInfoBuilder<?, ?, ?, ?> builder) {
        registerBlockTileInfo(builder.buildBlockInfo());
        registerContainerInfo(builder.buildContainerInfo());
    }

//	public static void registerJEICategoryInfo(JEICategoryInfo<?, ?, ?> info) {
//		register(JEI_CATEGORY_INFO_MAP, info.getName(), info);
//	}
//
//	public static <WRAPPER extends JEISimpleRecipeWrapper<WRAPPER>> void registerJEICategoryInfo(JEISimpleCategoryInfoBuilder<WRAPPER> builder) {
//		JEISimpleCategoryInfo<WRAPPER> info = builder.buildCategoryInfo();
//		registerJEICategoryInfo(info);
//	}

    @SuppressWarnings("unchecked")
    public static <TILE extends BlockEntity, INFO extends BlockSimpleTileInfo<TILE>> INFO getBlockSimpleTileInfo(String name) {
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

//    @SuppressWarnings("unchecked")
//    public static <WRAPPER extends JEIRecipeWrapper, CATEGORY extends JEIRecipeCategory<WRAPPER, CATEGORY, CATEGORY_INFO>, CATEGORY_INFO extends JEICategoryInfo<WRAPPER, CATEGORY, CATEGORY_INFO>> CATEGORY_INFO getJEICategoryInfo(String name) {
//        return (CATEGORY_INFO) JEI_CATEGORY_INFO_MAP.get(name);
//    }
//
//    public static void addJEICrafter(String name, Object crafter) {
//        getJEICategoryInfo(name).jeiCrafters.add(crafter);
//    }
//
//    public static void addJEIContainerConnection(String name, JEIContainerConnection connection) {
//        getJEICategoryInfo(name).jeiContainerConnections.add(connection);
//    }
//
//    public static <TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> JEIContainerConnection getProcessorJEIContainerConnection(String name) {
//        return getProcessorContainerInfo(name).getJEIContainerConnection();
//    }
//
//    public static <TILE extends BlockEntity> GuiInfoTileFunction<TILE> clientGetGuiInfoTileFunction(Supplier<GuiInfoTileFunction<TILE>> supplier) {
//        return proxy.clientGet(supplier);
//    }
}
