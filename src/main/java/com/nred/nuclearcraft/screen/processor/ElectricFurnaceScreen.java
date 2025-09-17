package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.ElectricFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ElectricFurnaceScreen extends ProcessorScreen<ElectricFurnaceMenu> {
    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "electric_furnace", 74, 35);
    }
}