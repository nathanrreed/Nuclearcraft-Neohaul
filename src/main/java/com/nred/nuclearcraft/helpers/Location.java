package com.nred.nuclearcraft.helpers;

import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class Location {
    public static ResourceLocation neoLoc(String location) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", location);
    }

    public static ResourceLocation cLoc(String location) {
        return ResourceLocation.fromNamespaceAndPath("c", location);
    }

    public static ResourceLocation ncLoc(String location) {
        return ResourceLocation.fromNamespaceAndPath(MODID, location);
    }
}
