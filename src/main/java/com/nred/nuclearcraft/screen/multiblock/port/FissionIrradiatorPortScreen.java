package com.nred.nuclearcraft.screen.multiblock.port;

import com.nred.nuclearcraft.block_entity.fission.port.FissionIrradiatorPortEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.port.FissionIrradiatorPortMenu;
import com.nred.nuclearcraft.payload.multiblock.port.ItemPortUpdatePacket;
import com.nred.nuclearcraft.screen.GuiInfoTile;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class FissionIrradiatorPortScreen extends GuiInfoTile<FissionIrradiatorPortMenu, FissionIrradiatorPortEntity, ItemPortUpdatePacket, TileContainerInfo<FissionIrradiatorPortEntity>> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "fission_irradiator_port");

    public FissionIrradiatorPortScreen(FissionIrradiatorPortMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, gui_texture);
        titleLabelY = Integer.MIN_VALUE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(gui_texture, getGuiLeft(), getGuiTop(), 256, 256);

        renderFakeItem(guiGraphics, tile.getFilterStacks().get(0), getGuiLeft() + 44, getGuiTop() + 35, 0.5f);

        int fontColor = tile.getMultiblockController().isPresent() && tile.getMultiblockController().get().isReactorOn ? -1 : 15641088;
        guiGraphics.drawCenteredString(FONT, getTitle(), width / 2, getGuiTop() + 6, fontColor);
    }
}