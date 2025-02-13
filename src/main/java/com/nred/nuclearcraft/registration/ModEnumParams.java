package com.nred.nuclearcraft.registration;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.SoundRegistration.RAD_POISONING;

public class ModEnumParams {
    public static Object getDamageEffectsParameter(int idx, Class<?> type) {
        return type.cast(switch (idx) {
            case 0 -> ncLoc("radiation").toString();
            case 1 -> RAD_POISONING;
            default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
        });
    }
}
