package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.SupercoolerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class SupercoolerScreen extends ProcessorScreen<SupercoolerMenu> {
    public SupercoolerScreen(SupercoolerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "supercooler", PROCESSOR_CONFIG_MAP.get("supercooler").processing_power(), 74, 35);
    }
}