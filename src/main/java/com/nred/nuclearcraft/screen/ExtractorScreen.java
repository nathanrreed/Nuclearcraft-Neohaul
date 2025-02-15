package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ExtractorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ExtractorScreen extends ProcessorScreen<ExtractorMenu> {
    public ExtractorScreen(ExtractorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_extractor", 60, 35);
    }
}