package com.nred.nuclearcraft.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class Lang {

    public static String localize(String unloc, Object... args) {
        return Component.translatable(unloc, args).getString();
    }

    public static String localize(String unloc) {
        return Component.translatable(unloc).getString();
    }

    public static String[] localizeList(String unloc, String... args) {
        return splitList(localize(unloc, (Object[]) args));
    }

    public static String[] localizeList(String unloc) {
        return splitList(localize(unloc));
    }

    public static List<String> localizeAll(List<String> unloc) {
        List<String> ret = new ArrayList<>();
        for (String s : unloc) {
            ret.add(localize(s));
        }
        return ret;
    }

    public static String[] localizeAll(Lang lang, String... unloc) {
        String[] ret = new String[unloc.length];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = Lang.localize(unloc[i]);
        }
        return ret;
    }

    public static String[] splitList(String list) {
        return list.split("\\|");
    }

    public static String[] splitList(String list, String split) {
        return list.split(split);
    }

    public static boolean canLocalize(String unloc) {
        return ComponentUtils.isTranslationResolvable(Component.translatable(unloc));
    }
}
