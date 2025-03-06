package com.nred.nuclearcraft.helpers;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import java.util.*;

public class SideConfigEnums {
    public enum OutputSetting {
        DEFAULT, VOID_EXCESS, VOID;

        public OutputSetting next(boolean reverse) {
            if (reverse) {
                return switch (this) {
                    case DEFAULT -> VOID;
                    case VOID_EXCESS -> DEFAULT;
                    case VOID -> VOID_EXCESS;
                };
            } else {
                return switch (this) {
                    case DEFAULT -> VOID_EXCESS;
                    case VOID_EXCESS -> VOID;
                    case VOID -> DEFAULT;
                };
            }
        }

        public static CompoundTag serializeNBT(HolderLookup.Provider provider, CompoundTag tag, List<OutputSetting> outputSettings) {
            if (outputSettings == null) return tag;
            tag.putIntArray("output_settings", outputSettings.stream().map(Enum::ordinal).toList());
            return tag;
        }

        public static ArrayList<OutputSetting> deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            if (!nbt.contains("output_settings")) return null;
            return new ArrayList<>(Arrays.stream(nbt.getIntArray("output_settings")).mapToObj(i -> OutputSetting.values()[i]).toList());
        }

    }

    public enum SideConfigSetting {
        OUTPUT, AUTO_OUTPUT, DISABLED, INPUT;

        public SideConfigSetting nextInput(boolean reverse) {
            if (reverse) {
                return switch (this) {
                    case INPUT -> OUTPUT;
                    case OUTPUT -> AUTO_OUTPUT;
                    case AUTO_OUTPUT -> DISABLED;
                    case DISABLED -> INPUT;
                };
            } else {
                return switch (this) {
                    case INPUT -> DISABLED;
                    case DISABLED -> AUTO_OUTPUT;
                    case AUTO_OUTPUT -> OUTPUT;
                    case OUTPUT -> INPUT;
                };
            }
        }

        public SideConfigSetting nextOutput(boolean reverse) {
            if (reverse) {
                return switch (this) {
                    case OUTPUT -> AUTO_OUTPUT;
                    case AUTO_OUTPUT -> DISABLED;
                    case DISABLED -> OUTPUT;
                    default -> throw new IllegalStateException("Unexpected value: " + this);
                };
            } else {
                return switch (this) {
                    case DISABLED -> AUTO_OUTPUT;
                    case AUTO_OUTPUT -> OUTPUT;
                    case OUTPUT -> DISABLED;
                    default -> throw new IllegalStateException("Unexpected value: " + this);
                };
            }
        }

        public static CompoundTag serializeNBT(HolderLookup.Provider provider, CompoundTag tag, Map<Direction, List<SideConfigSetting>> sideConfigs) {
            if (sideConfigs == null) return tag;

            for (Direction dir : Direction.values()) {
                tag.putIntArray("side_config_" + dir.getName(), sideConfigs.get(dir).stream().map(Enum::ordinal).toList());
            }

            return tag;
        }

        public static Map<Direction, List<SideConfigSetting>> deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            if (!nbt.contains("side_config_" + Direction.NORTH.getName())) return null;

            HashMap<Direction, List<SideConfigSetting>> map = new HashMap<>(Direction.values().length);
            for (Direction dir : Direction.values()) {
                map.put(dir, new ArrayList<>(Arrays.stream(nbt.getIntArray("side_config_" + dir.getName())).mapToObj(i -> SideConfigSetting.values()[i]).toList()));
            }

            return map;
        }
    }
}
