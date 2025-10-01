package com.nred.nuclearcraft.screen.multiblock;

import com.nred.nuclearcraft.menu.AbstractControllerMenu;
import com.nred.nuclearcraft.multiblock.MachineMultiblock;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.AbstractCuboidMultiblockPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractMultiblockScreen<MENU extends AbstractControllerMenu<?, MULTIBLOCK>, MULTIBLOCK extends MachineMultiblock<MULTIBLOCK>> extends AbstractContainerScreen<MENU> {
    public AbstractMultiblockScreen(MENU menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.titleLabelY = Integer.MIN_VALUE;
        this.inventoryLabelY = Integer.MIN_VALUE;
    }

    protected final MULTIBLOCK multiblock = menu.controller.getMultiblockController().orElseThrow(IllegalStateException::new);
    protected ScreenRectangle base = ScreenRectangle.empty();
    protected static final Font FONT = Minecraft.getInstance().font;
    int imageHeight = 76;

    public void resize(int width, int imageWidth, int height, int imageHeight) {
        base = new ScreenRectangle((width - imageWidth) / 2, (height - imageHeight) / 2, imageWidth, imageHeight);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        resize(this.width, imageWidth, this.height, imageHeight);
    }

    @Override
    protected void init() {
        super.init();

        resize(width, imageWidth, height, imageHeight);
    }
}
