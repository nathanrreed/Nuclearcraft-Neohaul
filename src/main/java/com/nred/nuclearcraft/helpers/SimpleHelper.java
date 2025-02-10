package com.nred.nuclearcraft.helpers;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleHelper {
    public static List<Direction> shuffledDirections() {
        ArrayList<Direction> rtn = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(rtn);
        return rtn;
    }

    public static String getFEString(double power) {
        if ((power / 1000000000.0) >= 0.999999) {
            return String.format("%.1f GFE", power / 1000000000.0);
        } else if ((power / 1000000.0) >= 0.999999) {
            return String.format("%.1f MFE", power / 1000000.0);
        } else if ((power / 1000.0) >= 0.999999) {
            return String.format("%.1f kFE", power / 1000.0);
        } else {
            return String.format("%.1f FE", power);
        }
    }

    //new DecimalFormat("#.##").format
}