package com.nred.nuclearcraft.multiblock.hx;

import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

public enum HeatExchangerTubeSetting implements StringRepresentable {
    CLOSED("closed"),
    OPEN("open"),
    CLOSED_BAFFLE("closed_baffle"),
    OPEN_BAFFLE("open_baffle");

    private final String name;

    HeatExchangerTubeSetting(String name) {
        this.name = name;
    }

    public static HeatExchangerTubeSetting of(boolean open, boolean baffle) {
        return open ? (baffle ? OPEN_BAFFLE : OPEN) : (baffle ? CLOSED_BAFFLE : CLOSED);
    }

    public boolean isOpen() {
        return equals(OPEN) || equals(OPEN_BAFFLE);
    }

    public boolean isBaffle() {
        return equals(CLOSED_BAFFLE) || equals(OPEN_BAFFLE);
    }

    public HeatExchangerTubeSetting next() {
        return switch (this) {
            case CLOSED -> OPEN;
            case OPEN -> CLOSED_BAFFLE;
            case CLOSED_BAFFLE -> OPEN_BAFFLE;
            default -> CLOSED;
        };
    }

    public ChatFormatting getTextColor() {
        return isOpen() ? ChatFormatting.BOLD : ChatFormatting.GRAY;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return getSerializedName();
    }
}