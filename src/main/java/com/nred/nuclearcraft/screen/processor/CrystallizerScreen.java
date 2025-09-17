package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.CrystallizerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CrystallizerScreen extends ProcessorScreen<CrystallizerMenu> {
    public CrystallizerScreen(CrystallizerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "crystallizer", 74, 35);
    }
}