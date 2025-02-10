package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ExtractorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class ExtractorScreen extends ProcessorScreen<ExtractorMenu> {
    public ExtractorScreen(ExtractorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_extractor", PROCESSOR_CONFIG_MAP.get("fluid_extractor").processing_power(), 60, 35);
    }
}