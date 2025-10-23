package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.menu.processor.NuclearFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class NuclearFurnaceScreen extends AbstractFurnaceScreen<NuclearFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ncLoc("textures/gui/sprites/screen/nuclear_furnace.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ncLoc("screen/" + "nuclear_furnace_lit");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ncLoc("screen/" + "nuclear_furnace_burn");

    public NuclearFurnaceScreen(NuclearFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new SmeltingRecipeBookComponent(), playerInventory, title, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
    }
}