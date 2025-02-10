package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.DecayHastenerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class DecayHastenerScreen extends ProcessorScreen<DecayHastenerMenu> {
    public DecayHastenerScreen(DecayHastenerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "decay_hastener", PROCESSOR_CONFIG_MAP.get("decay_hastener").processing_power(), 74, 36);
    }
}