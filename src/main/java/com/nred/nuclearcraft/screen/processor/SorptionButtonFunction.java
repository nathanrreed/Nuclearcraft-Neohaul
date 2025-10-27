package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.gui.SorptionConfig;

@FunctionalInterface
public interface SorptionButtonFunction {
    SorptionConfig apply(int id, int x, int y, int width, int height, NCButton.OnPressInfo onPress);
}
