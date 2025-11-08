package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.*;
import com.nred.nuclearcraft.screen.multiblock.*;
import com.nred.nuclearcraft.screen.multiblock.controller.*;
import com.nred.nuclearcraft.screen.multiblock.port.*;
import com.nred.nuclearcraft.screen.processor.GuiProcessorImpl.*;
import com.nred.nuclearcraft.screen.processor.NuclearFurnaceScreen;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.MenuRegistration.*;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ScreenRegistration {
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register((MenuType<AlloyFurnaceMenu>) PROCESSOR_MENU_TYPES.get("alloy_furnace").get(), GuiAlloyFurnace::new);
        event.register((MenuType<AssemblerMenu>) PROCESSOR_MENU_TYPES.get("assembler").get(), GuiAssembler::new);
        event.register((MenuType<CentrifugeMenu>) PROCESSOR_MENU_TYPES.get("centrifuge").get(), GuiCentrifuge::new);
        event.register((MenuType<ChemicalReactorMenu>) PROCESSOR_MENU_TYPES.get("chemical_reactor").get(), GuiChemicalReactor::new);
        event.register((MenuType<CrystallizerMenu>) PROCESSOR_MENU_TYPES.get("crystallizer").get(), GuiCrystallizer::new);
        event.register((MenuType<DecayHastenerMenu>) PROCESSOR_MENU_TYPES.get("decay_hastener").get(), GuiDecayHastener::new);
        event.register((MenuType<ElectricFurnaceMenu>) PROCESSOR_MENU_TYPES.get("electric_furnace").get(), GuiElectricFurnace::new);
        event.register((MenuType<ElectrolyzerMenu>) PROCESSOR_MENU_TYPES.get("electrolyzer").get(), GuiElectrolyzer::new);
        event.register((MenuType<EnricherMenu>) PROCESSOR_MENU_TYPES.get("fluid_enricher").get(), GuiEnricher::new);
        event.register((MenuType<ExtractorMenu>) PROCESSOR_MENU_TYPES.get("fluid_extractor").get(), GuiExtractor::new);
        event.register((MenuType<FuelReprocessorMenu>) PROCESSOR_MENU_TYPES.get("fuel_reprocessor").get(), GuiFuelReprocessor::new);
        event.register((MenuType<InfuserMenu>) PROCESSOR_MENU_TYPES.get("fluid_infuser").get(), GuiInfuser::new);
        event.register((MenuType<IngotFormerMenu>) PROCESSOR_MENU_TYPES.get("ingot_former").get(), GuiIngotFormer::new);
        event.register((MenuType<ManufactoryMenu>) PROCESSOR_MENU_TYPES.get("manufactory").get(), GuiManufactory::new);
        event.register((MenuType<MelterMenu>) PROCESSOR_MENU_TYPES.get("melter").get(), GuiMelter::new);
        event.register((MenuType<PressurizerMenu>) PROCESSOR_MENU_TYPES.get("pressurizer").get(), GuiPressurizer::new);
        event.register((MenuType<RockCrusherMenu>) PROCESSOR_MENU_TYPES.get("rock_crusher").get(), GuiRockCrusher::new);
        event.register((MenuType<SaltMixerMenu>) PROCESSOR_MENU_TYPES.get("fluid_mixer").get(), GuiSaltMixer::new);
        event.register((MenuType<SeparatorMenu>) PROCESSOR_MENU_TYPES.get("separator").get(), GuiSeparator::new);
        event.register((MenuType<SupercoolerMenu>) PROCESSOR_MENU_TYPES.get("supercooler").get(), GuiSupercooler::new);

        event.register(NUCLEAR_FURNACE_MENU_TYPE.get(), NuclearFurnaceScreen::new);

        event.register(TURBINE_CONTROLLER_MENU_TYPE.get(), TurbineControllerScreen::new);
        event.register(SOLID_FISSION_CONTROLLER_MENU_TYPE.get(), SolidFissionControllerScreen::new);
        event.register(SALT_FISSION_CONTROLLER_MENU_TYPE.get(), SaltFissionControllerScreen::new);

        event.register(FISSION_CELL_PORT_MENU_TYPE.get(), FissionCellPortScreen::new);
        event.register(FISSION_COOLER_PORT_MENU_TYPE.get(), FissionCoolerPortScreen::new);
        event.register(FISSION_HEATER_PORT_MENU_TYPE.get(), FissionHeaterPortScreen::new);
        event.register(FISSION_IRRADIATOR_PORT_MENU_TYPE.get(), FissionIrradiatorPortScreen::new);
        event.register(FISSION_VESSEL_PORT_MENU_TYPE.get(), FissionVesselPortScreen::new);

        event.register(FISSION_SOLID_CELL_MENU_TYPE.get(), SolidFissionCellScreen::new);
        event.register(FISSION_COOLER_MENU_TYPE.get(), FissionCoolerScreen::new);
        event.register(FISSION_SALT_HEATER_MENU_TYPE.get(), SaltFissionHeaterScreen::new);
        event.register(FISSION_IRRADIATOR_MENU_TYPE.get(), FissionIrradiatorScreen::new);
        event.register(FISSION_SALT_VESSEL_MENU_TYPE.get(), SaltFissionVesselScreen::new);

        event.register(HX_CONTROLLER_MENU_TYPE.get(), HeatExchangerControllerScreen::new);
        event.register(CONDENSER_CONTROLLER_MENU_TYPE.get(), CondenserControllerScreen::new);

        event.register(DISTILLER_CONTROLLER_MENU_TYPE.get(), DistillerControllerScreen::new);
        event.register(ELECTROLYZER_CONTROLLER_MENU_TYPE.get(), ElectrolyzerControllerScreen::new);
        event.register(INFILTRATOR_CONTROLLER_MENU_TYPE.get(), InfiltratorControllerScreen::new);
    }
}