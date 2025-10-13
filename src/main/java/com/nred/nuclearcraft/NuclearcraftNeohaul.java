package com.nred.nuclearcraft;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.config.Config;
import com.nred.nuclearcraft.config.Config2;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.registration.Registration;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

import static net.neoforged.neoforge.common.NeoForgeMod.enableMilkFluid;

@Mod(NuclearcraftNeohaul.MODID)
public class NuclearcraftNeohaul {
    public static final String MODID = "nuclearcraftneohaul";
    private static final Logger LOGGER = LogUtils.getLogger();

//    public static NuclearcraftNeohaul instance;

    public static final BlockHighlightTracker blockOverlayTracker = new BlockHighlightTracker();
    public static final NCRecipes recipes = new NCRecipes();


    public NuclearcraftNeohaul(IEventBus modEventBus, ModContainer modContainer) {
        enableMilkFluid();
        Registration.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.STARTUP, Config2.SPEC); //TODO MERGE

        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::addReloadListeners);
        modEventBus.addListener(this::commonSetup);

//        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private ReloadListener recipeCacheManager;

    public void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(getRecipeCacheManager());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        setRecipeCacheManager(new ReloadListener());
    }


    public class ReloadListener implements ResourceManagerReloadListener {
        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            LOGGER.debug("Recipes changed"); // TODO clear cache
            NCRecipes.clearRecipes();
        }
    }

    public ReloadListener getRecipeCacheManager() {
        return recipeCacheManager;
    }

    private void setRecipeCacheManager(ReloadListener manager) {
        if (recipeCacheManager == null) {
            recipeCacheManager = manager;
        } else {
            LOGGER.warn("Recipe cache manager has already been set.");
        }
    }
}