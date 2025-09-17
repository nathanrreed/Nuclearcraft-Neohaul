package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.MelterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MelterScreen extends ProcessorScreen<MelterMenu> {
    public MelterScreen(MelterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "melter", 74, 35);
    }
}