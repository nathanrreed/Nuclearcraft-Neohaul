package com.nred.nuclearcraft.screen.multiblock.port;

import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.port.FissionVesselPortMenu;
import com.nred.nuclearcraft.payload.gui.ClearFilterTankPacket;
import com.nred.nuclearcraft.payload.gui.ClearTankPacket;
import com.nred.nuclearcraft.payload.multiblock.port.FluidPortUpdatePacket;
import com.nred.nuclearcraft.screen.GuiInfoTile;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class FissionVesselPortScreen extends GuiInfoTile<FissionVesselPortMenu, FissionVesselPortEntity, FluidPortUpdatePacket, TileContainerInfo<FissionVesselPortEntity>> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "fission_vessel_port");

    public FissionVesselPortScreen(FissionVesselPortMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, gui_texture);
        titleLabelY = Integer.MIN_VALUE;
    }

    @Override
    public void init() {
        super.init();
        initButtons();
    }

    public void initButtons() {
        addWidget(new NCButton.ClearTank(0, getGuiLeft() + 40, getGuiTop() + 31, 24, 24, this::clearTankPressed));
        addWidget(new NCButton.ClearTank(1, getGuiLeft() + 112, getGuiTop() + 31, 24, 24, this::clearTankPressed));
    }

    protected void clearTankPressed(NCButton guiButton, int pressed) {
        if (tile.getLevel().isClientSide) {
            for (int i = 0; i < 2; ++i) {
                if (guiButton.id == i && NCUtil.isModifierKeyDown()) {
                    (tile.getTanks().get(i).isEmpty() ? new ClearFilterTankPacket(tile, i) : new ClearTankPacket(tile, i)).sendToServer();
                    return;
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(gui_texture, getGuiLeft(), getGuiTop(), 256, 256);

        Tank filterTank = tile.getFilterTanks().get(0);
        if (!filterTank.isEmpty()) {
            renderGuiTank(guiGraphics, filterTank, getGuiLeft() + 40, getGuiTop() + 31, 24, 24, 0.5f);
        }

        renderGuiTank(guiGraphics, tile.getTanks().get(0), getGuiLeft() + 40, getGuiTop() + 31, 24, 24, 1f);
        renderGuiTank(guiGraphics, tile.getTanks().get(1), getGuiLeft() + 112, getGuiTop() + 31, 24, 24, 1f);

        int fontColor = tile.getMultiblockController().isPresent() && tile.getMultiblockController().get().isReactorOn ? -1 : 15641088;
        guiGraphics.drawCenteredString(FONT, getTitle(), width / 2, getGuiTop() + 6, fontColor);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawFilteredFluidTooltip(guiGraphics, tile.getTanks().get(0), tile.getFilterTanks().get(0), mouseX, mouseY, 40, 31, 24, 24);
        drawFilteredFluidTooltip(guiGraphics, tile.getTanks().get(1), tile.getFilterTanks().get(1), mouseX, mouseY, 112, 31, 24, 24);
    }
}