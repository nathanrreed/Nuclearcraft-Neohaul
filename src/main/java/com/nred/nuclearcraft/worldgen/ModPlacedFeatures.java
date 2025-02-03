package com.nred.nuclearcraft.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.ORES;

public class ModPlacedFeatures {
    public static Map<String, ResourceKey<PlacedFeature>> ORE_PLACED_KEYS = ores();

    private static Map<String, ResourceKey<PlacedFeature>> ores() {
        Map<String, ResourceKey<PlacedFeature>> map = new java.util.HashMap<>(Map.of());
        for (String ore : ORES) {
            map.put(ore + "_rare", registerKey(ore + "_placed_rare"));
            map.put(ore + "_common", registerKey(ore + "_placed_common"));
        }

        return map;
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        for (String ore : ORES) {
            Random rand = new Random(ore.hashCode());
            register(context, ORE_PLACED_KEYS.get(ore + "_common"), configuredFeatures.getOrThrow(ModConfiguredFeatures.ORE_KEYS.get(ore + "_common")), commonOrePlacement(rand.nextInt(3, 15), HeightRangePlacement.uniform(VerticalAnchor.absolute(rand.nextInt(-32, -10)), VerticalAnchor.absolute(rand.nextInt(0, 60)))));
            register(context, ORE_PLACED_KEYS.get(ore + "_rare"), configuredFeatures.getOrThrow(ModConfiguredFeatures.ORE_KEYS.get(ore + "_rare")), rareOrePlacement(rand.nextInt(6, 10), HeightRangePlacement.triangle(VerticalAnchor.absolute(rand.nextInt(-64, -24)), VerticalAnchor.absolute(rand.nextInt(-20, -8)))));
        }
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, modifiers));
    }
}