package com.nred.nuclearcraft.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.config.Config2.ctrl_info;

public class InfoHelper {

    public static final int MAXIMUM_TEXT_WIDTH = 225;

    public static final Component SHIFT_STRING = Component.translatable("gui.nc.inventory.shift_for_info");
    public static final Component CTRL_STRING = Component.translatable("gui.nc.inventory.ctrl_for_info");

    public static final String[] EMPTY_ARRAY = {}, NULL_ARRAY = {null};
    public static final String[][] EMPTY_ARRAYS = {}, NULL_ARRAYS = {null};

    public static void infoLine(List<Component> list, ChatFormatting fixedColor, String line) {
        list.add(Component.literal(line).withStyle(fixedColor));
    }

    public static void shiftInfo(List<Component> list) {
        list.add((ctrl_info ? CTRL_STRING : SHIFT_STRING).copy().withStyle(ChatFormatting.ITALIC));
    }

    public static void fixedInfoList(List<Component> list, boolean infoBelow, ChatFormatting fixedColor, String... fixedLines) {
        for (String fixedLine : fixedLines) {
            infoLine(list, fixedColor, fixedLine);
        }
        if (infoBelow) {
            shiftInfo(list);
        }
    }

    public static void fixedInfoList(List<Component> list, boolean infoBelow, ChatFormatting[] fixedColors, String... fixedLines) {
        for (int i = 0; i < fixedLines.length; ++i) {
            infoLine(list, fixedColors[i], fixedLines[i]);
        }
        if (infoBelow) {
            shiftInfo(list);
        }
    }

    public static void infoList(List<Component> list, ChatFormatting infoColor, String... lines) {
        for (String line : lines) {
            infoLine(list, infoColor, line);
        }
    }

    public static void infoList(List<Component> list, ChatFormatting[] infoColors, String... lines) {
        for (int i = 0; i < lines.length; ++i) {
            infoLine(list, infoColors[i], lines[i]);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting fixedColor, String[] fixedLines, ChatFormatting infoColor, String... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColor, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColor, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting fixedColor, String[] fixedLines, ChatFormatting[] infoColors, String... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColor, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColors, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting[] fixedColors, String[] fixedLines, ChatFormatting infoColor, String... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColors, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColor, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting[] fixedColors, String[] fixedLines, ChatFormatting[] infoColors, String... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColors, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColors, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static boolean showFixedInfo(String[] fixedLines, String... lines) {
        return !Arrays.equals(fixedLines, EMPTY_ARRAY) && !Arrays.equals(fixedLines, NULL_ARRAY) && (!NCUtil.isInfoKeyDown() || Arrays.equals(lines, EMPTY_ARRAY));
    }

    public static boolean showInfo(String[] fixedLines, String... lines) {
        return (NCUtil.isInfoKeyDown() || lines.length == 1) && !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY);
    }

    public static boolean showShiftInfo(String[] fixedLines, String... lines) {
        return !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY);
    }

    public static void infoFull(List<Component> list, ChatFormatting infoColor, String... lines) {
        infoFull(list, ChatFormatting.AQUA, EMPTY_ARRAY, infoColor, lines);
    }

    public static void infoFull(List<Component> list, ChatFormatting[] infoColors, String... lines) {
        infoFull(list, new ChatFormatting[]{}, EMPTY_ARRAY, infoColors, lines);
    }

    public static String[] formattedInfo(String tooltip, Object... args) {
        return FontRenderHelper.wrapString(Lang.localize(tooltip, args), MAXIMUM_TEXT_WIDTH);
    }

    public static String[] buildFixedInfo(String unlocName, String... tooltip) {
        if (tooltip.length == 0) {
            return standardFixedInfo(unlocName, unlocName);
        } else {
            return tooltip;
        }
    }

    public static String[] buildInfo(String unlocName, String... tooltip) {
        if (tooltip.length == 0) {
            return standardInfo(unlocName, unlocName);
        } else {
            return tooltip;
        }
    }

    public static <T extends Enum<T> & StringRepresentable> String[] getEnumNames(Class<T> enumm) {
        T[] values = enumm.getEnumConstants();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; ++i) {
            names[i] = values[i].getSerializedName();
        }
        return names;
    }

    public static <T extends Enum<T> & StringRepresentable> String[][] buildFixedInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, getEnumNames(enumm), ".fixd", ".fix", tooltips);
    }

    public static String[][] buildFixedInfo(String unlocNameBase, String[] types, String[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, types, ".fixd", ".fix", tooltips);
    }

    public static <T extends Enum<T> & StringRepresentable> String[][] buildInfo(String unlocNameBase, Class<T> enumm, String[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, getEnumNames(enumm), ".desc", ".des", tooltips);
    }

    public static String[][] buildInfo(String unlocNameBase, String[] names, String[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, names, ".desc", ".des", tooltips);
    }

    public static String[][] buildGeneralInfo(String unlocNameBase, String[] types, String desc, String des, String[]... tooltips) {
        String[][] strings = new String[types.length][];

        if (Arrays.deepEquals(tooltips, NULL_ARRAYS)) {
            for (int i = 0; i < types.length; ++i) {
                strings[i] = NULL_ARRAY;
            }
            return strings;
        }

        for (int i = 0; i < types.length; ++i) {
            if (CollectionHelper.isNull(tooltips, i)) {
                strings[i] = EMPTY_ARRAY;
            } else if (CollectionHelper.isEmpty(tooltips, i)) {
                strings[i] = standardGeneralInfo(unlocNameBase + "." + types[i], unlocNameBase, desc, des);
            } else {
                strings[i] = tooltips[i];
            }
        }
        return strings;
    }

    public static String[] standardFixedInfo(String unlocName, String generalName) {
        return standardGeneralInfo(unlocName, generalName, ".fixd", ".fix");
    }

    public static String[] standardInfo(String unlocName, String generalName) {
        return standardGeneralInfo(unlocName, generalName, ".desc", ".des");
    }

    public static String[] standardGeneralInfo(String unlocName, String generalName, String desc, String des) {
        for (String name : new String[]{unlocName, generalName}) {
            if (Lang.canLocalize(name + desc)) {
                return formattedInfo(name + desc);
            }
        }
        return getNumberedInfo(unlocName + des);
    }

    public static String[] getNumberedInfo(String base) {
        String firstLine = base + 0;
        if (!Lang.canLocalize(firstLine)) {
            return EMPTY_ARRAY;
        }
        String[] info = new String[]{Lang.localize(firstLine)};
        int line = 1;
        while (Lang.canLocalize(base + line)) {
            info = CollectionHelper.concatenate(info, Lang.localize(base + line));
            ++line;
        }
        return info;
    }
}
