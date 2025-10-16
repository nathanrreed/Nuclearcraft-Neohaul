package com.nred.nuclearcraft.screen.multiblock.port;

import com.nred.nuclearcraft.block_entity.fission.port.FissionCellPortEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.port.FissionCellPortMenu;
import com.nred.nuclearcraft.payload.multiblock.port.ItemPortUpdatePacket;
import com.nred.nuclearcraft.screen.GuiInfoTile;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class FissionCellPortScreen extends GuiInfoTile<FissionCellPortEntity, ItemPortUpdatePacket, TileContainerInfo<FissionCellPortEntity>, FissionCellPortMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "fission_cell_port");

    public FissionCellPortScreen(FissionCellPortMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        titleLabelY = Integer.MIN_VALUE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(gui_texture, getGuiLeft(), getGuiTop(), 256, 256);

        guiGraphics.setColor(1, 1, 1, 0.5f);
        guiGraphics.renderFakeItem(tile.getFilterStacks().getFirst(), getGuiLeft() + 44, getGuiTop() + 35);
        guiGraphics.setColor(1, 1, 1, 1);

        int fontColor = tile.getMultiblockController().isPresent() && tile.getMultiblockController().get().isReactorOn ? -1 : 15641088;
        guiGraphics.drawCenteredString(FONT, getTitle(), width / 2, getGuiTop() + 6, fontColor);
    }
}