package com.nred.nuclearcraft.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

public record Element(String postfix, ResourceLocation outerTexture, ChatFormatting color) {
}