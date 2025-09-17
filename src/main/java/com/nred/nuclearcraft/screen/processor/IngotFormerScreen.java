package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.IngotFormerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class IngotFormerScreen extends ProcessorScreen<IngotFormerMenu> {
    public IngotFormerScreen(IngotFormerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "ingot_former", 74, 35);
    }
}