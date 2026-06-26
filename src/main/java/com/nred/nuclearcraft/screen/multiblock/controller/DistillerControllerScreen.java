package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.menu.multiblock.controller.DistillerControllerMenu;
import com.nred.nuclearcraft.multiblock.machine.DistillerLogic;
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

public class DistillerControllerScreen extends LogicMultiblockControllerScreen<Machine, MachineLogic, MachineUpdatePacket, DistillerControllerEntity, BlockEntityMenuInfo<DistillerControllerEntity>, DistillerLogic, DistillerControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "distiller_controller");

    StringCenteringOperator refluxBonusText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.distiller_controller.reflux_bonus", NCMath.pcDecimalPlaces(getLogic().refluxUnitBonus, 1)));
    StringCenteringOperator reboilingBonusText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.distiller_controller.reboiling_bonus", NCMath.pcDecimalPlaces(getLogic().reboilingUnitBonus, 1)));
    StringCenteringOperator distributionBonusText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.distiller_controller.distribution_bonus", NCMath.pcDecimalPlaces(getLogic().liquidDistributorBonus, 1)));
    StringCenteringOperator rateText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.machine_controller.rate", multiblock.recipeUnitInfo.getString(multiblock.readyToProcess ? logic.getProcessTimeFP() : null, 5)));
    StringCenteringOperator powerText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.machine_controller.power", UnitHelper.prefix(logic.getProcessPower(), 5, "FE/t")));

    public DistillerControllerScreen(DistillerControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, gui_texture);
        imageWidth = 176;
        imageHeight = 88;
    }

    @Override
    public void init() {
        super.init();
        clearAllButton = this.addRenderableWidget(new MultiblockButton.ClearAllMaterial(getGuiLeft() + 153, getGuiTop() + 65, (btn) -> {
            if (NCUtil.isModifierKeyDown()) new ClearAllMaterialPacket(tile.getBlockPos()).sendToServer();
        }));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int fontColor = multiblock.isMachineOn ? -1 : 15641088;
        int middle_x = getGuiLeft() + imageWidth / 2;
        String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize(MODID + ".tooltip.distiller_controller");
        guiGraphics.drawCenteredString(this.font, title, middle_x, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawCenteredString(this.font, underline, middle_x, getGuiTop() + 12, fontColor);

        refluxBonusText.apply(guiGraphics, 22, fontColor);

        reboilingBonusText.apply(guiGraphics, 34, fontColor);

        distributionBonusText.apply(guiGraphics, 46, fontColor);

        rateText.apply(guiGraphics, 58, fontColor);

        powerText.apply(guiGraphics, 70, fontColor);
    }
}