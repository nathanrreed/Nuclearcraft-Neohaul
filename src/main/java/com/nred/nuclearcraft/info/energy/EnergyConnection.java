package com.nred.nuclearcraft.info.energy;

import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

public enum EnergyConnection implements StringRepresentable {

    IN,
    OUT,
    BOTH,
    NON;

    public boolean canReceive() {
        return this == IN || this == BOTH;
    }

    public boolean canExtract() {
        return this == OUT || this == BOTH;
    }

    public boolean canConnect() {
        return this != NON;
    }

    public EnergyConnection next(Type type) {
        return switch (type) {
            case DEFAULT -> switch (this) {
                case IN -> OUT;
                case NON -> IN;
                default -> NON;
            };
        };
    }

    public ChatFormatting getTextColor() {
        return switch (this) {
            case IN -> ChatFormatting.BLUE;
            case OUT -> ChatFormatting.RED;
            case BOTH -> ChatFormatting.BOLD;
            case NON -> ChatFormatting.GRAY;
        };
    }

    @Override
    public String getSerializedName() {
        return switch (this) {
            case IN -> "in";
            case OUT -> "out";
            case BOTH -> "both";
            case NON -> "non";
        };
    }

    public enum Type {
        DEFAULT
    }
}