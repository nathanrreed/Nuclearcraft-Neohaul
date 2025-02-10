package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.PressurizerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class PressurizerScreen extends ProcessorScreen<PressurizerMenu> {
    public PressurizerScreen(PressurizerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "pressurizer", PROCESSOR_CONFIG_MAP.get("pressurizer").processing_power(), 74, 35);
    }
}