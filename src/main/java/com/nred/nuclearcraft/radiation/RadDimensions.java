package com.nred.nuclearcraft.radiation;

import java.util.HashMap;

import static com.nred.nuclearcraft.config.NCConfig.radiation_dim_limits;
import static com.nred.nuclearcraft.config.NCConfig.radiation_dims;

public class RadDimensions {
    public static final HashMap<String, Double> RAD_MAP = new HashMap<>();
    public static final HashMap<String, Double> LIMIT_MAP = new HashMap<>();

    public static void init() {
        for (String dim : radiation_dims) {
            int scorePos = dim.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            RAD_MAP.put(dim.substring(0, scorePos), Double.parseDouble(dim.substring(scorePos + 1)));
        }

        for (String world : radiation_dim_limits) {
            int scorePos = world.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            LIMIT_MAP.put(world.substring(0, scorePos), Double.parseDouble(world.substring(scorePos + 1)));
        }
    }
}