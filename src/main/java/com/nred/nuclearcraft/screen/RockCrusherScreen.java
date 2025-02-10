package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.RockCrusherMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class RockCrusherScreen extends ProcessorScreen<RockCrusherMenu> {
    public RockCrusherScreen(RockCrusherMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "rock_crusher", PROCESSOR_CONFIG_MAP.get("rock_crusher").processing_power(), 56, 36);
    }
}