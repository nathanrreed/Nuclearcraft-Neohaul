package com.nred.nuclearcraft.helpers;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.max;

public class SimpleHelper {
    public static List<Direction> shuffledDirections() {
        ArrayList<Direction> rtn = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(rtn);
        return rtn;
    }

    public static String getFEString(double power) {
        return getFEString(power, false);
    }

    public static String getFEString(double power, boolean toInt) {
        String base = toInt ? "%.0f" : "%.1f";
        if ((power / 1000000000.0) >= 0.999999) {
            return String.format(base + " GFE", power / 1000000000.0);
        } else if ((power / 1000000.0) >= 0.999999) {
            return String.format(base + " MFE", power / 1000000.0);
        } else if ((power / 1000.0) >= 0.999999) {
            return String.format(base + " kFE", power / 1000.0);
        } else {
            return String.format(base + " FE", power);
        }
    }

    public static String getTimeString(Double ticks) {
        if (ticks <= 0) {
            return "0s";
        } else if (ticks < 20) {
            return ticks + "t";
        }

        double time = max(ticks / 20.0, 0.0);
        double hours = time / 3600.0;
        time %= 3600.0;
        double mins = time / 60.0;
        time %= 60.0;

        String str = "";
        if (hours >= 1) {
            str += String.format("%dh ", (int) Math.floor(hours));
        }
        if (mins >= 1) {
            str += String.format("%dm ", (int) Math.floor(mins));
        }
        if (time >= 0) {
            str += String.format("%ds", (int) Math.floor(time));
        }

        return str.trim();
    }

    public static MobEffectInstance newEffect(Holder<MobEffect> effect, int strength, int ticks) {
        return new MobEffectInstance(effect, ticks, max(0, strength - 1));
    }
}