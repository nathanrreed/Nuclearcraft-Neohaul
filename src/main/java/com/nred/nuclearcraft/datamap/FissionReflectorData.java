package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FissionReflectorData(double efficiency, double reflectivity) {
    public static final Codec<FissionReflectorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(FissionReflectorData::efficiency),
            Codec.DOUBLE.fieldOf("reflectivity").forGetter(FissionReflectorData::reflectivity)
    ).apply(instance, FissionReflectorData::new));
}