package com.nred.nuclearcraft.block.internal.energy;

import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull String getSerializedName() {
        return switch (this) {
            case IN -> "input";
            case OUT -> "output";
            case BOTH -> "both";
            case NON -> "disabled";
        };
    }

    public enum Type {
        DEFAULT
    }
}