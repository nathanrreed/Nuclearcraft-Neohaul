package com.nred.nuclearcraft;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.registration.Registration;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import com.nred.nuclearcraft.util.NCUtil;
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
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final BlockHighlightTracker blockOverlayTracker = new BlockHighlightTracker();
    public static final NCRecipes recipes = new NCRecipes();


    public NuclearcraftNeohaul(IEventBus modEventBus, ModContainer modContainer) {
        enableMilkFluid();
        Registration.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, NCConfig.SPEC); // TODO split into client and server

        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::addReloadListeners);
        modEventBus.addListener(this::commonSetup);
    }

    private ReloadListener recipeCacheManager;

    public void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(getRecipeCacheManager());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        setRecipeCacheManager(new ReloadListener());
    }

    public static class ReloadListener implements ResourceManagerReloadListener {
        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            NCRecipes.getHandlers().forEach(e -> e.postReload(NCUtil.getRecipeManager(null)));
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