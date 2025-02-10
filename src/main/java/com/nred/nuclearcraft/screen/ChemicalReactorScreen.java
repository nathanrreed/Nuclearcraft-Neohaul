package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.menu.ChemicalReactorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;

public class ChemicalReactorScreen extends ProcessorScreen<ChemicalReactorMenu> {
    public ChemicalReactorScreen(ChemicalReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "chemical_reactor", PROCESSOR_CONFIG_MAP.get("chemical_reactor").processing_power(), 70, 34);
    }
}