package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.SeparatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SeparatorScreen extends ProcessorScreen<SeparatorMenu> {
    public SeparatorScreen(SeparatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "separator", 60, 34);
    }
}