package com.nred.nuclearcraft.compat.common;

import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record RecipeViewerInfo(String name, ScreenRectangle rect, ScreenPosition progress, List<ScreenPosition> inputs, List<ScreenPosition> outputs) {
    public ResourceLocation background() {
        return ncLoc("textures/gui/sprites/screen/" + name+".png");
    }
}
