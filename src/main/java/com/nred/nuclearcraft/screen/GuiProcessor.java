package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.block_entity.info.ProcessorContainerInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block.processor.IProcessor;
import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.gui.NCToggleButton;
import com.nred.nuclearcraft.gui.SorptionButtonFunction;
import com.nred.nuclearcraft.gui.SorptionConfig;
import com.nred.nuclearcraft.menu.ContainerInfoTile;
import com.nred.nuclearcraft.payload.gui.ClearTankPacket;
import com.nred.nuclearcraft.payload.gui.OpenSideConfigGuiPacket;
import com.nred.nuclearcraft.payload.gui.ToggleRedstoneControlPacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.NCUtil;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class GuiProcessor<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, MENU extends ContainerInfoTile<TILE, PACKET, INFO>> extends GuiInfoTile<TILE, PACKET, INFO, MENU> {
    protected final ResourceLocation gui_texture;

    public GuiProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation gui_texture) {
        super(menu, inventory, title);
        this.imageWidth = info.guiWidth;
        this.imageHeight = info.guiHeight;
        this.gui_texture = gui_texture;
    }

    @Override
    protected void init() {
        super.init();
        initButtons();
    }

    protected void initButtons() {
        initTankButtons();
        initConfigButtons();
    }

    protected void initTankButtons() {
        for (int i = 0; i < info.fluidInputSize; ++i) {
            int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
            addWidget(new NCButton.ClearTank(i, getGuiLeft() + tankXYWH[0], getGuiTop() + tankXYWH[1], tankXYWH[2], tankXYWH[3], btn -> clearTankAction((NCButton) btn)));
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
            addWidget(new NCButton.ClearTank(i + info.fluidInputSize, getGuiLeft() + tankXYWH[0], getGuiTop() + tankXYWH[1], tankXYWH[2], tankXYWH[3], btn -> clearTankAction((NCButton) btn)));
        }
    }

    protected void initConfigButtons() {
        if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0) {
            addWidget(new NCButton.MachineConfig(info.getMachineConfigButtonID(), getGuiLeft() + info.machineConfigGuiX, getGuiTop() + info.machineConfigGuiY, btn -> configButtonActionPerformed((NCButton) btn)));
        }
        if (info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0) {
            addWidget(new NCToggleButton.RedstoneControl(info.getRedstoneControlButtonID(), getGuiLeft() + info.redstoneControlGuiX, getGuiTop() + info.redstoneControlGuiY, tile, btn -> configButtonActionPerformed((NCButton) btn)));
        }
    }

    protected void initSorptionButtons() { // TODO
        for (int i = 0; i < info.itemInputSize; ++i) {
            addSorptionButton(SorptionConfig.ItemInput::new, info.itemInputSorptionButtonID[i], info.itemInputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.fluidInputSize; ++i) {
            addSorptionButton(SorptionConfig.FluidInput::new, info.fluidInputSorptionButtonID[i], info.fluidInputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.itemOutputSize; ++i) {
            addSorptionButton(SorptionConfig.ItemOutput::new, info.itemOutputSorptionButtonID[i], info.itemOutputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            addSorptionButton(SorptionConfig.FluidOutput::new, info.fluidOutputSorptionButtonID[i], info.fluidOutputGuiXYWH.get(i));
        }
    }

    protected void addSorptionButton(SorptionButtonFunction function, int id, int[] slotXYWH) {
        addWidget(function.apply(id, getGuiLeft() + slotXYWH[0] - 1, getGuiTop() + slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2, btn -> sorptionButtonActionPerformed((NCButton) btn)));
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        drawMainBackground(guiGraphics);
        drawBars(guiGraphics);
        drawTanks(guiGraphics);
//        guiGraphics.drawCenteredString(FONT, guiName.get(), xSize / 2 - nameWidth.get() / 2, 6, 4210752); TODO
    }

    protected void drawMainBackground(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(gui_texture, 256, 256, 0, 0, getGuiLeft(), getGuiTop(), imageWidth, imageHeight);
    }

    protected void drawTanks(GuiGraphics guiGraphics) {
        List<Tank> tanks = tile.getTanks();
        for (int i = 0; i < info.fluidInputSize; ++i) {
            int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
            renderGuiTank(guiGraphics, tanks.get(i), getGuiLeft() + tankXYWH[0], getGuiTop() + tankXYWH[1], tankXYWH[2], tankXYWH[3]);
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
            renderGuiTank(guiGraphics, tanks.get(i + info.fluidInputSize), getGuiLeft() + tankXYWH[0], getGuiTop() + tankXYWH[1], tankXYWH[2], tankXYWH[3]);
        }
    }

    protected void drawBars(GuiGraphics guiGraphics) {
        drawProgressBar(guiGraphics);

        if (tile.getTileWorld().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), null) != null) {
            drawEnergyBar(guiGraphics);
        }
    }

    protected void drawProgressBar(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(gui_texture, 256, 256, getGuiLeft() + info.progressBarGuiX, getGuiTop() + info.progressBarGuiY, info.progressBarGuiU, info.progressBarGuiV, getProgressBarWidth(), info.progressBarGuiH);
    }

    protected int getProgressBarWidth() {
        double baseProcessTime = tile.getBaseProcessTime();
        if (baseProcessTime / tile.getSpeedMultiplier() < 2D) {
            return tile.getIsProcessing() ? info.progressBarGuiW : 0;
        } else {
            return baseProcessTime == 0D ? 0 : NCMath.toInt(Math.round(tile.getCurrentTime() * info.progressBarGuiW / baseProcessTime));
        }
    }

    protected void drawEnergyBar(GuiGraphics guiGraphics) {
        int energyX = getGuiLeft() + info.energyBarGuiX, energyY = getGuiTop() + info.energyBarGuiY;
        if (info.defaultProcessPower != 0) {
            int e = getEnergyBarHeight(), d = info.energyBarGuiH - e;
            guiGraphics.blitSprite(gui_texture, 256, 256, energyX, energyY + d, info.energyBarGuiU, info.energyBarGuiV + d, info.energyBarGuiW, e);
        } else {
            guiGraphics.fillGradient(energyX, energyY, energyX + info.energyBarGuiW, energyY + info.energyBarGuiH, 0xFFC6C6C6, 0xFF8B8B8B);
        }
    }

    protected int getEnergyBarHeight() {
        IEnergyStorage energyStorage = tile.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), null);
        return NCMath.toInt(Math.round((double) info.energyBarGuiH * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored()));
    }

    final void clearTankAction(NCButton button) {
        if (NCUtil.isModifierKeyDown()) {
            if (button.id >= 0 && button.id < info.getTankCount()) {
                clearTankAction(button.id);
            }
        }
    }

    protected void clearTankAction(int tankNumber) {
        new ClearTankPacket(tile, tankNumber).sendToServer();
    }

    protected void configButtonActionPerformed(NCButton button) {
        if (button.id == info.getMachineConfigButtonID()) {
            new OpenSideConfigGuiPacket(tile).sendToServer();
        } else if (button.id == info.getRedstoneControlButtonID()) {
            tile.setRedstoneControl(!tile.getRedstoneControl());
            new ToggleRedstoneControlPacket(tile).sendToServer();
        }
    }

    protected boolean sorptionButtonActionPerformed(NCButton button) {
        for (int i = 0; i < info.itemInputSize; ++i) {
            if (button.id == info.itemInputSorptionButtonID[i]) {
//                menu.inventory.player.openMenu() TODO
//                FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Input<>(this, tile, info.itemInputSlots[i]));
                return true;
            }
        }

        for (int i = 0; i < info.fluidInputSize; ++i) {
            if (button.id == info.fluidInputSorptionButtonID[i]) {
//                FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Input<>(this, tile, info.fluidInputTanks[i]));
                return true;
            }
        }

        for (int i = 0; i < info.itemOutputSize; ++i) {
            if (button.id == info.itemOutputSorptionButtonID[i]) {
//                FMLCommonHandler.instance().showGuiScreen(new GuiItemSorptions.Output<>(this, tile, info.itemOutputSlots[i]));
                return true;
            }
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            if (button.id == info.fluidOutputSorptionButtonID[i]) {
//                FMLCommonHandler.instance().showGuiScreen(new GuiFluidSorptions.Output<>(this, tile, info.fluidOutputTanks[i]));
                return true;
            }
        }

        return false;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        renderProcessorTooltips(guiGraphics, x, y);
    }

    protected void renderProcessorTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        renderTankTooltips(guiGraphics, mouseX, mouseY);
        renderBarTooltips(guiGraphics, mouseX, mouseY);
        renderConfigButtonTooltips(guiGraphics, mouseX, mouseY);
    }

    protected void renderTankTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        List<Tank> tanks = tile.getTanks();
        for (int i = 0; i < info.fluidInputSize; ++i) {
            int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
            drawFluidTooltip(guiGraphics, tanks.get(i), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
            drawFluidTooltip(guiGraphics, tanks.get(i + info.fluidInputSize), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
        }
    }

    protected void renderBarTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        IEnergyStorage energyStorage = tile.getTileWorld().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), null);
        if (energyStorage != null) {
            drawEnergyTooltip(guiGraphics, energyStorage, mouseX, mouseY, info.energyBarGuiX, info.energyBarGuiY, info.energyBarGuiW, info.energyBarGuiH);
        }
    }

    protected void renderConfigButtonTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0) {
            drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.side_config"), mouseX, mouseY, info.machineConfigGuiX, info.machineConfigGuiY, 18, 18);
        }
        if (info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0) {
            drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.redstone_control"), mouseX, mouseY, info.redstoneControlGuiX, info.redstoneControlGuiY, 18, 18);
        }
    }

    protected void renderSorptionButtonTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) { // TODO
        for (int i = 0; i < info.itemInputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, Component.translatable(MODID + ".tooltip.side_config.item_input").withStyle(ChatFormatting.BLUE), info.itemInputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.fluidInputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, Component.translatable(MODID + ".tooltip.side_config.fluid_input").withStyle(ChatFormatting.DARK_AQUA), info.fluidInputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.itemOutputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, Component.translatable(MODID + ".tooltip.side_config.item_output").withStyle(ChatFormatting.GOLD), info.itemOutputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, Component.translatable(MODID + ".tooltip.side_config.fluid_output").withStyle(ChatFormatting.RED), info.fluidOutputGuiXYWH.get(i));
        }
    }

    protected void drawSorptionButtonTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, Component component, int[] slotXYWH) {
        drawTooltip(guiGraphics, component, mouseX, mouseY, slotXYWH[0] - 1, slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2);
    }

    @Override
    protected void drawEnergyTooltip(GuiGraphics guiGraphics, IEnergyStorage energyStorage, int mouseX, int mouseY, int x, int y, int drawWidth, int drawHeight) {
        if (info.defaultProcessPower != 0) {
            super.drawEnergyTooltip(guiGraphics, energyStorage, mouseX, mouseY, x, y, drawWidth, drawHeight);
        } else {
            drawNoEnergyTooltip(guiGraphics, mouseX, mouseY, x, y, drawWidth, drawHeight);
        }
    }

    @Override
    protected List<Component> energyInfo(IEnergyStorage energyStorage) {
        List<Component> info = super.energyInfo(energyStorage);
        info.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(tile.getProcessPower(), 5, "RF/t"))).withStyle(ChatFormatting.LIGHT_PURPLE));
        return info;
    }

//  TODO  public static class SideConfig<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends GuiProcessor<TILE, PACKET, INFO> {
//        public SideConfig(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
//            super(inventory, player, tile, textureLocation);
//        }
//
//        @Override
//        public void initButtons() {
//            initSorptionButtons();
//        }
//
////        @Override
////        protected boolean buttonActionPerformed(Button button) {
////            return sorptionButtonActionPerformed(button);
////        }
//
//        @Override
//        public void renderProcessorTooltips(int mouseX, int mouseY) {
//            renderSorptionButtonTooltips(mouseX, mouseY);
//        }
//
//        @Override
//        protected void keyTyped(char typedChar, int keyCode) throws IOException {
//            if (isEscapeKeyDown(keyCode)) {
//                new OpenTileGuiPacket(tile).sendToServer();
//            } else {
//                super.keyTyped(typedChar, keyCode);
//            }
//        }
//    }
}