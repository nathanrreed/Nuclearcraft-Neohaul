package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.gui.NCToggleButton;
import com.nred.nuclearcraft.gui.SorptionConfig;
import com.nred.nuclearcraft.menu.InfoTileMenu;
import com.nred.nuclearcraft.payload.gui.ClearTankPacket;
import com.nred.nuclearcraft.payload.gui.ToggleRedstoneControlPacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.screen.GuiInfoTile;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.NCUtil;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class GuiProcessor<MENU extends InfoTileMenu<TILE, PACKET, INFO>, TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends GuiInfoTile<MENU, TILE, PACKET, INFO> {
    public GuiProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
        super(menu, inventory, title, textureLocation);

        imageWidth = info.guiWidth;
        imageHeight = info.guiHeight;
    }

    @Override
    public void init() {
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
            addWidget(new NCButton.ClearTank(i, leftPos + tankXYWH[0], topPos + tankXYWH[1], tankXYWH[2], tankXYWH[3], this::clearTankAction));
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
            addWidget(new NCButton.ClearTank(i + info.fluidInputSize, leftPos + tankXYWH[0], topPos + tankXYWH[1], tankXYWH[2], tankXYWH[3], this::clearTankAction));
        }
    }

    protected void initConfigButtons() {
        if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0) {
            addRenderableWidget(new NCButton.MachineConfig(info.getMachineConfigButtonID(), leftPos + info.machineConfigGuiX, topPos + info.machineConfigGuiY, this::configButtonActionPerformed));
        }
        if (info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0) {
            addRenderableWidget(new NCToggleButton.RedstoneControl(info.getRedstoneControlButtonID(), leftPos + info.redstoneControlGuiX, topPos + info.redstoneControlGuiY, tile, this::configButtonActionPerformed));
        }
    }

    protected void initSorptionButtons() {
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
        addRenderableWidget(function.apply(id, leftPos + slotXYWH[0] - 1, topPos + slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2, this::sorptionButtonActionPerformed));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.drawCenteredString(FONT, title, leftPos + imageWidth / 2, topPos + 6, -1); //TODO use title built in
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        drawMainBackground(guiGraphics);
        drawBars(guiGraphics);
        drawTanks(guiGraphics);
    }

    protected void drawMainBackground(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(guiTextures, 256, 256, 0, 0, leftPos, topPos, imageWidth, imageHeight);
    }

    protected void drawTanks(GuiGraphics guiGraphics) {
        List<Tank> tanks = tile.getTanks();
        for (int i = 0; i < info.fluidInputSize; ++i) {
            int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
            renderGuiTank(guiGraphics, tanks.get(i), leftPos + tankXYWH[0], topPos + tankXYWH[1], tankXYWH[2], tankXYWH[3], 1);
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
            renderGuiTank(guiGraphics, tanks.get(i + info.fluidInputSize), leftPos + tankXYWH[0], topPos + tankXYWH[1], tankXYWH[2], tankXYWH[3], 1);
        }
    }

    protected void drawBars(GuiGraphics guiGraphics) {
        drawProgressBar(guiGraphics);

        if (tile.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), null) != null) {
            drawEnergyBar(guiGraphics);
        }
    }

    protected void drawProgressBar(GuiGraphics guiGraphics) {
        guiGraphics.blitSprite(guiTextures, 256, 256, info.progressBarGuiU, info.progressBarGuiV, leftPos + info.progressBarGuiX, topPos + info.progressBarGuiY, getProgressBarWidth(), info.progressBarGuiH);
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
        int energyX = leftPos + info.energyBarGuiX, energyY = topPos + info.energyBarGuiY;
        if (info.getDefaultProcessPower() != 0) {
            int e = getEnergyBarHeight(), d = info.energyBarGuiH - e;
            guiGraphics.blitSprite(guiTextures, 256, 256, info.energyBarGuiU, info.energyBarGuiV + d, energyX, energyY + d, info.energyBarGuiW, e);
        } else {
            guiGraphics.blitSprite(guiTextures, 256, 256, energyX + info.energyBarGuiW, energyY + info.energyBarGuiH, energyX, energyY, 0xFFC6C6C6, 0xFF8B8B8B);
        }
    }

    protected int getEnergyBarHeight() {
        IEnergyStorage energyStorage = tile.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), null);
        return NCMath.toInt(Math.round((double) info.energyBarGuiH * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored()));
    }

    protected void clearTankAction(NCButton button, int pressed) {
        if (NCUtil.isModifierKeyDown()) {
            new ClearTankPacket(tile, button.id).sendToServer();
        }
    }

    protected boolean configButtonActionPerformed(NCButton button, int pressed) {
        if (button.id == info.getMachineConfigButtonID()) {

            setConfigScreen();

            return true;
        } else if (button.id == info.getRedstoneControlButtonID()) {
            tile.setRedstoneControl(!tile.getRedstoneControl());
            new ToggleRedstoneControlPacket(tile).sendToServer();
            return true;
        }
        return false;
    }

    protected void setConfigScreen() {
        Minecraft.getInstance().setScreen(new SideConfigScreen(menu, this, menu.inventory, Component.empty(), ncLoc("screen/" + info.name)));
    }

    protected boolean sorptionButtonActionPerformed(NCButton button, int pressed) {
        for (int i = 0; i < info.itemInputSize; ++i) {
            if (button.id == info.itemInputSorptionButtonID[i]) {
                Minecraft.getInstance().setScreen(new GuiItemSorptions.Input<>(this, tile, info.itemInputSlots[i]));
                return true;
            }
        }

        for (int i = 0; i < info.fluidInputSize; ++i) {
            if (button.id == info.fluidInputSorptionButtonID[i]) {
                Minecraft.getInstance().setScreen(new GuiFluidSorptions.Input<>(this, tile, info.fluidInputTanks[i]));
                return true;
            }
        }

        for (int i = 0; i < info.itemOutputSize; ++i) {
            if (button.id == info.itemOutputSorptionButtonID[i]) {
                Minecraft.getInstance().setScreen(new GuiItemSorptions.Output<>(this, tile, info.itemOutputSlots[i]));
                return true;
            }
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            if (button.id == info.fluidOutputSorptionButtonID[i]) {
                Minecraft.getInstance().setScreen(new GuiFluidSorptions.Output<>(this, tile, info.fluidOutputTanks[i]));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
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
        IEnergyStorage energyStorage = tile.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), null);
        if (energyStorage != null) {
            drawEnergyTooltip(guiGraphics, energyStorage, mouseX, mouseY, info.energyBarGuiX, info.energyBarGuiY, info.energyBarGuiW, info.energyBarGuiH);
        }
    }

    protected void renderConfigButtonTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (info.machineConfigGuiX >= 0 && info.machineConfigGuiY >= 0) {
            drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.machine_side_config"), mouseX, mouseY, info.machineConfigGuiX, info.machineConfigGuiY, 18, 18);
        }
        if (info.redstoneControlGuiX >= 0 && info.redstoneControlGuiY >= 0) {
            drawTooltip(guiGraphics, Component.translatable(MODID + ".tooltip.redstone_control"), mouseX, mouseY, info.redstoneControlGuiX, info.redstoneControlGuiY, 18, 18);
        }
    }

    protected void renderSorptionButtonTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < info.itemInputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, ChatFormatting.BLUE, MODID + ".tooltip.input_item_config", info.itemInputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.fluidInputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, ChatFormatting.DARK_AQUA, MODID + ".tooltip.input_tank_config", info.fluidInputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.itemOutputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, ChatFormatting.GOLD, MODID + ".tooltip.output_item_config", info.itemOutputGuiXYWH.get(i));
        }

        for (int i = 0; i < info.fluidOutputSize; ++i) {
            drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, ChatFormatting.RED, MODID + ".tooltip.output_tank_config", info.fluidOutputGuiXYWH.get(i));
        }
    }

    protected void drawSorptionButtonTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, ChatFormatting formatting, String unlocalized, int[] slotXYWH) {
        drawTooltip(guiGraphics, Component.translatable(unlocalized).withStyle(formatting), mouseX, mouseY, slotXYWH[0] - 1, slotXYWH[1] - 1, slotXYWH[2] + 2, slotXYWH[3] + 2);
    }

    @Override
    protected void drawEnergyTooltip(GuiGraphics guiGraphics, IEnergyStorage energyStorage, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        if (info.getDefaultProcessPower() != 0) {
            super.drawEnergyTooltip(guiGraphics, energyStorage, mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
        } else {
            drawNoEnergyTooltip(guiGraphics, mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
        }
    }

    @Override
    protected List<Component> energyInfo(IEnergyStorage energyStorage) {
        List<Component> info = super.energyInfo(energyStorage);
        info.add(Component.translatable(MODID + ".tooltip.process_power", Component.literal(UnitHelper.prefix(tile.getProcessPower(), 5, "RF/t")).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
        return info;
    }

    public class SideConfigScreen extends GuiProcessor<MENU, TILE, PACKET, INFO> {
        private final Screen parent;

        public SideConfigScreen(MENU menu, Screen parent, Inventory inventory, Component title, ResourceLocation textureLocation) {
            super(menu, inventory, title, textureLocation);
            this.parent = parent;
        }

        @Override
        public void initButtons() {
            initSorptionButtons();
        }

        @Override
        public void renderProcessorTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
            renderSorptionButtonTooltips(guiGraphics, mouseX, mouseY);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (isEscapeKeyDown(keyCode, scanCode)) {
                Minecraft.getInstance().setScreen(parent);
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }
}