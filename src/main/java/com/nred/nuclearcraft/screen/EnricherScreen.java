package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.EnricherMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class EnricherScreen extends ProcessorScreen<EnricherMenu> {
    public EnricherScreen(EnricherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_enricher", PROCESSOR_CONFIG_MAP.get("fluid_enricher").processing_power(), 84, 35);
    }
}