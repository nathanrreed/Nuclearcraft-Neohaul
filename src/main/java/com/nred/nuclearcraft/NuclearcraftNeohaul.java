package com.nred.nuclearcraft;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.config.Config;
import com.nred.nuclearcraft.payload.ButtonPressPayload;
import com.nred.nuclearcraft.payload.FluidClearPayload;
import com.nred.nuclearcraft.registration.Registration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(NuclearcraftNeohaul.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NuclearcraftNeohaul {
    public static final String MODID = "nuclearcraftneohaul";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NuclearcraftNeohaul(IEventBus modEventBus, ModContainer modContainer) {
        Registration.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Client to Server
        registrar.playToServer(ButtonPressPayload.TYPE, ButtonPressPayload.STREAM_CODEC, ButtonPressPayload::handleOnServer);
        registrar.playToServer(FluidClearPayload.TYPE, FluidClearPayload.STREAM_CODEC, FluidClearPayload::handleOnServer);

        // Server to Client

    }
}