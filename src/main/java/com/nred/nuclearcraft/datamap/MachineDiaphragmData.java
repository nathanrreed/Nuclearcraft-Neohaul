package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MachineDiaphragmData(double efficiency, double contact_factor) {
    public static final Codec<MachineDiaphragmData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("efficiency").forGetter(MachineDiaphragmData::efficiency),
            Codec.DOUBLE.fieldOf("contact_factor").forGetter(MachineDiaphragmData::contact_factor)
    ).apply(instance, MachineDiaphragmData::new));
}