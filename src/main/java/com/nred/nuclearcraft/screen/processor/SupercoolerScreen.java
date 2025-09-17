package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.SupercoolerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SupercoolerScreen extends ProcessorScreen<SupercoolerMenu> {
    public SupercoolerScreen(SupercoolerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "supercooler", 74, 35);
    }
}