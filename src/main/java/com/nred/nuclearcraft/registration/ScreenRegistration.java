package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.menu.processor.*;
import com.nred.nuclearcraft.screen.multiblock.*;
import com.nred.nuclearcraft.screen.multiblock.controller.SaltFissionControllerScreen;
import com.nred.nuclearcraft.screen.multiblock.controller.SolidFissionControllerScreen;
import com.nred.nuclearcraft.screen.multiblock.controller.TurbineControllerScreen;
import com.nred.nuclearcraft.screen.multiblock.port.*;
import com.nred.nuclearcraft.screen.processor.*;
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
        event.register((MenuType<AlloyFurnaceMenu>) PROCESSOR_MENU_TYPES.get("alloy_furnace").get(), AlloyFurnaceScreen::new);
        event.register((MenuType<AssemblerMenu>) PROCESSOR_MENU_TYPES.get("assembler").get(), AssemblerScreen::new);
        event.register((MenuType<CentrifugeMenu>) PROCESSOR_MENU_TYPES.get("centrifuge").get(), CentrifugeScreen::new);
        event.register((MenuType<ChemicalReactorMenu>) PROCESSOR_MENU_TYPES.get("chemical_reactor").get(), ChemicalReactorScreen::new);
        event.register((MenuType<CrystallizerMenu>) PROCESSOR_MENU_TYPES.get("crystallizer").get(), CrystallizerScreen::new);
        event.register((MenuType<DecayHastenerMenu>) PROCESSOR_MENU_TYPES.get("decay_hastener").get(), DecayHastenerScreen::new);
        event.register((MenuType<ElectricFurnaceMenu>) PROCESSOR_MENU_TYPES.get("electric_furnace").get(), ElectricFurnaceScreen::new);
        event.register((MenuType<ElectrolyzerMenu>) PROCESSOR_MENU_TYPES.get("electrolyzer").get(), ElectrolyzerScreen::new);
        event.register((MenuType<EnricherMenu>) PROCESSOR_MENU_TYPES.get("fluid_enricher").get(), EnricherScreen::new);
        event.register((MenuType<ExtractorMenu>) PROCESSOR_MENU_TYPES.get("fluid_extractor").get(), ExtractorScreen::new);
        event.register((MenuType<FuelReprocessorMenu>) PROCESSOR_MENU_TYPES.get("fuel_reprocessor").get(), FuelReprocessorScreen::new);
        event.register((MenuType<InfuserMenu>) PROCESSOR_MENU_TYPES.get("fluid_infuser").get(), InfuserScreen::new);
        event.register((MenuType<IngotFormerMenu>) PROCESSOR_MENU_TYPES.get("ingot_former").get(), IngotFormerScreen::new);
        event.register((MenuType<ManufactoryMenu>) PROCESSOR_MENU_TYPES.get("manufactory").get(), ManufactoryScreen::new);
        event.register((MenuType<MelterMenu>) PROCESSOR_MENU_TYPES.get("melter").get(), MelterScreen::new);
        event.register((MenuType<PressurizerMenu>) PROCESSOR_MENU_TYPES.get("pressurizer").get(), PressurizerScreen::new);
        event.register((MenuType<RockCrusherMenu>) PROCESSOR_MENU_TYPES.get("rock_crusher").get(), RockCrusherScreen::new);
        event.register((MenuType<SaltMixerMenu>) PROCESSOR_MENU_TYPES.get("fluid_mixer").get(), FluidMixerScreen::new);
        event.register((MenuType<SeparatorMenu>) PROCESSOR_MENU_TYPES.get("separator").get(), SeparatorScreen::new);
        event.register((MenuType<SupercoolerMenu>) PROCESSOR_MENU_TYPES.get("supercooler").get(), SupercoolerScreen::new);

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
    }
}