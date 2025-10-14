package com.nred.nuclearcraft.gui;

import net.minecraft.client.gui.components.Button;

@FunctionalInterface
public interface SorptionButtonFunction {
    SorptionConfig apply(int id, int x, int y, int width, int height, Button.OnPress onPress);
}