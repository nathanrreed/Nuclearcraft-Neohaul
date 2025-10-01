package com.nred.nuclearcraft;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.config.Config;
import com.nred.nuclearcraft.config.Config2;
import com.nred.nuclearcraft.registration.Registration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

import static net.neoforged.neoforge.common.NeoForgeMod.enableMilkFluid;

@Mod(NuclearcraftNeohaul.MODID)
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
}