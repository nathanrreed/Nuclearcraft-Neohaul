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
}