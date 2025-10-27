package com.nred.nuclearcraft.gui;

import net.minecraft.resources.ResourceLocation;

public interface IGuiButton {
    int getTextureWidth();

    int getTextureHeight();

    ResourceLocation getTexture(boolean hovered);
}