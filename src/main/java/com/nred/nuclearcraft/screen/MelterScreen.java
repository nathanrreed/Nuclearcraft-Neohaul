package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.MelterMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class MelterScreen extends ProcessorScreen<MelterMenu> {
    public MelterScreen(MelterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "melter", PROCESSOR_CONFIG_MAP.get("melter").processing_power(), 74, 35);
    }
}