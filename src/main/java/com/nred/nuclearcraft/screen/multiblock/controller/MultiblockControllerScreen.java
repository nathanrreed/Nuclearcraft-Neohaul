package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.menu.multiblock.controller.MultiblockControllerMenu;
import com.nred.nuclearcraft.multiblock.IMultiblockScreenPart;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import com.nred.nuclearcraft.screen.InfoTileScreen;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class MultiblockControllerScreen<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & IMultiblockScreenPart<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends BlockEntityMenuInfo<CONTROLLER>, MENU extends MultiblockControllerMenu<MULTIBLOCK, PACKET, CONTROLLER, INFO>> extends InfoTileScreen<MENU, CONTROLLER, PACKET, INFO> {
    protected final MULTIBLOCK multiblock;
    protected MultiblockButton.ClearAllMaterial clearAllButton;

    public MultiblockControllerScreen(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
        super(menu, inventory, title, textureLocation);

        this.multiblock = menu.tile.getMultiblockController().orElseThrow();

        inventoryLabelY = Integer.MIN_VALUE;
        titleLabelY = Integer.MIN_VALUE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(getGuiTexture(), 256, 256, 0, 0, getGuiLeft(), getGuiTop(), imageWidth, imageHeight);
        clearAllButton.visible = NCUtil.isModifierKeyDown();
    }

    protected abstract ResourceLocation getGuiTexture(); // TODO REMOVE?
}