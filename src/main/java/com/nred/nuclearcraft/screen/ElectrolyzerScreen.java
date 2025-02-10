package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ElectrolyzerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class ElectrolyzerScreen extends ProcessorScreen<ElectrolyzerMenu> {
    public ElectrolyzerScreen(ElectrolyzerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "electrolyzer", PROCESSOR_CONFIG_MAP.get("electrolyzer").processing_power(), 68, 31, 12);
    }
}