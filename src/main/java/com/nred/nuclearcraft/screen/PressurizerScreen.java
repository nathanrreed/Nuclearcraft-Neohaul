package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.PressurizerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PressurizerScreen extends ProcessorScreen<PressurizerMenu> {
    public PressurizerScreen(PressurizerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "pressurizer", 74, 35);
    }
}