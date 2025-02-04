package com.nred.nuclearcraft.helpers;

import com.nred.nuclearcraft.info.Fluids;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Concat {
    @SafeVarargs
    public static List<String> fluidKeys(Map<String, Fluids>... args) {
        return Arrays.stream(args).flatMap(m -> m.keySet().stream()).toList();
    }
    @SafeVarargs
    public static List<Map.Entry<String, Fluids>> fluidEntries(Map<String, Fluids>... args) {
        return Arrays.stream(args).flatMap(m -> m.entrySet().stream()).toList();
    }
    @SafeVarargs
    public static List<Fluids> fluidValues(Map<String, Fluids>... args) {
        return Arrays.stream(args).flatMap(m -> m.values().stream()).toList();
    }
}
