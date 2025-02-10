package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.IngotFormerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class IngotFormerScreen extends ProcessorScreen<IngotFormerMenu> {
    public IngotFormerScreen(IngotFormerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "ingot_former", PROCESSOR_CONFIG_MAP.get("ingot_former").processing_power(), 74, 35);
    }
}