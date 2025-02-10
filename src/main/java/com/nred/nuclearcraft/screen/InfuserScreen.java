package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.InfuserMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class InfuserScreen extends ProcessorScreen<InfuserMenu> {
    public InfuserScreen(InfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "fluid_infuser", PROCESSOR_CONFIG_MAP.get("fluid_infuser").processing_power(), 84, 36);
    }
}