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
        map.put("assembler", new RecipeViewerInfo("assembler", new ScreenRectangle(42, 27, 108, 44), new ScreenPosition(42, 4), List.of(new ScreenPosition(3, 3), new ScreenPosition(23, 3), new ScreenPosition(3, 23), new ScreenPosition(23, 23)), List.of(new ScreenPosition(83, 12))));
        map.put("centrifuge", new RecipeViewerInfo("centrifuge", new ScreenRectangle(36, 27, 120, 44), new ScreenPosition(22, 4), List.of(new ScreenPosition(3, 13)), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(99, 3), new ScreenPosition(59, 23), new ScreenPosition(79, 23), new ScreenPosition(99, 23))));
        map.put("chemical_reactor", new RecipeViewerInfo("chemical_reactor", new ScreenRectangle(28, 27, 136, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(new ScreenPosition(79, 3), new ScreenPosition(99, 3))));
        map.put("crystallizer", new RecipeViewerInfo("crystallizer", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7))));
        map.put("decay_hastener", new RecipeViewerInfo("decay_hastener", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7))));
        map.put("electric_furnace", new RecipeViewerInfo("electric_furnace", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7))));
        map.put("electrolyzer", new RecipeViewerInfo("electrolyzer", new ScreenRectangle(46, 27, 100, 44), new ScreenPosition(22, 3), List.of(new ScreenPosition(3, 13)), List.of(new ScreenPosition(60, 3), new ScreenPosition(80, 3), new ScreenPosition(60, 23), new ScreenPosition(80, 23))));
        map.put("fluid_enricher", new RecipeViewerInfo("fluid_enricher", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(new ScreenPosition(79, 3))));
        map.put("fluid_extractor", new RecipeViewerInfo("fluid_extractor", new ScreenRectangle(38, 27, 116, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7), new ScreenPosition(87, 3))));
        map.put("fluid_infuser", new RecipeViewerInfo("fluid_infuser", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(new ScreenPosition(83, 7))));
        map.put("fluid_mixer", new RecipeViewerInfo("fluid_mixer", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(new ScreenPosition(79, 3))));
        map.put("fuel_reprocessor", new RecipeViewerInfo("fuel_reprocessor", new ScreenRectangle(26, 27, 140, 44), new ScreenPosition(22, 3), List.of(new ScreenPosition(3, 13)), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(99, 3), new ScreenPosition(119, 3), new ScreenPosition(59, 23), new ScreenPosition(79, 23), new ScreenPosition(99, 23), new ScreenPosition(119, 23))));
        map.put("ingot_former", new RecipeViewerInfo("ingot_former", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7))));
        map.put("manufactory", new RecipeViewerInfo("manufactory", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7))));
        map.put("melter", new RecipeViewerInfo("melter", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(59, 3))));
        map.put("pressurizer", new RecipeViewerInfo("pressurizer", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7))));
        map.put("rock_crusher", new RecipeViewerInfo("rock_crusher", new ScreenRectangle(34, 31, 120, 24), new ScreenPosition(22, 4), List.of(new ScreenPosition(3, 3)), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(99, 3))));
        map.put("separator", new RecipeViewerInfo("separator", new ScreenRectangle(37, 27, 116, 32), new ScreenPosition(23, 8), List.of(new ScreenPosition(4, 7)), List.of(new ScreenPosition(63, 7), new ScreenPosition(91, 7))));
        map.put("supercooler", new RecipeViewerInfo("supercooler", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(59, 3))));
        return map;
    }
}