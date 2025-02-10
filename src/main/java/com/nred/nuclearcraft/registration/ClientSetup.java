package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.menu.*;
import com.nred.nuclearcraft.screen.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.MenuRegistration.MENU_TYPES;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void fluidLoad(RegisterClientExtensionsEvent event) {
        for (Fluids fluid : fluidValues(GASSES, MOLTEN, CUSTOM_FLUID, HOT_GAS, SUGAR, CHOCOLATE, FISSION, STEAM, SALT_SOLUTION, ACID, FLAMMABLE, HOT_COOLANT, COOLANT)) {
            event.registerFluidType(fluid.client, fluid.type);
        }
    }

    @SubscribeEvent
    public static void fluidColoring(final FMLCommonSetupEvent event) {
        for (Fluids fluid : fluidValues(GASSES, MOLTEN, CUSTOM_FLUID, HOT_GAS, SUGAR, CHOCOLATE, FISSION, STEAM, SALT_SOLUTION, ACID, FLAMMABLE, HOT_COOLANT, COOLANT)) {
            ItemBlockRenderTypes.setRenderLayer(fluid.still.get(), RenderType.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(fluid.flowing.get(), RenderType.TRANSLUCENT);
        }
    }

    @SubscribeEvent
    public static void bucketColoring(RegisterColorHandlersEvent.Item event) {
        for (Fluids fluid : fluidValues(GASSES, MOLTEN, CUSTOM_FLUID, HOT_GAS, SUGAR, CHOCOLATE, FISSION, STEAM, SALT_SOLUTION, ACID, FLAMMABLE, HOT_COOLANT, COOLANT)) {
            event.register(((stack, tintIndex) -> tintIndex == 0 ? -1 : fluid.client.getTintColor()), fluid.bucket.asItem());
        }
    }

    @SubscribeEvent
    private static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(((MenuType<AlloyFurnaceMenu>) MENU_TYPES.get("alloy_furnace").get()), AlloyFurnaceScreen::new);
        event.register(((MenuType<AssemblerMenu>) MENU_TYPES.get("assembler").get()), AssemblerScreen::new);
        event.register(((MenuType<CentrifugeMenu>) MENU_TYPES.get("centrifuge").get()), CentrifugeScreen::new);
        event.register(((MenuType<ChemicalReactorMenu>) MENU_TYPES.get("chemical_reactor").get()), ChemicalReactorScreen::new);
        event.register(((MenuType<CrystallizerMenu>) MENU_TYPES.get("crystallizer").get()), CrystallizerScreen::new);
        event.register(((MenuType<DecayHastenerMenu>) MENU_TYPES.get("decay_hastener").get()), DecayHastenerScreen::new);
        event.register(((MenuType<ElectricFurnaceMenu>) MENU_TYPES.get("electric_furnace").get()), ElectricFurnaceScreen::new);
        event.register(((MenuType<ElectrolyzerMenu>) MENU_TYPES.get("electrolyzer").get()), ElectrolyzerScreen::new);
        event.register(((MenuType<EnricherMenu>) MENU_TYPES.get("fluid_enricher").get()), EnricherScreen::new);
        event.register(((MenuType<ExtractorMenu>) MENU_TYPES.get("fluid_extractor").get()), ExtractorScreen::new);
        event.register(((MenuType<FuelReprocessorMenu>) MENU_TYPES.get("fuel_reprocessor").get()), FuelReprocessorScreen::new);
        event.register(((MenuType<InfuserMenu>) MENU_TYPES.get("fluid_infuser").get()), InfuserScreen::new);
        event.register(((MenuType<IngotFormerMenu>) MENU_TYPES.get("ingot_former").get()), IngotFormerScreen::new);
        event.register(((MenuType<ManufactoryMenu>) MENU_TYPES.get("manufactory").get()), ManufactoryScreen::new);
        event.register(((MenuType<MelterMenu>) MENU_TYPES.get("melter").get()), MelterScreen::new);
        event.register(((MenuType<PressurizerMenu>) MENU_TYPES.get("pressurizer").get()), PressurizerScreen::new);
        event.register(((MenuType<RockCrusherMenu>) MENU_TYPES.get("rock_crusher").get()), RockCrusherScreen::new);
        event.register(((MenuType<SaltMixerMenu>) MENU_TYPES.get("fluid_mixer").get()), SaltMixerScreen::new);
        event.register(((MenuType<SeparatorMenu>) MENU_TYPES.get("separator").get()), SeparatorScreen::new);
        event.register(((MenuType<SupercoolerMenu>) MENU_TYPES.get("supercooler").get()), SupercoolerScreen::new);
    }
}
