package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.FuelReprocessorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FuelReprocessorScreen extends ProcessorScreen<FuelReprocessorMenu> {
    public FuelReprocessorScreen(FuelReprocessorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fuel_reprocessor", 48, 30, 12);
    }
}