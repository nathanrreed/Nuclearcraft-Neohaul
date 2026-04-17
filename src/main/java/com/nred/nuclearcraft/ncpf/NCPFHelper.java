package com.nred.nuclearcraft.ncpf;

import java.util.Map;

public class NCPFHelper {

    @SuppressWarnings("unchecked")
    public static <T> T get(Map<String, Object> map, String key) {
        return (T) map.get(key);
    }
}