package com.nred.nuclearcraft.info;

import java.util.Map;

public class Radiation {
    public static Map<String, Long> RAD_MAP = Map.of(
            "uranium_233", 600000000L, //TODO make correct
            "uranium_233_c", 60000L,
            "uranium_233_ox", 60L,
            "uranium_233_ni", 600000000L,
            "uranium_233_za", 6L,
            "uranium_235", 1000000000L,
            "uranium_235_c", 1000000L,
            "uranium_235_ox", 100000L,
            "uranium_235_ni", 10L,
            "uranium_235_za", 10L
    );
}
