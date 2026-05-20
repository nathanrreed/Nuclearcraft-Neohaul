package com.nred.nuclearcraft.util;

import net.minecraft.util.FastColor;

import java.awt.*;

public class ColorHelper {
    public static int saturate(int color, float saturation) {
        float[] hsb = Color.RGBtoHSB(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), null);

        int rgb = Color.HSBtoRGB(hsb[0], (float) NCMath.sigmoid(hsb[1] * saturation), hsb[2]);
        return FastColor.ARGB32.color(FastColor.ARGB32.alpha(color), rgb);
    }
}