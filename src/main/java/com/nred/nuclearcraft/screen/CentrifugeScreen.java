package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.CentrifugeMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class CentrifugeScreen extends ProcessorScreen<CentrifugeMenu> {
    public CentrifugeScreen(CentrifugeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "centrifuge", 58, 30, 12);
    }
}