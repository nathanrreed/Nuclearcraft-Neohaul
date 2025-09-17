package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.AssemblerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AssemblerScreen extends ProcessorScreen<AssemblerMenu> {
    public AssemblerScreen(AssemblerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "assembler", 84, 31, 12);
    }
}