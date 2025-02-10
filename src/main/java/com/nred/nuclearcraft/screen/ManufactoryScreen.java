package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ManufactoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class ManufactoryScreen extends ProcessorScreen<ManufactoryMenu> {
    public ManufactoryScreen(ManufactoryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "manufactory", PROCESSOR_CONFIG_MAP.get("manufactory").processing_power(), 74, 36);
    }
}