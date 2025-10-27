package com.nred.nuclearcraft.block_entity.internal.inventory;

import com.nred.nuclearcraft.gui.IGuiButton;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public enum ItemSorption implements StringRepresentable, IGuiButton {
    IN,
    OUT,
    BOTH,
    NON,
    AUTO_IN,
    AUTO_OUT;

    public boolean canReceive() {
        return this == IN || this == BOTH || this == AUTO_IN;
    }

    public boolean canExtract() {
        return this == OUT || this == BOTH || this == AUTO_OUT;
    }

    public boolean canConnect() {
        return this != NON;
    }

    public ItemSorption next(Type type, boolean reverse) {
        if (reverse) {
            return switch (type) {
                case INPUT -> switch (this) {
                    case IN -> NON;
                    case NON -> AUTO_OUT;
                    case AUTO_OUT -> OUT;
                    case OUT -> IN;
                    default -> IN;
                };
                case OUTPUT -> switch (this) {
                    case OUT -> NON;
                    case NON -> AUTO_OUT;
                    case AUTO_OUT -> OUT;
                    default -> OUT;
                };
                default -> switch (this) {
                    case IN, AUTO_IN -> NON;
                    case NON -> BOTH;
                    case BOTH -> OUT;
                    case OUT, AUTO_OUT -> IN;
                };
            };
        } else {
            return switch (type) {
                case INPUT -> switch (this) {
                    case IN -> OUT;
                    case OUT -> AUTO_OUT;
                    case AUTO_OUT -> NON;
                    case NON -> IN;
                    default -> IN;
                };
                case OUTPUT -> switch (this) {
                    case OUT -> AUTO_OUT;
                    case AUTO_OUT -> NON;
                    case NON -> OUT;
                    default -> OUT;
                };
                default -> switch (this) {
                    case IN, AUTO_IN -> OUT;
                    case OUT, AUTO_OUT -> BOTH;
                    case BOTH -> NON;
                    case NON -> IN;
                };
            };
        }
    }

    private static final ItemSorption[][] FROM_INT_ARRAYS = {
            {IN, OUT, BOTH, NON},
            {IN, OUT, AUTO_OUT, NON},
            {OUT, AUTO_OUT, NON}
    };

    public static ItemSorption fromInt(Type type, int index) {
        return FROM_INT_ARRAYS[type.ordinal()][index];
    }

    public ChatFormatting getTextColor() {
        return switch (this) {
            case IN -> ChatFormatting.BLUE;
            case OUT -> ChatFormatting.GOLD;
            case BOTH -> ChatFormatting.BOLD;
            case NON -> ChatFormatting.GRAY;
            case AUTO_IN -> ChatFormatting.DARK_BLUE;
            case AUTO_OUT -> ChatFormatting.YELLOW;
        };
    }

    @Override
    public ResourceLocation getTexture(boolean hovered) {
        return ncLoc("button/item_" + this.getSerializedName());
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
            case IN -> "in";
            case OUT -> "out";
            case BOTH -> "both";
            case NON -> "non";
            case AUTO_IN -> "auto_in";
            case AUTO_OUT -> "auto_out";
        };
    }

    public enum Type {
        DEFAULT,
        INPUT,
        OUTPUT
    }
}