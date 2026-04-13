package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ElectrolyzerElectrolyteData(double efficiency, String group) {
    public static final Codec<ElectrolyzerElectrolyteData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(ElectrolyzerElectrolyteData::efficiency),
            Codec.STRING.fieldOf("group").forGetter(ElectrolyzerElectrolyteData::group)
    ).apply(instance, ElectrolyzerElectrolyteData::new));

    public double efficiency(String group) {
        return this.group.equals(group) ? efficiency : 0;
    }
}