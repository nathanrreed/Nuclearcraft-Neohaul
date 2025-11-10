package com.nred.nuclearcraft.multiblock.quantum;

public class QuantumHelpers {
    public static boolean[] demux(int[] targets, int size) {
        boolean[] demux = new boolean[size];
        int index = 0;

        for (int i = 0, count = targets.length; i < size; ++i) {
            if (index < count && targets[index] == i) {
                demux[i] = true;
                ++index;
            }
        }

        return demux;
    }

    public static int[] window(int[] targets, int size) {
        int count = targets.length, span = 1 << count;
        int[] window = new int[span];

        boolean[] demux = QuantumHelpers.demux(targets, size);
        outer:
        for (int i = 0, index = 0; index < span; ++i) {
            for (int j = 0; j < size; ++j) {
                if (!demux[j] && ((i >> j) & 1) == 1) {
                    continue outer;
                }
            }
            window[index++] = i << 1;
        }

        return window;
    }

    public static int[] steps(int[] targets, int size) {
        int count = targets.length, len = size - count;
        int[] steps = new int[len];

        for (int i = 0, j = 0, k = 0; k < len; ++i) {
            if (j >= count || targets[j] != i) {
                steps[k++] = 1 << i;
            } else {
                ++j;
            }
        }

        return steps;
    }

    public static int at(int index, int[] steps) {
        int x = 0;
        for (int i = 0, len = steps.length; i < len; ++i) {
            if (((index >> i) & 1) == 1) {
                x |= steps[i];
            }
        }
        return x << 1;
    }

    public static int offset(int[] targets, boolean[] results) {
        int offset = 0;

        if (results == null) {
            for (int target : targets) {
                offset |= 1 << target;
            }
        } else {
            for (int i = 0, len = targets.length; i < len; ++i) {
                if (results[i]) {
                    offset |= 1 << targets[i];
                }
            }
        }

        return offset;
    }
}