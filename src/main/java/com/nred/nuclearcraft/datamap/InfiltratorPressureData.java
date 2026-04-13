package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record InfiltratorPressureData(double efficiency) {
    public static final Codec<InfiltratorPressureData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(InfiltratorPressureData::efficiency)
    ).apply(instance, InfiltratorPressureData::new));
}