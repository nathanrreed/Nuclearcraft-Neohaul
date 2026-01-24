package com.nred.nuclearcraft.radiation;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.HashSet;

import static com.nred.nuclearcraft.config.NCConfig.*;

public class RadBiomes {
    public static final Object2DoubleMap<Biome> RAD_MAP = new Object2DoubleOpenHashMap<>();
    public static final Object2DoubleMap<Biome> LIMIT_MAP = new Object2DoubleOpenHashMap<>();
    public static final HashSet<Biome> DIM_BLACKLIST = new HashSet<>();

    public static void init(MinecraftServer server) {
        HolderGetter.Provider provider = server.getLevel(Level.OVERWORLD).registryAccess().asGetterLookup();
        for (String biomeInfo : radiation_biomes) {
            int scorePos = biomeInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            provider.get(Registries.BIOME, ResourceKey.create(Registries.BIOME, ResourceLocation.parse(biomeInfo.substring(0, scorePos)))).ifPresent(biome -> RAD_MAP.put(biome.value(), Double.parseDouble(biomeInfo.substring(scorePos + 1))));
        }

        for (String biomeInfo : radiation_biome_limits) {
            int scorePos = biomeInfo.lastIndexOf('_');
            if (scorePos == -1) {
                continue;
            }
            provider.get(Registries.BIOME, ResourceKey.create(Registries.BIOME, ResourceLocation.parse(biomeInfo.substring(0, scorePos)))).ifPresent(biome -> LIMIT_MAP.put(biome.value(), Double.parseDouble(biomeInfo.substring(scorePos + 1))));
        }

        for (String dim : radiation_from_biomes_dims_blacklist) {
            provider.get(Registries.BIOME, ResourceKey.create(Registries.BIOME, ResourceLocation.parse(dim))).ifPresent(biome -> DIM_BLACKLIST.add(biome.value()));
        }
    }
}