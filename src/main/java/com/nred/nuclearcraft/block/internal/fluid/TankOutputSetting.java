package com.nred.nuclearcraft.block.internal.fluid;


import com.nred.nuclearcraft.gui.IGuiButton;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

public enum TankOutputSetting implements StringRepresentable, IGuiButton {
    DEFAULT,
    VOID_EXCESS,
    VOID;

    public TankOutputSetting next(boolean reverse) {
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

    public ChatFormatting getTextColor() {
        return switch (this) {
            case DEFAULT -> ChatFormatting.WHITE;
            case VOID_EXCESS -> ChatFormatting.LIGHT_PURPLE;
            case VOID -> ChatFormatting.DARK_PURPLE;
        };
    }

    @Override
    public int getTextureX() {
        return switch (this) {
            case DEFAULT -> 54;
            case VOID_EXCESS -> 72;
            case VOID -> 90;
        };
    }

    @Override
    public int getTextureY() {
        return 18;
    }

    @Override
    public int getTextureWidth() {
        return 18;
    }

    @Override
    public int getTextureHeight() {
        return 18;
    }

    @Override
    public String getSerializedName() {
        return switch (this) {
            case DEFAULT -> "default";
            case VOID_EXCESS -> "void_excess";
            case VOID -> "void";
        };
    }
}