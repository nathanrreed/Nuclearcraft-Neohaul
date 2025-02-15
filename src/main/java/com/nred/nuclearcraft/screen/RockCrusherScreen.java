package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.RockCrusherMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class RockCrusherScreen extends ProcessorScreen<RockCrusherMenu> {
    public RockCrusherScreen(RockCrusherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "rock_crusher", 56, 35);
    }
}