package com.nred.nuclearcraft.worldgen.dimension;

import com.nred.nuclearcraft.worldgen.biome.NuclearWastelandBiome;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;

import java.util.OptionalLong;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.worldgen.biome.NCBiomes.WASTELAND_BIOME;

public class NCDimensions {
    public static final ResourceKey<LevelStem> WASTELAND_KEY = ResourceKey.create(Registries.LEVEL_STEM, ncLoc("nuclear_wasteland"));
    public static final ResourceKey<Level> WASTELAND_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION, ncLoc("nuclear_wasteland"));
    public static final ResourceKey<DimensionType> WASTELAND_DIMENSION = ResourceKey.create(Registries.DIMENSION_TYPE, ncLoc("nuclear_wasteland_type"));
    public static final ResourceKey<NoiseGeneratorSettings> WASTELAND_NOISE = ResourceKey.create(Registries.NOISE_SETTINGS, ncLoc("nuclear_wasteland")
    );

    public static void bootstrapType(BootstrapContext<DimensionType> context) {
        context.register(WASTELAND_DIMENSION, new DimensionType(OptionalLong.empty(),
                true, false, false, true, 1.0, true, false, -64, 384, 384,
                BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0f,
                new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)));
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(WASTELAND_BIOME)),
                noiseGenSettings.getOrThrow(WASTELAND_NOISE));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(WASTELAND_DIMENSION), wrappedChunkGenerator);

        context.register(WASTELAND_KEY, stem);
    }

    public static void bootstrapNoise(BootstrapContext<NoiseGeneratorSettings> context) {
        NoiseGeneratorSettings overworld = NoiseGeneratorSettings.overworld(context, false, false);
        context.register(WASTELAND_NOISE, new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 1, 2),
                Blocks.STONE.defaultBlockState(),
                CUSTOM_FLUID_MAP.get("corium").block.get().defaultBlockState(),
                new NoiseRouter( // Mixing Overworld with no caves
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.constant(0.7),
                        overworld.noiseRouter().vegetation(),
                        DensityFunctions.zero(),
                        DensityFunctions.constant(0.7),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        overworld.noiseRouter().finalDensity(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero(),
                        DensityFunctions.zero()
                ),
                NuclearWastelandBiome.makeRules(),
                new OverworldBiomeBuilder().spawnTarget(),
                63,
                false,
                false,
                true,
                false
        ));
    }
}