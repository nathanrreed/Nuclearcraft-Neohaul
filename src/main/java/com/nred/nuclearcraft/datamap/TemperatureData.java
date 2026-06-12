package com.nred.nuclearcraft.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.util.DataMapHelper;
import net.minecraft.world.level.material.Fluid;

import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.TEMPERATURE_DATA;

public record TemperatureData(int temperature) {
    public static final Codec<TemperatureData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("temperature").forGetter(TemperatureData::temperature)
    ).apply(instance, TemperatureData::new));

    public static int getTemperature(Fluid fluid) {
        TemperatureData data = DataMapHelper.getData(fluid, TEMPERATURE_DATA);
        if (data != null) {
            return data.temperature();
        } else {
            return fluid.getFluidType().getTemperature();
        }
    }
}