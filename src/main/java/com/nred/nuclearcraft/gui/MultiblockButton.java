package com.nred.nuclearcraft.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class MultiblockButton {
    @OnlyIn(Dist.CLIENT)
    public static class ClearAllMaterial extends ImageButton {
        private static final Component CLEAR_ALL = Component.translatable(MODID + ".tooltip.shift_clear_multiblock").withStyle(ChatFormatting.ITALIC);

        public ClearAllMaterial(int x, int y, OnPress onPress) {
            super(x, y, 18, 18, new WidgetSprites(ncLoc("button/void_excess_off"), ncLoc("button/void_excess_on")), onPress);
            setTooltip(Tooltip.create(CLEAR_ALL));
        }
    }
}