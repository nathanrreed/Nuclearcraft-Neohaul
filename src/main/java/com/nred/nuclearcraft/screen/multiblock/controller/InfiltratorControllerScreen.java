package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
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

public class InfiltratorControllerScreen extends LogicMultiblockControllerScreen<Machine, MachineLogic, MachineUpdatePacket, InfiltratorControllerEntity, BlockEntityMenuInfo<InfiltratorControllerEntity>, InfiltratorLogic, InfiltratorControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "infiltrator_controller");

    StringCenteringOperator pressureChamberEfficiencyText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.infiltrator_controller.pressure_chamber_efficiency", NCMath.pcDecimalPlaces(multiblock.basePowerMultiplier <= 0D ? 0D : multiblock.baseSpeedMultiplier * (1D + getLogic().heatingBonus) / multiblock.basePowerMultiplier, 1)));
    StringCenteringOperator pressureFluidEfficiencyText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.infiltrator_controller.pressure_fluid_efficiency", NCMath.pcDecimalPlaces(getLogic().pressureFluidEfficiency, 1)));
    StringCenteringOperator rateText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.machine_controller.rate", multiblock.recipeUnitInfo.getString(multiblock.readyToProcess ? logic.getProcessTimeFP() : null, 5)));
    StringCenteringOperator powerText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.machine_controller.power", UnitHelper.prefix(logic.getProcessPower(), 5, "RF/t")));

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

        pressureChamberEfficiencyText.apply(guiGraphics, 22, fontColor);

        pressureFluidEfficiencyText.apply(guiGraphics, 34, fontColor);

        rateText.apply(guiGraphics, 46, fontColor);

        powerText.apply(guiGraphics, 58, fontColor);
    }
}