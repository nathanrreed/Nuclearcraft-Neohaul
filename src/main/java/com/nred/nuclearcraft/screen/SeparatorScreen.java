package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.SeparatorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class SeparatorScreen extends ProcessorScreen<SeparatorMenu> {
    public SeparatorScreen(SeparatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "separator", PROCESSOR_CONFIG_MAP.get("separator").processing_power(), 60, 35);
    }
}