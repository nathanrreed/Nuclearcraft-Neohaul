package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.AlloyFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AlloyFurnaceScreen extends ProcessorScreen<AlloyFurnaceMenu> {
    public AlloyFurnaceScreen(AlloyFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "alloy_furnace", 84, 35);
    }
}