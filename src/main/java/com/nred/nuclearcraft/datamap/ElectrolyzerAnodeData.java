package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ElectrolyzerAnodeData(double efficiency) {
    public static final Codec<ElectrolyzerAnodeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(ElectrolyzerAnodeData::efficiency)
    ).apply(instance, ElectrolyzerAnodeData::new));
}