package com.nred.nuclearcraft;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.config.Config;
import com.nred.nuclearcraft.config.Config2;
import com.nred.nuclearcraft.payload.ButtonPressPayload;
import com.nred.nuclearcraft.payload.FluidClearPayload;
import com.nred.nuclearcraft.payload.RecipeSetPayload;
import com.nred.nuclearcraft.registration.Registration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

import static net.neoforged.neoforge.common.NeoForgeMod.enableMilkFluid;

@Mod(NuclearcraftNeohaul.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NuclearcraftNeohaul {
    public static final String MODID = "nuclearcraftneohaul";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NuclearcraftNeohaul(IEventBus modEventBus, ModContainer modContainer) {
        enableMilkFluid();
        Registration.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.STARTUP, Config2.SPEC); //TODO MERGE
//        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        // Client to Server
        registrar.playToServer(ButtonPressPayload.TYPE, ButtonPressPayload.STREAM_CODEC, ButtonPressPayload::handleOnServer);
        registrar.playToServer(FluidClearPayload.TYPE, FluidClearPayload.STREAM_CODEC, FluidClearPayload::handleOnServer);

        // Server to Client
        registrar.playToClient(RecipeSetPayload.TYPE, RecipeSetPayload.STREAM_CODEC, RecipeSetPayload::handleOnClient);
    }
}

