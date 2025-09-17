package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.CentrifugeMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CentrifugeScreen extends ProcessorScreen<CentrifugeMenu> {
    public CentrifugeScreen(CentrifugeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "centrifuge", 58, 30, 12);
    }
}