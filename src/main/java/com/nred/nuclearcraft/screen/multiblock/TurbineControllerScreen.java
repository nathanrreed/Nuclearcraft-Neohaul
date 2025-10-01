package com.nred.nuclearcraft.screen.multiblock;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.turbine.TurbineRotorBearingEntity;
import com.nred.nuclearcraft.menu.multiblock.TurbineControllerMenu;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.payload.ClearPayload;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.NCUtil;
import com.nred.nuclearcraft.util.StringHelper;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class TurbineControllerScreen extends AbstractMultiblockScreen<TurbineControllerMenu, Turbine> {
    private static final ResourceLocation BASE = ncLoc("screen/" + "turbine_controller");

    ImageButton imagebutton;

    public TurbineControllerScreen(TurbineControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imagebutton = this.addRenderableWidget(new ImageButton(base.right() - 23, base.top() + 5, 18, 18, new WidgetSprites(ncLoc("button/void_excess_off"), ncLoc("button/void_excess_on")), button -> {
            PacketDistributor.sendToServer(new ClearPayload(0, menu.controller.getBlockPos()));
        }));
        imagebutton.visible = false;
    }

    public void resize(int width, int imageWidth, int height, int imageHeight) {
        super.resize(width, imageWidth, height, imageHeight);
        imagebutton.setPosition(base.right() - 23, base.top() + 5);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        imagebutton.visible = Screen.hasShiftDown();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        imagebutton.visible = Screen.hasShiftDown();
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(BASE, 256, 256, 0, 0, base.left(), base.top(), imageWidth, imageHeight);

        int fontColor = multiblock.isTurbineOn ? -1 : 15641088;
        int middle_x = base.getCenterInAxis(ScreenAxis.HORIZONTAL);
        Component title = Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.title", multiblock.getInteriorLengthX(), multiblock.getInteriorLengthY(), multiblock.getInteriorLengthZ());
        guiGraphics.drawString(font, title, middle_x - this.font.width(title) / 2, base.top() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawString(FONT, underline, middle_x - this.font.width(underline) / 2, base.top() + 12, fontColor);

        Component power = Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.power", UnitHelper.prefix(Math.round(multiblock.power), 5, "RF/t"));
        guiGraphics.drawString(FONT, power, middle_x - this.font.width(power) / 2, base.top() + 22, fontColor);

        int bearingCount = multiblock.getPartCount(TurbineRotorBearingEntity.class);
        Component coils = NCUtil.isModifierKeyDown() ? Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.dynamo_coil_count", (bearingCount == 0 ? "0/0, 0/0" : multiblock.dynamoCoilCount + "/" + bearingCount / 2 + ", " + multiblock.dynamoCoilCountOpposite + "/" + bearingCount / 2)) : Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.dynamo_efficiency", NCMath.pcDecimalPlaces(multiblock.conductivity, 1));
        guiGraphics.drawString(FONT, coils, middle_x - this.font.width(coils) / 2, base.top() + 34, fontColor);

        Component rotor = NCUtil.isModifierKeyDown() ? Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.expansion_level", (multiblock.idealTotalExpansionLevel <= 0D ? "0%" : NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel, 1) + " [" + NCMath.decimalPlaces(multiblock.idealTotalExpansionLevel, 1) + " x " + NCMath.pcDecimalPlaces(multiblock.totalExpansionLevel / multiblock.idealTotalExpansionLevel, 1) + "]")) : Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.rotor_efficiency", NCMath.pcDecimalPlaces(multiblock.rotorEfficiency, 1));
        guiGraphics.drawString(FONT, rotor, middle_x - this.font.width(rotor) / 2, base.top() + 46, fontColor);

        Component inputRate;
        if (NCUtil.isModifierKeyDown()) {
            inputRate = Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.power_bonus", NCMath.pcDecimalPlaces(multiblock.powerBonus, 1));
            if (imagebutton.isHovered())
                guiGraphics.renderTooltip(FONT, Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.shift_clear_multiblock").withStyle(ChatFormatting.ITALIC), mouseX, mouseY);
        } else {
            double maxRecipeRateMultiplierFP = multiblock.getMaxRecipeRateMultiplier();
            double rateRatio = (double) multiblock.recipeInputRate / maxRecipeRateMultiplierFP;
            double rateRatioFP = multiblock.recipeInputRateFP / maxRecipeRateMultiplierFP;
            inputRate = Component.translatable(NuclearcraftNeohaul.MODID + ".menu.turbine_controller.fluid_rate", UnitHelper.prefix(Math.round(multiblock.recipeInputRateFP), 5, "B/t", -1), " [" + NCMath.pcDecimalPlaces(rateRatioFP, 1) + (rateRatio > 1D ? "] [!]" : "]"));
        }
        guiGraphics.drawString(FONT, inputRate, middle_x - this.font.width(inputRate) / 2, base.top() + 58, multiblock.bearingTension <= 0D ? fontColor : multiblock.isTurbineOn ? 0xFFFFFF - NCMath.toInt((255D * Mth.clamp(2D * multiblock.bearingTension, 0D, 1D))) - 256 * NCMath.toInt((255D * Mth.clamp(2D * multiblock.bearingTension - 1D, 0D, 1D))) : FastColor.ARGB32.lerp((float) multiblock.bearingTension, 15641088, 0xFF0000));
    }
}