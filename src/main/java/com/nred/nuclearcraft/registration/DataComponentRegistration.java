package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.capability.radiation.resistance.RadiationResistanceItem;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.nred.nuclearcraft.registration.Registers.DATA_COMPONENT_TYPES;

public class DataComponentRegistration {
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RadiationResistanceItem>> RADIATION_RESISTANCE_ITEM = DATA_COMPONENT_TYPES.registerComponentType(
            "radiation_resistance_item",
            builder -> builder
                    .persistent(RadiationResistanceItem.CODEC)
                    .networkSynchronized(RadiationResistanceItem.STREAM_CODEC)
    );

    public static void init() {
    }
}