package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockVariant;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import static com.nred.nuclearcraft.registration.FluidRegistration.COOLANT_MAP;

public interface ICoolantType extends IMultiblockVariant {
    default ResourceLocation getCoolantId() {
        if (this == FissionCoolantHeaterPortType.STANDARD || this == FissionCoolantHeaterType.STANDARD) {
            return BuiltInRegistries.FLUID.getKey(COOLANT_MAP.get("nak").still.value());
        } else {
            return BuiltInRegistries.FLUID.getKey(COOLANT_MAP.get(getName() + "_nak").still.value());
        }
    }
}