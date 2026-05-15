package com.nred.nuclearcraft.util;

import net.neoforged.fml.ModList;

public class ModCheck {
    private static boolean initialized = false;

    private static boolean mekanismLoaded = false;
    private static boolean jeiLoaded = false;
    private static boolean emiLoaded = false;
    private static boolean ccLoaded = false;
    private static boolean patchouliLoaded = false;
    private static boolean curiosLoaded = false;
    private static boolean ponderLoaded = false;
    private static boolean sableLoaded = false;

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        ModList loader = ModList.get();

        mekanismLoaded = loader.isLoaded("mekanism");
        jeiLoaded = loader.isLoaded("jei");
        emiLoaded = loader.isLoaded("emi");
        curiosLoaded = loader.isLoaded("curios");
        ccLoaded = loader.isLoaded("computercraft");
        patchouliLoaded = loader.isLoaded("patchouli");
        ponderLoaded = loader.isLoaded("ponder");
        sableLoaded = loader.isLoaded("sable"); // For Create Aeronautics
    }

    public static boolean mekanismLoaded() {
        return mekanismLoaded;
    }

    public static boolean curiosLoaded() {
        return curiosLoaded;
    }

    public static boolean jeiLoaded() {
        return jeiLoaded;
    }

    public static boolean emiLoaded() {
        return emiLoaded;
    }

    public static boolean patchouliLoaded() {
        return patchouliLoaded;
    }

    public static boolean ccLoaded() {
        return ccLoaded;
    }

    public static boolean ponderLoaded() { // TODO should this be replaced with GuideME?
        return ponderLoaded;
    }
    public static boolean sableLoaded() {
        return sableLoaded;
    }
}