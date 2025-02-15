package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ManufactoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ManufactoryScreen extends ProcessorScreen<ManufactoryMenu> {
    public ManufactoryScreen(ManufactoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "manufactory", 74, 35);
    }
}