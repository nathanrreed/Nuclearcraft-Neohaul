package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.ElectrolyzerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ElectrolyzerScreen extends ProcessorScreen<ElectrolyzerMenu> {
    public ElectrolyzerScreen(ElectrolyzerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "electrolyzer", 68, 30, 12);
    }
}