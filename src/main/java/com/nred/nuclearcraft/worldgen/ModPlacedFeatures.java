package com.nred.nuclearcraft.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.ORES;
import static com.nred.nuclearcraft.worldgen.ModConfiguredFeatures.LAKE_WASTELAND_PORTAL_KEY;

public class ModPlacedFeatures {
    public static Map<String, ResourceKey<PlacedFeature>> ORE_PLACED_KEYS = ores();
    public static ResourceKey<PlacedFeature> GLOWING_MUSHROOM_PLACED_KEY = registerKey("glowing_mushroom_placed");
    public static ResourceKey<PlacedFeature> LAKE_WASTELAND_PORTAL = registerKey("lake_wasteland_portal");

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
        return orePlacement(RarityFilter.onAverageOnceEvery(chance / 2), heightRange);
    }

    record OreInfo(int min_y, int max_y, int ore_rate, int ore_size) {
    }

    static final Map<String, OreInfo> oreInfoMap = Map.of(
            "tin", new OreInfo(-16, 50, 4, 6),
            "lead", new OreInfo(-16, 36, 6, 6),
            "thorium", new OreInfo(-32, 32, 3, 3),
            "uranium", new OreInfo(-32, 32, 6, 3),
            "boron", new OreInfo(-42, 28, 6, 4),
            "lithium", new OreInfo(-52, 28, 6, 4),
            "magnesium", new OreInfo(-64, 24, 4, 5)

    );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        for (String ore : ORES) {
            register(context, ORE_PLACED_KEYS.get(ore + "_common"), configuredFeatures.getOrThrow(ModConfiguredFeatures.ORE_KEYS.get(ore + "_common")), commonOrePlacement(oreInfoMap.get(ore).ore_rate, HeightRangePlacement.triangle(VerticalAnchor.absolute(oreInfoMap.get(ore).min_y), VerticalAnchor.absolute(oreInfoMap.get(ore).max_y))));
            register(context, ORE_PLACED_KEYS.get(ore + "_rare"), configuredFeatures.getOrThrow(ModConfiguredFeatures.ORE_KEYS.get(ore + "_rare")), rareOrePlacement(oreInfoMap.get(ore).ore_rate, HeightRangePlacement.uniform(VerticalAnchor.absolute(oreInfoMap.get(ore).min_y), VerticalAnchor.absolute(oreInfoMap.get(ore).max_y))));
        }

        register(context, GLOWING_MUSHROOM_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GLOWING_MUSHROOM_KEY), VegetationPlacements.getMushroomPlacement(32, null));

        register(
                context,
                LAKE_WASTELAND_PORTAL,
                configuredFeatures.getOrThrow(LAKE_WASTELAND_PORTAL_KEY),
                List.of(RarityFilter.onAverageOnceEvery(200),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome())
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, modifiers));
    }
}