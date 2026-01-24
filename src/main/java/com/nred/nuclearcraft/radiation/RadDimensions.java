package com.nred.nuclearcraft.radiation;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

import static com.nred.nuclearcraft.config.NCConfig.radiation_dim_limits;
import static com.nred.nuclearcraft.config.NCConfig.radiation_dims;

public class RadDimensions {
    public static final HashMap<ResourceLocation, Double> RAD_MAP = new HashMap<>();
    public static final HashMap<ResourceLocation, Double> LIMIT_MAP = new HashMap<>();

    public static void init() {
        for (String dim : radiation_dims) {
            int scorePos = dim.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            RAD_MAP.put(ResourceLocation.parse(dim.substring(0, scorePos)), Double.parseDouble(dim.substring(scorePos + 1)));
        }

        for (String level : radiation_dim_limits) {
            int scorePos = level.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            LIMIT_MAP.put(ResourceLocation.parse(level.substring(0, scorePos)), Double.parseDouble(level.substring(scorePos + 1)));
        }
    }
}