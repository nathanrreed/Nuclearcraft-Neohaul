package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.payload.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) { // TODO is this the right event type
        PlacementRule.init();
    }

    @SubscribeEvent
    public static void postInit(ServerStartingEvent event) { // TODO is this the right event type
        PlacementRule.postInit();
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Client to Server
        registrar.playToServer(ButtonPressPayload.TYPE, ButtonPressPayload.STREAM_CODEC, ButtonPressPayload::handleOnServer);
        registrar.playToServer(ClearPayload.TYPE, ClearPayload.STREAM_CODEC, ClearPayload::handleOnServer);

        // Server to Client
        registrar.playToClient(RecipeSetPayload.TYPE, RecipeSetPayload.STREAM_CODEC, RecipeSetPayload::handleOnClient);
        registrar.playToClient(TurbineRenderPayload.TYPE, TurbineRenderPayload.STREAM_CODEC, TurbineRenderPayload::handleOnClient);
        registrar.playToClient(TurbineUpdatePayload.TYPE, TurbineUpdatePayload.STREAM_CODEC, TurbineUpdatePayload::handleOnClient);
        registrar.playToClient(SolidFissionPayload.TYPE, SolidFissionPayload.STREAM_CODEC, SolidFissionPayload::handleOnClient);
        registrar.playToClient(SaltFissionPayload.TYPE, SaltFissionPayload.STREAM_CODEC, SaltFissionPayload::handleOnClient);
    }
}