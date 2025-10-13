package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block.fission.SolidFissionControllerEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.SolidFissionControllerMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.multiblock.fisson.solid.SolidFuelFissionLogic;
import com.nred.nuclearcraft.payload.multiblock.ClearAllMaterialPacket;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import com.nred.nuclearcraft.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class SolidFissionControllerScreen extends GuiLogicMultiblockController<FissionReactor, FissionReactorLogic, FissionUpdatePacket, SolidFissionControllerEntity, TileContainerInfo<SolidFissionControllerEntity>, SolidFuelFissionLogic, SolidFissionControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "solid_fission_controller");

    public SolidFissionControllerScreen(SolidFissionControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 114;
    }

    @Override
    protected void init() {
        super.init();
        clearAllButton = this.addRenderableWidget(new MultiblockButton.ClearAllMaterial(getGuiLeft() + 153, getGuiTop() + 81, (btn) -> {
            if (NCUtil.isModifierKeyDown()) new ClearAllMaterialPacket(tile.getTilePos()).sendToServer();
        }));
    }

    @Override
    protected ResourceLocation getGuiTexture() {
        return gui_texture;
    }

    public List<Component> heatInfo() {
        List<Component> info = new ArrayList<>();
        info.add(Component.translatable(MODID + ".tooltip.fission_controller.heat_stored", Component.literal(UnitHelper.prefix(logic.heatBuffer.getHeatStored(), logic.heatBuffer.getHeatCapacity(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        info.add(Component.translatable(MODID + ".tooltip.fission_controller.net_cluster_heating", Component.literal(UnitHelper.prefix(getLogic().getNetClusterHeating(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        info.add(Component.translatable(MODID + ".tooltip.fission_controller.total_cluster_cooling", Component.literal(UnitHelper.prefix(-multiblock.cooling, 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.BLUE));
        return info;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        drawTooltip(guiGraphics, heatInfo(), mouseX, mouseY, 6, 103, 164, 6);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int fontColor = multiblock.isReactorOn ? -1 : 15641088;
        int middle_x = getGuiLeft() + imageWidth / 2;
        String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize(MODID + ".tooltip.solid_fission_controller");
        guiGraphics.drawString(this.font, title, middle_x - this.font.width(title) / 2, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawString(this.font, underline, middle_x - this.font.width(underline) / 2, getGuiTop() + 12, fontColor);

        Component clusters = Component.translatable(MODID + ".tooltip.fission_controller.clusters", multiblock.clusterCount);
        guiGraphics.drawString(this.font, clusters, middle_x - this.font.width(clusters) / 2, getGuiTop() + 22, fontColor);

        Component efficiency = NCUtil.isModifierKeyDown() ? Component.translatable(MODID + ".tooltip.fission_controller.heat_mult", NCMath.pcDecimalPlaces(multiblock.meanHeatMult, 1)) : Component.translatable(MODID + ".tooltip.fission_controller.efficiency", NCMath.pcDecimalPlaces(multiblock.meanEfficiency, 1));
        guiGraphics.drawString(this.font, efficiency, middle_x - this.font.width(efficiency) / 2, getGuiTop() + 34, fontColor);

        Component outputRate = Component.translatable(MODID + ".tooltip.solid_fission_controller.output_rate", UnitHelper.prefix(getLogic().heatingOutputRateFP, 5, "B/t", -1));
        guiGraphics.drawString(this.font, outputRate, middle_x - this.font.width(outputRate) / 2, getGuiTop() + 46, fontColor);

        Component usefulParts = NCUtil.isModifierKeyDown() ? Component.translatable(MODID + ".tooltip.fission_controller.sparsity", NCMath.pcDecimalPlaces(multiblock.sparsityEfficiencyMult, 1)) : Component.translatable(MODID + ".tooltip.fission_controller.useful_parts", multiblock.usefulPartCount + "/" + multiblock.getInteriorVolume());
        guiGraphics.drawString(this.font, usefulParts, middle_x - this.font.width(usefulParts) / 2, getGuiTop() + 58, fontColor);

        Component temperature = Component.translatable(MODID + ".tooltip.fission_controller.temperature", (NCUtil.isModifierKeyDown() ? Math.round(logic.getTemperature() - 273.15D) + " C" : Math.round(logic.getTemperature()) + " K"));
        guiGraphics.drawString(this.font, temperature, middle_x - this.font.width(temperature) / 2, getGuiTop() + (NCUtil.isModifierKeyDown() ? 70 : 76), fontColor);

        if (!NCUtil.isModifierKeyDown()) {
            String netClusterHeating = Lang.localize(MODID + ".tooltip.fission_controller.net_cluster_heating", UnitHelper.prefix(getLogic().getNetClusterHeating(), 5, "H/t"));
            guiGraphics.drawString(this.font, netClusterHeating, middle_x - this.font.width(netClusterHeating) / 2, getGuiTop() + 88, fontColor);
        }

        int h = NCMath.toInt(Math.round((double) logic.heatBuffer.getHeatStored() / (double) logic.heatBuffer.getHeatCapacity() * 164));
        guiGraphics.blitSprite(getGuiTexture(), 256, 256, 3, 114, getGuiLeft() + 6, getGuiTop() + 102, h, 6);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}