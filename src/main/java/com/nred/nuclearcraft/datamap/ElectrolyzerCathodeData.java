package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ElectrolyzerCathodeData(double efficiency) {
    public static final Codec<ElectrolyzerCathodeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(ElectrolyzerCathodeData::efficiency)
    ).apply(instance, ElectrolyzerCathodeData::new));
}