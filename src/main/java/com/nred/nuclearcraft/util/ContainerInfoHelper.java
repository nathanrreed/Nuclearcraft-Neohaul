package com.nred.nuclearcraft.util;

import java.util.List;

public class ContainerInfoHelper {

    public static int[] standardSlot(int x, int y) {
        return new int[]{x, y, 16, 16};
    }

    public static int[] bigSlot(int x, int y) {
        return new int[]{x, y, 24, 24};
    }

    public static List<int[]> stackXYList(List<int[]> slotXYWHList) {
        return StreamHelper.map(slotXYWHList, ContainerInfoHelper::stackXY);
    }

    public static int[] stackXY(int[] slotXYWH) {
        return new int[]{slotXYWH[0] + (slotXYWH[2] - 16) / 2, slotXYWH[1] + (slotXYWH[3] - 16) / 2};
    }
}