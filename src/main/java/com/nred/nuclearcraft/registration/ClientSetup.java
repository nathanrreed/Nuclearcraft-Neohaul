package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.info.Fluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;

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
}
