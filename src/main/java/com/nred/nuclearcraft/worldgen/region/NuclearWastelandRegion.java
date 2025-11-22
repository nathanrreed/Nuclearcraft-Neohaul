package com.nred.nuclearcraft.worldgen.region;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils.*;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

import static com.nred.nuclearcraft.worldgen.biome.NCBiomes.WASTELAND_BIOME;

public class NuclearWastelandRegion extends Region {
    public NuclearWastelandRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<com.mojang.datafixers.util.Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();
        new ParameterPointListBuilder()
                .temperature(Temperature.HOT)
                .humidity(Humidity.ARID)
                .continentalness(Continentalness.NEAR_INLAND)
                .erosion(Erosion.EROSION_2)
                .depth(Depth.SURFACE)
                .weirdness(Weirdness.MID_SLICE_NORMAL_DESCENDING)
                .offset(0L)
                .build().forEach(point -> builder.add(point, WASTELAND_BIOME));

        // Add our points to the mapper
        builder.build().forEach(mapper);
    }
}