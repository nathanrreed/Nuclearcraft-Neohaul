package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.CrystallizerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class CrystallizerScreen extends ProcessorScreen<CrystallizerMenu> {
    public CrystallizerScreen(CrystallizerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "crystallizer", PROCESSOR_CONFIG_MAP.get("crystallizer").processing_power(), 74, 36);
    }
}