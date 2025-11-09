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
        map.put("alloy_furnace", new RecipeViewerInfo("alloy_furnace", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(), List.of(new ScreenPosition(83, 7)), List.of()));
        map.put("assembler", new RecipeViewerInfo("assembler", new ScreenRectangle(42, 27, 108, 44), new ScreenPosition(42, 4), List.of(new ScreenPosition(3, 3), new ScreenPosition(23, 3), new ScreenPosition(3, 23), new ScreenPosition(23, 23)), List.of(), List.of(new ScreenPosition(83, 12)), List.of()));
        map.put("centrifuge", new RecipeViewerInfo("centrifuge", new ScreenRectangle(36, 27, 120, 44), new ScreenPosition(22, 3), List.of(),List.of(new ScreenPosition(3, 13)), List.of(), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(99, 3), new ScreenPosition(59, 23), new ScreenPosition(79, 23), new ScreenPosition(99, 23))));
        map.put("chemical_reactor", new RecipeViewerInfo("chemical_reactor", new ScreenRectangle(28, 27, 136, 32), new ScreenPosition(42, 7), List.of(),List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(), List.of(new ScreenPosition(79, 3), new ScreenPosition(107, 3))));
        map.put("crystallizer", new RecipeViewerInfo("crystallizer", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7)), List.of()));
        map.put("decay_hastener", new RecipeViewerInfo("decay_hastener", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(63, 7)), List.of()));
        map.put("electric_furnace", new RecipeViewerInfo("electric_furnace", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(63, 7)), List.of()));
        map.put("electrolyzer", new RecipeViewerInfo("electrolyzer", new ScreenRectangle(46, 27, 100, 44), new ScreenPosition(22, 3), List.of(), List.of(new ScreenPosition(3, 13)), List.of(), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(59, 23), new ScreenPosition(79, 23))));
        map.put("fluid_enricher", new RecipeViewerInfo("fluid_enricher", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(23, 7)), List.of(), List.of(new ScreenPosition(79, 3))));
        map.put("fluid_extractor", new RecipeViewerInfo("fluid_extractor", new ScreenRectangle(38, 27, 116, 32), new ScreenPosition(22, 7), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(63, 7)), List.of(new ScreenPosition(87, 3))));
        map.put("fluid_infuser", new RecipeViewerInfo("fluid_infuser", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 8), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(23, 7)), List.of(new ScreenPosition(83, 7)), List.of()));
        map.put("fluid_mixer", new RecipeViewerInfo("fluid_mixer", new ScreenRectangle(42, 27, 108, 32), new ScreenPosition(42, 7), List.of(),List.of(new ScreenPosition(3, 7), new ScreenPosition(23, 7)), List.of(), List.of(new ScreenPosition(79, 3))));
        map.put("fuel_reprocessor", new RecipeViewerInfo("fuel_reprocessor", new ScreenRectangle(26, 27, 140, 44), new ScreenPosition(22, 3), List.of(new ScreenPosition(3, 13)), List.of(), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(99, 3), new ScreenPosition(59, 23), new ScreenPosition(79, 23), new ScreenPosition(99, 23), new ScreenPosition(119, 3), new ScreenPosition(119, 23)), List.of()));
        map.put("ingot_former", new RecipeViewerInfo("ingot_former", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(), List.of(new ScreenPosition(3, 7)), List.of(new ScreenPosition(63, 7)), List.of()));
        map.put("manufactory", new RecipeViewerInfo("manufactory", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(63, 7)), List.of()));
        map.put("melter", new RecipeViewerInfo("melter", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(), List.of(), List.of(new ScreenPosition(59, 3))));
        map.put("pressurizer", new RecipeViewerInfo("pressurizer", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(63, 7)), List.of()));
        map.put("rock_crusher", new RecipeViewerInfo("rock_crusher", new ScreenRectangle(34, 31, 120, 24), new ScreenPosition(22, 4), List.of(new ScreenPosition(3, 3)), List.of(), List.of(new ScreenPosition(59, 3), new ScreenPosition(79, 3), new ScreenPosition(99, 3)), List.of()));
        map.put("separator", new RecipeViewerInfo("separator", new ScreenRectangle(37, 27, 116, 32), new ScreenPosition(23, 7), List.of(new ScreenPosition(4, 7)), List.of(), List.of(new ScreenPosition(64, 7), new ScreenPosition(92, 7)), List.of()));
        map.put("supercooler", new RecipeViewerInfo("supercooler", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(59, 3))));

        map.put("collector_item", new RecipeViewerInfo("collector_item", new ScreenRectangle(36, 29, 88, 28), new ScreenPosition(24, 7), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(65, 5)), List.of()));
        map.put("collector_fluid", new RecipeViewerInfo("collector_fluid", new ScreenRectangle(36, 29, 88, 28), new ScreenPosition(24, 7), List.of(new ScreenPosition(5, 5)), List.of(), List.of(), List.of(new ScreenPosition(61, 1))));

        map.put("decay_generator", new RecipeViewerInfo("decay_generator", new ScreenRectangle(52, 27, 88, 32), new ScreenPosition(22, 8), List.of(new ScreenPosition(3, 7)), List.of(), List.of(new ScreenPosition(63, 7)), List.of()));

        map.put("turbine", new RecipeViewerInfo("turbine", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(61, 1))));

        map.put("solid_fission", new RecipeViewerInfo("solid_fission", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(65, 5)), List.of()));
        map.put("salt_fission", new RecipeViewerInfo("salt_fission", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(61, 1))));
        map.put("fission_irradiator", new RecipeViewerInfo("fission_irradiating", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(65, 5)), List.of()));
        map.put("fission_vent", new RecipeViewerInfo("fission_heating", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(61, 1))));
        map.put("fission_emergency_cooling", new RecipeViewerInfo("fission_emergency_cooling", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(61, 1))));
        map.put("salt_cooling", new RecipeViewerInfo("coolant_heater", new ScreenRectangle(40, 29, 108, 28), new ScreenPosition(44, 6), List.of(new ScreenPosition(5, 5)), List.of(new ScreenPosition(25, 5)), List.of(), List.of(new ScreenPosition(81, 1))));

        map.put("condenser", new RecipeViewerInfo("condenser", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(61, 1))));
        map.put("heat_exchanger", new RecipeViewerInfo("heat_exchanger", new ScreenRectangle(50, 29, 88, 28), new ScreenPosition(24, 6), List.of(), List.of(new ScreenPosition(5, 5)), List.of(), List.of(new ScreenPosition(61, 1))));

        map.put("multiblock_distiller", new RecipeViewerInfo("multiblock_distiller", new ScreenRectangle(16, 27, 161, 44), new ScreenPosition(42, 3), List.of(), List.of(new ScreenPosition(3, 13), new ScreenPosition(23, 13)), List.of(), List.of(new ScreenPosition(79, 3), new ScreenPosition(99, 3), new ScreenPosition(119, 3), new ScreenPosition(139, 3), new ScreenPosition(79, 23), new ScreenPosition(99, 23), new ScreenPosition(119, 23), new ScreenPosition(139, 23))));
        map.put("multiblock_electrolyzer", new RecipeViewerInfo("multiblock_electrolyzer", new ScreenRectangle(0, 27, 162, 44), new ScreenPosition(42, 3), List.of(new ScreenPosition(3, 3), new ScreenPosition(23, 3)), List.of(new ScreenPosition(3, 23), new ScreenPosition(23, 23)), List.of(new ScreenPosition(81, 3), new ScreenPosition(101, 3), new ScreenPosition(81, 23), new ScreenPosition(101, 23)), List.of(new ScreenPosition(121, 3), new ScreenPosition(141, 3), new ScreenPosition(121, 23), new ScreenPosition(141, 23))));
        map.put("multiblock_infiltrator", new RecipeViewerInfo("multiblock_infiltrator", new ScreenRectangle(42, 27, 108, 44), new ScreenPosition(42, 4), List.of(new ScreenPosition(3, 4), new ScreenPosition(23, 3)), List.of(new ScreenPosition(3, 23), new ScreenPosition(23, 23)), List.of(new ScreenPosition(83, 13)), List.of()));

        return map;
    }
}