package com.nred.nuclearcraft.screen.multiblock;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.menu.multiblock.SolidFissionControllerMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.SolidFuelFissionLogic;
import com.nred.nuclearcraft.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class SolidFissionControllerScreen extends AbstractMultiblockScreen<SolidFissionControllerMenu, FissionReactor> {
    private SolidFuelFissionLogic logic = menu.logic;
    private static final ResourceLocation BASE = ncLoc("screen/" + "solid_fission_controller");
    private ScreenRectangle bar;
    int imageHeight = 114;

    public SolidFissionControllerScreen(SolidFissionControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public int getClearButtonY() {
        return base.top() + 80;
    }

    @Override
    public void resize(int width, int imageWidth, int height, int imageHeight) {
        super.resize(width, imageWidth, height, imageHeight);

        bar = new ScreenRectangle(base.left() + 6, base.top() + 102, 164, 6);
    }

    public List<Component> heatInfo() {
        List<Component> info = new ArrayList<>();
        info.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.heat_stored", Component.literal(UnitHelper.prefix(logic.heatBuffer.getHeatStored(), logic.heatBuffer.getHeatCapacity(), 5, "H")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        info.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.net_cluster_heating", Component.literal(UnitHelper.prefix(logic.getNetClusterHeating(), 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.YELLOW));
        info.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.total_cluster_cooling", Component.literal(UnitHelper.prefix(-multiblock.cooling, 5, "H/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.BLUE));
        return info;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        if (bar.containsPoint(mouseX, mouseY)) {
            guiGraphics.renderTooltip(this.font, heatInfo(), Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(BASE, 256, 256, 0, 0, base.left(), base.top(), imageWidth, imageHeight);

        int fontColor = multiblock.isReactorOn ? -1 : 15641088;
        int middle_x = base.getCenterInAxis(ScreenAxis.HORIZONTAL);
        String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize(NuclearcraftNeohaul.MODID + ".tooltip.solid_fission_controller");
        guiGraphics.drawString(this.font, title, middle_x - this.font.width(title) / 2, base.top() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawString(this.font, underline, middle_x - this.font.width(underline) / 2, base.top() + 12, fontColor);

        Component clusters = Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.clusters", multiblock.clusterCount);
        guiGraphics.drawString(this.font, clusters, middle_x - this.font.width(clusters) / 2, base.top() + 22, fontColor);

        Component efficiency = NCUtil.isModifierKeyDown() ? Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.heat_mult", NCMath.pcDecimalPlaces(multiblock.meanHeatMult, 1)) : Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.efficiency", NCMath.pcDecimalPlaces(multiblock.meanEfficiency, 1));
        guiGraphics.drawString(this.font, efficiency, middle_x - this.font.width(efficiency) / 2, base.top() + 34, fontColor);

        String outputRate = Lang.localize(NuclearcraftNeohaul.MODID + ".tooltip.solid_fission_controller.output_rate") + " " + UnitHelper.prefix(logic.heatingOutputRateFP, 5, "B/t", -1);
        guiGraphics.drawString(this.font, outputRate, middle_x - this.font.width(outputRate) / 2, base.top() + 46, fontColor);

        Component usefulParts = NCUtil.isModifierKeyDown() ? Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.sparsity", NCMath.pcDecimalPlaces(multiblock.sparsityEfficiencyMult, 1)) : Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.useful_parts", multiblock.usefulPartCount + "/" + multiblock.getInteriorVolume());
        guiGraphics.drawString(this.font, usefulParts, middle_x - this.font.width(usefulParts) / 2, base.top() + 58, fontColor);

        Component temperature = Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.temperature", (NCUtil.isModifierKeyDown() ? Math.round(logic.getTemperature() - 273.15D) + " C" : Math.round(logic.getTemperature()) + " K"));
        guiGraphics.drawString(this.font, temperature, middle_x - this.font.width(temperature) / 2, base.top() + (NCUtil.isModifierKeyDown() ? 70 : 76), fontColor);

        if (!NCUtil.isModifierKeyDown()) {
            String netClusterHeating = Lang.localize(NuclearcraftNeohaul.MODID + ".tooltip.fission_controller.net_cluster_heating", UnitHelper.prefix(logic.getNetClusterHeating(), 5, "H/t"));
            guiGraphics.drawString(this.font, netClusterHeating, middle_x - this.font.width(netClusterHeating) / 2, base.top() + 88, fontColor);
        }

        int h = NCMath.toInt(Math.round((double) logic.heatBuffer.getHeatStored() / (double) logic.heatBuffer.getHeatCapacity() * 164));
        guiGraphics.blitSprite(BASE, 256, 256, 3, 114, bar.left(), bar.top(), h, bar.height());

        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}