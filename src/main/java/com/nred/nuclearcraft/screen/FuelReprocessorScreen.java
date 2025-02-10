package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.FuelReprocessorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class FuelReprocessorScreen extends ProcessorScreen<FuelReprocessorMenu> {
    public FuelReprocessorScreen(FuelReprocessorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fuel_reprocessor", PROCESSOR_CONFIG_MAP.get("fuel_reprocessor").processing_power(), 48, 31, 12);
    }
}