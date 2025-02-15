package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.EnricherMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class EnricherScreen extends ProcessorScreen<EnricherMenu> {
    public EnricherScreen(EnricherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_enricher", 84, 35);
    }
}