package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.controller.InfiltratorControllerMenu;
import com.nred.nuclearcraft.multiblock.machine.InfiltratorLogic;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
import com.nred.nuclearcraft.payload.multiblock.ClearAllMaterialPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import com.nred.nuclearcraft.util.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class InfiltratorControllerScreen extends LogicMultiblockControllerScreen<Machine, MachineLogic, MachineUpdatePacket, InfiltratorControllerEntity, TileContainerInfo<InfiltratorControllerEntity>, InfiltratorLogic, InfiltratorControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "infiltrator_controller");

    public InfiltratorControllerScreen(InfiltratorControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, gui_texture);
        imageWidth = 176;
        imageHeight = 76;
    }

    @Override
    public void init() {
        super.init();
        clearAllButton = this.addRenderableWidget(new MultiblockButton.ClearAllMaterial(getGuiLeft() + 153, getGuiTop() + 53, (btn) -> {
            if (NCUtil.isModifierKeyDown()) new ClearAllMaterialPacket(tile.getBlockPos()).sendToServer();
        }));
    }

    @Override
    protected ResourceLocation getGuiTexture() {
        return gui_texture;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int fontColor = multiblock.isMachineOn ? -1 : 15641088;
        int middle_x = getGuiLeft() + imageWidth / 2;
        String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize(MODID + ".tooltip.infiltrator_controller");
        guiGraphics.drawCenteredString(this.font, title, middle_x, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawCenteredString(this.font, underline, middle_x, getGuiTop() + 12, fontColor);

        Component pressureChamberEfficiency = Component.translatable(MODID + ".tooltip.infiltrator_controller.pressure_chamber_efficiency", NCMath.pcDecimalPlaces(multiblock.basePowerMultiplier <= 0D ? 0D : multiblock.baseSpeedMultiplier * (1D + getLogic().heatingBonus) / multiblock.basePowerMultiplier, 1));
        guiGraphics.drawCenteredString(this.font, pressureChamberEfficiency, middle_x, getGuiTop() + 22, fontColor);

        Component pressureFluidEfficiency = Component.translatable(MODID + ".tooltip.infiltrator_controller.pressure_fluid_efficiency", NCMath.pcDecimalPlaces(getLogic().pressureFluidEfficiency, 1));
        guiGraphics.drawCenteredString(this.font, pressureFluidEfficiency, middle_x, getGuiTop() + 34, fontColor);

        Component rate = Component.translatable(MODID + ".tooltip.machine_controller.rate", multiblock.recipeUnitInfo.getString(logic.getProcessTimeFP(), 5));
        guiGraphics.drawCenteredString(this.font, rate, middle_x, getGuiTop() + 46, fontColor);

        Component power = Component.translatable(MODID + ".tooltip.machine_controller.power", UnitHelper.prefix(logic.getProcessPower(), 5, "RF/t"));
        guiGraphics.drawCenteredString(this.font, power, middle_x, getGuiTop() + 58, fontColor);
    }
}