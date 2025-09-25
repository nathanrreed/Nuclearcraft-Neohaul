package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.block.batteries.BatteryEntity;
import com.nred.nuclearcraft.block.batteries.BatteryRenderer;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.menu.processor.*;
import com.nred.nuclearcraft.render.TurbineRotorRenderer;
import com.nred.nuclearcraft.screen.processor.*;
import com.nred.nuclearcraft.screen.turbine.TurbineControllerScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_CONTROLLER;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;
import static com.nred.nuclearcraft.registration.MenuRegistration.TURBINE_CONTROLLER_MENU_TYPE;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.register(new SoundHandler());
    }

    @SubscribeEvent
    public static void fluidLoad(RegisterClientExtensionsEvent event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            event.registerFluidType(fluid.client, fluid.type);
        }
    }

    @SubscribeEvent
    public static void fluidColoring(final FMLCommonSetupEvent event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            ItemBlockRenderTypes.setRenderLayer(fluid.still.get(), RenderType.TRANSLUCENT);
            ItemBlockRenderTypes.setRenderLayer(fluid.flowing.get(), RenderType.TRANSLUCENT);
        }
    }

    @SubscribeEvent
    public static void blockEntityRenderer(final FMLCommonSetupEvent event) {
        for (DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BatteryEntity>> type : BATTERY_ENTITY_TYPE.values()) {
            BlockEntityRenderers.register(type.get(), BatteryRenderer::new);
        }
        BlockEntityRenderers.register(TURBINE_CONTROLLER.get(), TurbineRotorRenderer::new);
    }

    @SubscribeEvent
    public static void bucketColoring(RegisterColorHandlersEvent.Item event) {
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, CUSTOM_FLUID, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, FISSION_FUEL_MAP)) {
            event.register(((stack, tintIndex) -> tintIndex == 0 ? -1 : fluid.client.getTintColor()), fluid.bucket.asItem());
        }
    }

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

        event.register(TURBINE_CONTROLLER_MENU_TYPE.get(), TurbineControllerScreen::new);
    }
}
