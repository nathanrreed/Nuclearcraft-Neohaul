package com.nred.nuclearcraft.util;

import com.mojang.logging.LogUtils;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

import static com.nred.nuclearcraft.config.NCConfig.ctrl_info;

public class NCUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static Logger getLogger() {
        return LOGGER;
    }

    private static boolean isShiftKeyDown() {
        return Screen.hasShiftDown();
    }

    private static boolean isCtrlKeyDown() {
        return Screen.hasControlDown();
    }

    public static boolean isInfoKeyDown() {
        return ctrl_info ? NCUtil.isCtrlKeyDown() : NCUtil.isShiftKeyDown();
    }

    public static boolean isModifierKeyDown() {
        return NCUtil.isCtrlKeyDown() || NCUtil.isShiftKeyDown();
    }

    private static final Object2ObjectMap<String, String> SHORT_MOD_ID_MAP = new Object2ObjectOpenHashMap<>();

    static {
        putShortModId(NuclearcraftNeohaul.MODID, "nc");
    }

    public static void putShortModId(String modId, String shortModId) {
        SHORT_MOD_ID_MAP.put(modId, shortModId);
    }

    public static String getShortModId(String modId) {
        String shortModId = SHORT_MOD_ID_MAP.get(modId);
        return shortModId == null ? modId : shortModId;
    }

    public static RecipeManager getRecipeManager(Level level) {
        if (level != null) {
            return level.getRecipeManager();
        }
        if (FMLEnvironment.dist.isClient()) {
            return net.minecraft.client.Minecraft.getInstance().level != null ? net.minecraft.client.Minecraft.getInstance().level.getRecipeManager() : null;
        } else {
            return ServerLifecycleHooks.getCurrentServer() != null ? ServerLifecycleHooks.getCurrentServer().getRecipeManager() : null;
        }
    }
}
