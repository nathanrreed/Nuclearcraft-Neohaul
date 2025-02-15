package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.DecayHastenerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DecayHastenerScreen extends ProcessorScreen<DecayHastenerMenu> {
    public DecayHastenerScreen(DecayHastenerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "decay_hastener", 74, 35);
    }
}