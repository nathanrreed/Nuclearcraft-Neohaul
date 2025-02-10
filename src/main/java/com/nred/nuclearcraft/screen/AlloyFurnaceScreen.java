package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.AlloyFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class AlloyFurnaceScreen extends ProcessorScreen<AlloyFurnaceMenu> {
    public AlloyFurnaceScreen(AlloyFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "alloy_furnace", PROCESSOR_CONFIG_MAP.get("alloy_furnace").processing_power(), 84, 34);
    }
}