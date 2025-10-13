package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.ContainerMultiblockController;
import com.nred.nuclearcraft.multiblock.IMultiblockGuiPart;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import com.nred.nuclearcraft.screen.GuiInfoTile;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class GuiMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & IMultiblockGuiPart<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>, MENU extends ContainerMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO>> extends GuiInfoTile<CONTROLLER, PACKET, INFO, MENU> {
    protected final MULTIBLOCK multiblock;
    protected MultiblockButton.ClearAllMaterial clearAllButton;

    public GuiMultiblockController(MENU menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.multiblock = menu.tile.getMultiblockController().orElseThrow();

        inventoryLabelY = Integer.MIN_VALUE;
        titleLabelY = Integer.MIN_VALUE;
    }

//    @Override TODO
//    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        GlStateManager.color(1F, 1F, 1F, 1F);
//        mc.getTextureManager().bindTexture(getGuiTexture());
//        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
//    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(getGuiTexture(), 256, 256, 0, 0, getGuiLeft(), getGuiTop(), imageWidth, imageHeight);
        clearAllButton.visible = NCUtil.isModifierKeyDown();
    }

    protected abstract ResourceLocation getGuiTexture();
}