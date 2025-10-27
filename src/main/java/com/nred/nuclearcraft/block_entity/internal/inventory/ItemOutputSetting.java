package com.nred.nuclearcraft.block_entity.internal.inventory;

import com.nred.nuclearcraft.gui.IGuiButton;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public enum ItemOutputSetting implements StringRepresentable, IGuiButton {
    DEFAULT,
    VOID_EXCESS,
    VOID;

    public ItemOutputSetting next(boolean reverse) {
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
    public ResourceLocation getTexture(boolean hovered) {
        return ncLoc("button/" + this.getSerializedName() + (hovered ? "_on" : "_off"));
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