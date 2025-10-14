package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.block.turbine.TurbineRotorBearingEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.TurbineControllerMenu;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.payload.multiblock.ClearAllMaterialPacket;
import com.nred.nuclearcraft.payload.multiblock.TurbineUpdatePacket;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.NCUtil;
import com.nred.nuclearcraft.util.StringHelper;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class TurbineControllerScreen extends GuiMultiblockController<Turbine, TurbineUpdatePacket, TurbineControllerEntity, TileContainerInfo<TurbineControllerEntity>, TurbineControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "turbine_controller");

    public TurbineControllerScreen(TurbineControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 76;
    }

    @Override
    protected void init() {
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

        int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
        int middle_x = getGuiLeft() + imageWidth / 2;
        Component title = Component.translatable(MODID + ".menu.turbine_controller.title", multiblock.getInteriorLengthX(), multiblock.getInteriorLengthY(), multiblock.getInteriorLengthZ());
        guiGraphics.drawString(font, title, middle_x - this.font.width(title) / 2, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawString(FONT, underline, middle_x - this.font.width(underline) / 2, getGuiTop() + 12, fontColor);

        Component power = Component.translatable(MODID + ".menu.turbine_controller.power", UnitHelper.prefix(Math.round(multiblock.power), 5, "RF/t"));
        guiGraphics.drawString(FONT, power, middle_x - this.font.width(power) / 2, getGuiTop() + 22, fontColor);

        int bearingCount = multiblock.getPartCount(TurbineRotorBearingEntity.class);
        Component coils = NCUtil.isModifierKeyDown() ? Component.translatable(MODID + ".menu.turbine_controller.dynamo_coil_count", (bearingCount == 0 ? "0/0, 0/0" : multiblock.dynamoCoilCount + "/" + bearingCount / 2 + ", " + multiblock.dynamoCoilCountOpposite + "/" + bearingCount / 2)) : Component.translatable(MODID + ".menu.turbine_controller.dynamo_efficiency", NCMath.pcDecimalPlaces(multiblock.conductivity, 1));
        guiGraphics.drawString(FONT, coils, middle_x - this.font.width(coils) / 2, getGuiTop() + 34, fontColor);

        Component rotor = NCUtil.isModifierKeyDown() ? Component.translatable(MODID + ".menu.turbine_controller.expansion_level", (multiblock.idealTotalExpansionLevel <= 0D ? "0%" : NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel, 1) + " [" + NCMath.decimalPlaces(multiblock.idealTotalExpansionLevel, 1) + " x " + NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel / multiblock.idealTotalExpansionLevel, 1) + "]")) : Component.translatable(MODID + ".menu.turbine_controller.rotor_efficiency", NCMath.pcDecimalPlaces(multiblock.rotorEfficiency, 1));
        guiGraphics.drawString(FONT, rotor, middle_x - this.font.width(rotor) / 2, getGuiTop() + 46, fontColor);

        Component inputRate;
        if (NCUtil.isModifierKeyDown()) {
            inputRate = Component.translatable(MODID + ".menu.turbine_controller.power_bonus", NCMath.pcDecimalPlaces(multiblock.powerBonus, 1));
        } else {
            double maxRecipeRateMultiplierFP = multiblock.getLogic().getMaxRecipeRateMultiplier();
            double rateRatio = (double) multiblock.recipeInputRate / maxRecipeRateMultiplierFP;
            double rateRatioFP = multiblock.recipeInputRateFP / maxRecipeRateMultiplierFP;
            inputRate = Component.translatable(MODID + ".menu.turbine_controller.fluid_rate", UnitHelper.prefix(Math.round(multiblock.recipeInputRateFP), 5, "B/t", -1), " [" + NCMath.pcDecimalPlaces(rateRatioFP, 1) + (rateRatio > 1D ? "] [!]" : "]"));
        }
        guiGraphics.drawString(FONT, inputRate, middle_x - this.font.width(inputRate) / 2, getGuiTop() + 58, multiblock.bearingTension <= 0D ? fontColor : multiblock.isTurbineOn ? 0xFFFFFF - NCMath.toInt((255D * Mth.clamp(2D * multiblock.bearingTension, 0D, 1D))) - 256 * NCMath.toInt((255D * Mth.clamp(2D * multiblock.bearingTension - 1D, 0D, 1D))) : FastColor.ARGB32.lerp((float) multiblock.bearingTension, 15641088, 0xFF0000));
    }
}