package com.nred.nuclearcraft.radiation;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;

import static com.nred.nuclearcraft.config.NCConfig.radiation_dim_limits;
import static com.nred.nuclearcraft.config.NCConfig.radiation_dims;

public class RadDimensions {
    public static final Int2DoubleMap RAD_MAP = new Int2DoubleOpenHashMap();
    public static final Int2DoubleMap LIMIT_MAP = new Int2DoubleOpenHashMap();

    public static void init() { // TODO call
        for (String world : radiation_dims) {
            int scorePos = world.indexOf('_');
            if (scorePos == -1) {
                continue;
            }
            RAD_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
        }

        for (String world : radiation_dim_limits) {
            int scorePos = world.indexOf('_');
            if (scorePos == -1) {
                continue;
            }
            LIMIT_MAP.put(Integer.parseInt(world.substring(0, scorePos)), Double.parseDouble(world.substring(scorePos + 1)));
        }
    }
}