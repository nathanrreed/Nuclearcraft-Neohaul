package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ElectricFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class ElectricFurnaceScreen extends ProcessorScreen<ElectricFurnaceMenu> {
    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "electric_furnace", PROCESSOR_CONFIG_MAP.get("electric_furnace").processing_power(), 74, 36);
    }
}