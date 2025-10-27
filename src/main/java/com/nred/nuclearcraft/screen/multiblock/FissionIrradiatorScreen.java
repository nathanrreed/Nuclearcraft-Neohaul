package com.nred.nuclearcraft.screen.multiblock;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fission.FissionIrradiatorEntity;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.FissionIrradiatorMenu;
import com.nred.nuclearcraft.payload.multiblock.FissionIrradiatorUpdatePacket;
import com.nred.nuclearcraft.screen.processor.GuiProcessorImpl.GuiBasicFilteredProcessor;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class FissionIrradiatorScreen extends GuiBasicFilteredProcessor<FissionIrradiatorMenu, FissionIrradiatorEntity, FissionIrradiatorUpdatePacket> {
    public FissionIrradiatorScreen(FissionIrradiatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ncLoc("screen/" + "fission_irradiator"));
        inventoryLabelY = Integer.MIN_VALUE;
        titleLabelY = Integer.MIN_VALUE;
    }

    @Override
    protected void initConfigButtons() {
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        int fontColor = tile.getMultiblockController().isPresent() && tile.getMultiblockController().get().isReactorOn ? -1 : 15641088;
        guiGraphics.drawCenteredString(FONT, title, width / 2, getGuiTop() + 6, fontColor);
    }

    @Override
    protected void drawBars(GuiGraphics guiGraphics) {
        drawProgressBar(guiGraphics);

        if (tile.clusterHeatCapacity >= 0L) {
            int e = NCMath.toInt(Math.round(74D * tile.clusterHeatStored / tile.clusterHeatCapacity));
            guiGraphics.blitSprite(guiTextures, 256, 256, getGuiLeft() + 8, getGuiTop() + 6 + 74 - e, 176, 90 + 74 - e, 16, e);
        } else {
            guiGraphics.fillGradient(getGuiLeft() + 8, getGuiTop() + 6, getGuiLeft() + 8 + 16, getGuiTop() + 6 + 74, 0xFF777777, 0xFF535353);
        }
    }

    @Override
    protected void renderBarTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawTooltip(guiGraphics, tile.clusterHeatCapacity >= 0L ? heatInfo() : noClusterInfo(), mouseX, mouseY, 8, 6, 16, 74);
    }

    public List<Component> heatInfo() {
        String heat = UnitHelper.prefix(tile.clusterHeatStored, tile.clusterHeatCapacity, 5, "H");
        return Lists.newArrayList(Component.translatable(MODID + ".tooltip.fission_controller.heat_stored", heat).withStyle(ChatFormatting.YELLOW));
    }

    @Override
    protected void renderConfigButtonTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }
}