package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.AssemblerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class AssemblerScreen extends ProcessorScreen<AssemblerMenu> {
    public AssemblerScreen(AssemblerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "assembler", PROCESSOR_CONFIG_MAP.get("assembler").processing_power(), 84, 31, 12);
    }
}