package com.nred.nuclearcraft.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config2.ctrl_info;

public class InfoHelper {
    public static final int MAXIMUM_TEXT_WIDTH = 225;

    public static final Component SHIFT_STRING = Component.translatable(MODID + ".tooltip.shift_for_info").withStyle(ChatFormatting.GRAY);
    public static final Component CTRL_STRING = Component.translatable(MODID + ".tooltip.ctrl_for_info").withStyle(ChatFormatting.GRAY);

    public static final Component[] EMPTY_ARRAY = {}, NULL_ARRAY = {null};
    public static final Component[][] EMPTY_ARRAYS = {}, NULL_ARRAYS = {null};

    public static void infoLine(List<Component> list, ChatFormatting fixedColor, Component line) {
        if (fixedColor.equals(ChatFormatting.UNDERLINE)) {
            list.add(line.copy().withStyle(fixedColor, ChatFormatting.GRAY));
        } else {
            list.add(line.copy().withStyle(fixedColor));
        }
    }

    public static void shiftInfo(List<Component> list) {
        list.add((ctrl_info ? CTRL_STRING : SHIFT_STRING).copy().withStyle(ChatFormatting.ITALIC));
    }

    public static void fixedInfoList(List<Component> list, boolean infoBelow, ChatFormatting fixedColor, Component... fixedLines) {
        for (Component fixedLine : fixedLines) {
            infoLine(list, fixedColor, fixedLine);
        }
        if (infoBelow) {
            shiftInfo(list);
        }
    }

    public static void fixedInfoList(List<Component> list, boolean infoBelow, ChatFormatting[] fixedColors, Component... fixedLines) {
        for (int i = 0; i < fixedLines.length; ++i) {
            infoLine(list, fixedColors[i], fixedLines[i]);
        }
        if (infoBelow) {
            shiftInfo(list);
        }
    }

    public static void infoList(List<Component> list, ChatFormatting infoColor, Component... lines) {
        for (Component line : lines) {
            infoLine(list, infoColor, line);
        }
    }

    public static void infoList(List<Component> list, ChatFormatting[] infoColors, Component... lines) {
        for (int i = 0; i < lines.length; ++i) {
            infoLine(list, infoColors[i], lines[i]);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting fixedColor, Component[] fixedLines, ChatFormatting infoColor, Component... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColor, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColor, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting fixedColor, Component[] fixedLines, ChatFormatting[] infoColors, Component... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColor, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColors, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting[] fixedColors, Component[] fixedLines, ChatFormatting infoColor, Component... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColors, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColor, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static void infoFull(List<Component> list, ChatFormatting[] fixedColors, Component[] fixedLines, ChatFormatting[] infoColors, Component... lines) {
        if (showFixedInfo(fixedLines, lines)) {
            fixedInfoList(list, !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY), fixedColors, fixedLines);
        } else if (showInfo(fixedLines, lines)) {
            infoList(list, infoColors, lines);
        } else if (showShiftInfo(fixedLines, lines)) {
            shiftInfo(list);
        }
    }

    public static boolean showFixedInfo(Component[] fixedLines, Component... lines) {
        return !Arrays.equals(fixedLines, EMPTY_ARRAY) && !Arrays.equals(fixedLines, NULL_ARRAY) && (!NCUtil.isInfoKeyDown() || Arrays.equals(lines, EMPTY_ARRAY));
    }

    public static boolean showInfo(Component[] fixedLines, Component... lines) {
        return (NCUtil.isInfoKeyDown() || lines.length == 1) && !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY);
    }

    public static boolean showShiftInfo(Component[] fixedLines, Component... lines) {
        return !Arrays.equals(lines, EMPTY_ARRAY) && !Arrays.equals(lines, NULL_ARRAY);
    }

    public static void infoFull(List<Component> list, ChatFormatting infoColor, Component... lines) {
        infoFull(list, ChatFormatting.AQUA, EMPTY_ARRAY, infoColor, lines);
    }

    public static void infoFull(List<Component> list, ChatFormatting[] infoColors, Component... lines) {
        infoFull(list, new ChatFormatting[]{}, EMPTY_ARRAY, infoColors, lines);
    }

    public static Component[] buildFixedInfo(String unlocName, Component... tooltip) {
        if (tooltip.length == 0) {
            return standardFixedInfo(unlocName);
        } else {
            return tooltip;
        }
    }

    public static Component[] buildInfo(String unlocName, Component... tooltip) {
        if (tooltip.length == 0) {
            return standardInfo(unlocName);
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

    public static <T extends Enum<T> & StringRepresentable> Component[][] buildFixedInfo(String unlocNameBase, Class<T> enumm, Component[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, getEnumNames(enumm), ".fixd", tooltips);
    }

    public static Component[][] buildFixedInfo(String unlocNameBase, String[] types, Component[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, types, ".fixd", tooltips);
    }

    public static <T extends Enum<T> & StringRepresentable> Component[][] buildInfo(String unlocNameBase, Class<T> enumm, Component[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, getEnumNames(enumm), ".desc", tooltips);
    }

    public static Component[][] buildInfo(String unlocNameBase, String[] names, Component[]... tooltips) {
        return buildGeneralInfo(unlocNameBase, names, ".desc", tooltips);
    }

    public static Component[][] buildGeneralInfo(String unlocNameBase, String[] types, String desc, Component[]... tooltips) {
        Component[][] strings = new Component[types.length][];

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
                strings[i] = standardGeneralInfo(unlocNameBase + "." + types[i], desc);
            } else {
                strings[i] = tooltips[i];
            }
        }
        return strings;
    }

    public static Component[] standardFixedInfo(String unlocName) {
        return standardGeneralInfo(unlocName, ".fixd");
    }

    public static Component[] standardInfo(String unlocName) {
        return standardGeneralInfo(unlocName, ".desc");
    }

    public static Component[] standardGeneralInfo(String unlocNameString, String desc) {
        return new Component[]{Component.translatable(unlocNameString + desc)};
    }
}
