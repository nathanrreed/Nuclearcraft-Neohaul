package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.block_entity.machine.DecayPoolContainerEntity;
import com.nred.nuclearcraft.block_entity.machine.DecayPoolControllerEntity;
import com.nred.nuclearcraft.gui.MultiblockButton;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.menu.multiblock.controller.DecayPoolControllerMenu;
import com.nred.nuclearcraft.multiblock.machine.DecayPoolLogic;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;
import com.nred.nuclearcraft.payload.multiblock.ClearAllMaterialPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
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

public class DecayPoolControllerScreen extends LogicMultiblockControllerScreen<Machine, MachineLogic, MachineUpdatePacket, DecayPoolControllerEntity, BlockEntityMenuInfo<DecayPoolControllerEntity>, DecayPoolLogic, DecayPoolControllerMenu> {
    protected static final ResourceLocation gui_texture = ncLoc("screen/" + "decay_pool_controller");

    StringCenteringOperator containerCountText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.decay_pool_controller.containers", getLogic().getPartCount(DecayPoolContainerEntity.class)));
    StringCenteringOperator heatingRateText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.decay_pool_controller.heating_rate", UnitHelper.prefix(multiblock.baseSpeedMultiplier, 5, "H/t")));
    StringCenteringOperator totalDecayRateText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.decay_pool_controller.total_decay_rate", UnitHelper.prefix(getLogic().totalDecayRate, 5, "R/t")));
    StringCenteringOperator rateText = centeredTracker(() -> Component.translatable(MODID + ".tooltip.machine_controller.rate", multiblock.recipeUnitInfo.getString(multiblock.readyToProcess ? logic.getProcessTimeFP() : null, 5)));


    public DecayPoolControllerScreen(DecayPoolControllerMenu menu, Inventory playerInventory, Component title) {
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
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        int fontColor = multiblock.isMachineOn ? -1 : 15641088;
        int middle_x = getGuiLeft() + imageWidth / 2;
        String title = multiblock.getInteriorLengthX() + "*" + multiblock.getInteriorLengthY() + "*" + multiblock.getInteriorLengthZ() + " " + Lang.localize(MODID + ".tooltip.decay_pool_controller");
        guiGraphics.drawCenteredString(this.font, title, middle_x, getGuiTop() + 6, fontColor);

        String underline = StringHelper.charLine('-', Mth.ceil((double) this.font.width(title) / this.font.width("-")));
        guiGraphics.drawCenteredString(this.font, underline, middle_x, getGuiTop() + 12, fontColor);

        containerCountText.apply(guiGraphics, 22, fontColor);

        heatingRateText.apply(guiGraphics, 34, fontColor);

        totalDecayRateText.apply(guiGraphics, 46, fontColor);

        rateText.apply(guiGraphics, 58, fontColor);
    }
}