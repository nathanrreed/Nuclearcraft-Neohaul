package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.InfuserMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class InfuserScreen extends ProcessorScreen<InfuserMenu> {
    public InfuserScreen(InfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_infuser", 84, 35);
    }
}