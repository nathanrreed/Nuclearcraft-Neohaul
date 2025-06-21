package com.nred.nuclearcraft.util;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public class I18nHelper {

    private static final Lazy<Int2IntMap> PLURAL_RULE_MAP = new Lazy<>(() -> {
        Int2IntMap map = new Int2IntOpenHashMap();

        if (Lang.canLocalize("nc.sf.plural_rule")) {
            String str = Lang.localize("nc.sf.plural_rule");
            String[] split = str.split(",", -1);

            map.defaultReturnValue(Integer.parseInt(split[0]));

            for (int i = 1; i < split.length; ++i) {
                String[] entry = split[i].split(":", -1);
                map.put(Integer.parseInt(entry[0]), Integer.parseInt(entry[1]));
            }
        }

        return map;
    });

    public static String getPluralForm(String unlocBase, int count, Object... args) {
        return Lang.localize(unlocBase + PLURAL_RULE_MAP.get().get(count), args);
    }
}
