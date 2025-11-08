package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.controller.CondenserControllerMenu;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerLogic;
import com.nred.nuclearcraft.payload.multiblock.ClearAllMaterialPacket;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerUpdatePacket;
import com.nred.nuclearcraft.util.Lang;
import com.nred.nuclearcraft.util.NCUtil;
import com.nred.nuclearcraft.util.StringHelper;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class CondenserControllerScreen extends LogicMultiblockControllerScreen<HeatExchanger, HeatExchangerLogic, HeatExchangerUpdatePacket, CondenserControllerEntity, TileContainerInfo<CondenserControllerEntity>, HeatExchangerLogic, CondenserControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "heat_exchanger_controller");

    public CondenserControllerScreen(CondenserControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, gui_texture);
        imageWidth = 176;
        imageHeight = 76;
    }

    @Override
    public void init() {
        super.init();
        clearAllButton = this.addRenderableWidget(new MultiblockButton.ClearAllMaterial(getGuiLeft() + 153, getGuiTop() + 5, (btn) -> {
            if (NCUtil.isModifierKeyDown()) new ClearAllMaterialPacket(tile.getTilePos()).sendToServer();
        }));
    }

    @Override
    protected ResourceLocation getGuiTexture() {
        return gui_texture;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int fontColor = multiblock.isExchangerOn ? -1 : 0xEE9270;
        int middle_x = getGuiLeft() + imageWidth / 2;
        String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize(MODID + ".tooltip.condenser_controller");
        guiGraphics.drawCenteredString(this.font, title, middle_x, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawCenteredString(this.font, underline, middle_x, getGuiTop() + 12, fontColor);

        if (NCUtil.isModifierKeyDown()) {
            Component networkCount = Component.translatable(MODID + ".tooltip.heat_exchanger_controller.active_network_count", multiblock.activeNetworkCount + "/" + multiblock.totalNetworkCount);
            guiGraphics.drawCenteredString(this.font, networkCount, middle_x, getGuiTop() + 22, fontColor);
        } else {
            Component tubeCount = Component.translatable(MODID + ".tooltip.heat_exchanger_controller.active_tube_count", multiblock.activeTubeCount + "/" + multiblock.getPartCount(HeatExchangerTubeEntity.class));
            guiGraphics.drawCenteredString(this.font, tubeCount, middle_x, getGuiTop() + 22, fontColor);
        }

        Component tubeInputRate = Component.translatable(MODID + ".tooltip.heat_exchanger_controller.tube_input", UnitHelper.prefix(Math.round(multiblock.tubeInputRateFP), 5, "B/t", -1));
        guiGraphics.drawCenteredString(this.font, tubeInputRate, middle_x, getGuiTop() + 34, fontColor);

        Component heatDissipationRate = Component.translatable(MODID + ".tooltip.heat_exchanger_controller.heat_dissipation_rate", UnitHelper.prefix(Math.round(multiblock.heatTransferRateFP), 5, "H/t"));
        guiGraphics.drawCenteredString(this.font, heatDissipationRate, middle_x, getGuiTop() + 46, fontColor);

        Component meanTempDiff = Component.translatable(MODID + ".tooltip.heat_exchanger_controller.mean_temp_diff", UnitHelper.prefix(multiblock.activeContactCount == 0 ? 0D : Math.round(multiblock.totalTempDiff / multiblock.activeContactCount), 5, "K"));
        guiGraphics.drawCenteredString(this.font, meanTempDiff, middle_x, getGuiTop() + 58, fontColor);
    }
}