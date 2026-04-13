package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MachineSieveAssemblyData(double efficiency) {
    public static final Codec<MachineSieveAssemblyData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(MachineSieveAssemblyData::efficiency)
    ).apply(instance, MachineSieveAssemblyData::new));
}