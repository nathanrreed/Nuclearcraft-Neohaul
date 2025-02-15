package com.nred.nuclearcraft.compat.common;

import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeViewerInfoMap {
    public static final Map<String, RecipeViewerInfo> RECIPE_VIEWER_MAP = createRecipeViewers();

    private static Map<String, RecipeViewerInfo> createRecipeViewers() {
        Map<String, RecipeViewerInfo> map = new HashMap<>();
        map.put("alloy_furnace", new RecipeViewerInfo("alloy_furnace", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(new ScreenPosition(83, 7))));
        map.put("manufactory", new RecipeViewerInfo("manufactory", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(32, 8), List.of(new ScreenPosition(13, 7)), List.of(new ScreenPosition(73, 7))));
        return map;
    }
}
