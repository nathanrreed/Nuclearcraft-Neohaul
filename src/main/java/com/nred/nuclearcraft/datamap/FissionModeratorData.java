package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FissionModeratorData(int fluxFactor, double efficiency) {
    public static final Codec<FissionModeratorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("fluxFactor").forGetter(FissionModeratorData::fluxFactor),
            Codec.DOUBLE.fieldOf("efficiency").forGetter(FissionModeratorData::efficiency)
    ).apply(instance, FissionModeratorData::new));
}