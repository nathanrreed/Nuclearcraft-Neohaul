package com.nred.nuclearcraft.registration;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.SoundRegistration.rad_poisoning;

public class ModEnumParams {
    public static Object getDamageEffectsParameter(int idx, Class<?> type) { // Extends net.minecraft.world.damagesource.DamageEffects
        return type.cast(switch (idx) {
            case 0 -> ncLoc("fatal_rads").toString();
            case 1 -> rad_poisoning;
            default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
        });
    }
}
