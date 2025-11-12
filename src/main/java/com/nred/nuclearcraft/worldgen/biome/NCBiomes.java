package com.nred.nuclearcraft.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.worldgen.biome.NuclearWastelandBiome.wastelandBiome;

public class NCBiomes {
    public static final ResourceKey<Biome> WASTELAND_BIOME = ResourceKey.create(Registries.BIOME, ncLoc("nuclear_wasteland"));

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(WASTELAND_BIOME, wastelandBiome(context));
    }
}