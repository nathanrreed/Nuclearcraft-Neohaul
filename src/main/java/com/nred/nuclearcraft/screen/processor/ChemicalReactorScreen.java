package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.ChemicalReactorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ChemicalReactorScreen extends ProcessorScreen<ChemicalReactorMenu> {
    public ChemicalReactorScreen(ChemicalReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, "chemical_reactor", 70, 34);
    }
}