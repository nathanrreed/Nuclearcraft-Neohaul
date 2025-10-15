package com.nred.nuclearcraft.util;

import net.neoforged.fml.ModList;

public class ModCheck {
    private static boolean initialized = false;

    private static boolean mekanismLoaded = false;
    private static boolean jeiLoaded = false;
    private static boolean emiLoaded = false;
    private static boolean ccLoaded = false;

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        ModList loader = ModList.get();

        mekanismLoaded = loader.isLoaded("mekanism");
        jeiLoaded = loader.isLoaded("jei");
        emiLoaded = loader.isLoaded("emi");
        ccLoaded = loader.isLoaded("computercraft");
    }

    public static boolean mekanismLoaded() {
        return mekanismLoaded;
    }

    public static boolean jeiLoaded() {
        return jeiLoaded;
    }

    public static boolean emiLoaded() {
        return emiLoaded;
    }
    public static boolean ccLoaded() {
        return ccLoaded;
    }
}