package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineRotorBearingEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.menu.multiblock.controller.TurbineControllerMenu;
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

public class TurbineControllerScreen extends MultiblockControllerScreen<Turbine, TurbineUpdatePacket, TurbineControllerEntity, BlockEntityMenuInfo<TurbineControllerEntity>, TurbineControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "turbine_controller");

    StringCenteringOperator powerText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.turbine_controller.power", UnitHelper.prefix(multiblock.power, 5, "FE/t")));
    StringCenteringOperator coilCountText = centeredTracker(() -> {
        int bearingCount = multiblock.getPartCount(TurbineRotorBearingEntity.class);
        return Component.translatable(MODID + ".tooltip.turbine_controller.dynamo_coil_count", (bearingCount == 0 ? "0/0, 0/0" : multiblock.dynamoCoilCount + "/" + bearingCount / 2 + ", " + multiblock.dynamoCoilCountOpposite + "/" + bearingCount / 2));
    });
    StringCenteringOperator dynamoEfficiencyText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.turbine_controller.dynamo_efficiency", NCMath.pcDecimalPlaces(multiblock.conductivity, 1)));
    StringCenteringOperator expansionLevelText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.turbine_controller.expansion_level", (multiblock.idealTotalExpansionLevel <= 0D ? "0%" : NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel, 1) + " [" + NCMath.decimalPlaces(multiblock.idealTotalExpansionLevel, 1) + " x " + NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel / multiblock.idealTotalExpansionLevel, 1) + "]")));
    StringCenteringOperator rotorEfficiencyText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.turbine_controller.rotor_efficiency", NCMath.pcDecimalPlaces(multiblock.rotorEfficiency, 1)));
    StringCenteringOperator powerBonusText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.turbine_controller.power_bonus", NCMath.pcDecimalPlaces(multiblock.powerBonus, 1)));
    StringCenteringOperator recipeInputRateText = centeredTracker(() -> {
        double maxRecipeRateMultiplierFP = multiblock.getLogic().getMaxRecipeRateMultiplier();
        double rateRatioFP = maxRecipeRateMultiplierFP <= 0D ? 0D : multiblock.recipeInputRateFP / maxRecipeRateMultiplierFP;
        return Component.translatable(MODID + ".tooltip.turbine_controller.fluid_rate", UnitHelper.prefix(multiblock.recipeInputRateFP, 5, "B/t", -1) + " [" +NCMath.pcDecimalPlaces(rateRatioFP, 1) + (rateRatioFP > 1D ? "] [!]" : "]"));
    });

    public TurbineControllerScreen(TurbineControllerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, gui_texture);
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
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
        int middle_x = getGuiLeft() + imageWidth / 2;
        Component title = Component.translatable(MODID + ".tooltip.turbine_controller", multiblock.getInteriorLengthX(), multiblock.getInteriorLengthY(), multiblock.getInteriorLengthZ());
        guiGraphics.drawString(font, title, middle_x - this.font.width(title) / 2, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawString(FONT, underline, middle_x - this.font.width(underline) / 2, getGuiTop() + 12, fontColor);

        powerText.apply(guiGraphics, 22, fontColor);

        if (NCUtil.isModifierKeyDown()) {
            coilCountText.apply(guiGraphics, 34, fontColor);
        } else {
            dynamoEfficiencyText.apply(guiGraphics, 34, fontColor);
        }

        if (NCUtil.isModifierKeyDown()) {
            expansionLevelText.apply(guiGraphics, 46, fontColor);
        } else {
            rotorEfficiencyText.apply(guiGraphics, 46, fontColor);
        }

        int tensionColor = multiblock.bearingTension <= 0D ? fontColor : multiblock.isTurbineOn ? 0xFFFFFF - NCMath.toInt((255D * Mth.clamp(2D * multiblock.bearingTension, 0D, 1D))) - 256 * NCMath.toInt((255D * Mth.clamp(2D * multiblock.bearingTension - 1D, 0D, 1D))) : FastColor.ARGB32.lerp((float) multiblock.bearingTension, 15641088, 0xFF0000);
        if (NCUtil.isModifierKeyDown()) {
            powerBonusText.apply(guiGraphics, 58, tensionColor);
        } else {
            recipeInputRateText.apply(guiGraphics, 58, tensionColor);
        }
    }
}