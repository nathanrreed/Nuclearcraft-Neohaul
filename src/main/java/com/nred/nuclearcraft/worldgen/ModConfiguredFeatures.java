package com.nred.nuclearcraft.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.ORES;
import static com.nred.nuclearcraft.registration.BlockRegistration.ORE_MAP;

public class ModConfiguredFeatures {
    public static Map<String, ResourceKey<ConfiguredFeature<?, ?>>> ORE_KEYS = ores();

    private static Map<String, ResourceKey<ConfiguredFeature<?, ?>>> ores() {
        Map<String, ResourceKey<ConfiguredFeature<?, ?>>> map = new java.util.HashMap<>(Map.of());
        for (String ore : ORES) {
            map.put(ore + "_common", registerKey(ore + "_common"));
            map.put(ore + "_rare", registerKey(ore + "_rare"));
        }

        return map;
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        for (String ore : ORES) {
            Random rand = new Random(ore.hashCode());
            List<OreConfiguration.@NotNull TargetBlockState> ores = List.of(OreConfiguration.target(stoneReplaceables, ORE_MAP.get(ore).get().defaultBlockState()), OreConfiguration.target(deepslateReplaceables, ORE_MAP.get(ore + "_deepslate").get().defaultBlockState()));
            register(context, ORE_KEYS.get(ore + "_common"), Feature.ORE, new OreConfiguration(ores, rand.nextInt(6, 12)));
            register(context, ORE_KEYS.get(ore + "_rare"), Feature.ORE, new OreConfiguration(ores, rand.nextInt(4, 8)));
        }
    }

    static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature(feature, configuration));
    }
}