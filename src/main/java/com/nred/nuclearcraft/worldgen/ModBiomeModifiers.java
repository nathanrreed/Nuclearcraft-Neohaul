package com.nred.nuclearcraft.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Map;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.info.Names.ORES;
import static com.nred.nuclearcraft.worldgen.biome.NCBiomes.WASTELAND_BIOME;

public class ModBiomeModifiers {
    static Map<String, ResourceKey<BiomeModifier>> ADD_ORES = ores();
    static ResourceKey<BiomeModifier> ADD_GLOWING_MUSHROOM = registerKey("add_glowing_mushroom");
    static ResourceKey<BiomeModifier> ADD_GLOWING_MUSHROOM_NETHER = registerKey("add_glowing_mushroom_nether");

    private static Map<String, ResourceKey<BiomeModifier>> ores() {
        Map<String, ResourceKey<BiomeModifier>> map = new java.util.HashMap<>(Map.of());
        for (String ore : ORES) {
            map.put(ore + "_rare", registerKey(ore + "_rare"));
            map.put(ore + "_common", registerKey(ore + "_common"));
        }
        return map;
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        for (String ore : ORES) {
            context.register(ADD_ORES.get(ore + "_common"), new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.ORE_PLACED_KEYS.get(ore + "_common"))), GenerationStep.Decoration.UNDERGROUND_ORES));
            context.register(ADD_ORES.get(ore + "_rare"), new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.ORE_PLACED_KEYS.get(ore + "_rare"))), GenerationStep.Decoration.UNDERGROUND_ORES));
        }

        context.register(ADD_GLOWING_MUSHROOM, new BiomeModifiers.AddFeaturesBiomeModifier(HolderSet.direct(biomes.getOrThrow(WASTELAND_BIOME)), HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.GLOWING_MUSHROOM_PLACED_KEY)), GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_GLOWING_MUSHROOM_NETHER, new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(BiomeTags.IS_NETHER), HolderSet.direct(placedFeature.getOrThrow(ModPlacedFeatures.GLOWING_MUSHROOM_PLACED_KEY)), GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    public static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }
}