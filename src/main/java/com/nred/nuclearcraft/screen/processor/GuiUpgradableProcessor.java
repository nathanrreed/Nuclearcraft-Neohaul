package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.UpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.gui.SorptionConfig;
import com.nred.nuclearcraft.menu.InfoTileMenu;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public abstract class GuiUpgradableProcessor<MENU extends InfoTileMenu<TILE, PACKET, INFO>, TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends UpgradableProcessorMenuInfo<TILE, PACKET, INFO>> extends GuiProcessor<MENU, TILE, PACKET, INFO> {
    public GuiUpgradableProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
        super(menu, inventory, title, textureLocation);
    }

    @Override
    protected void initSorptionButtons() {
        super.initSorptionButtons();
        addSorptionButton(SorptionConfig.SpeedUpgrade::new, info.speedUpgradeSorptionButtonID, info.speedUpgradeGuiXYWH);
        addSorptionButton(SorptionConfig.EnergyUpgrade::new, info.energyUpgradeSorptionButtonID, info.energyUpgradeGuiXYWH);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(guiGraphics, partialTicks, mouseX, mouseY);
        drawUpgradeRenderers(guiGraphics);
    }

    @Override
    protected void setConfigScreen() {
        Minecraft.getInstance().setScreen(new SideConfigScreen(menu, this, menu.inventory, Component.empty(), ncLoc("screen/" + info.name)));
    }

    protected void drawUpgradeRenderers(GuiGraphics guiGraphics) {
        int[] stackXY = info.speedUpgradeStackXY;
        renderFakeItem(guiGraphics, UPGRADE_MAP.get("speed").toStack(), leftPos + stackXY[0], topPos + stackXY[1], 0.5F);

        stackXY = info.energyUpgradeStackXY;
        renderFakeItem(guiGraphics, UPGRADE_MAP.get("energy").toStack(), leftPos + stackXY[0], topPos + stackXY[1], 0.5F);
    }

    @Override
    protected boolean sorptionButtonActionPerformed(NCButton button, int pressed) {
        if (super.sorptionButtonActionPerformed(button, pressed)) {
            return true;
        } else if (button.id == info.speedUpgradeSorptionButtonID) {
            Minecraft.getInstance().setScreen(new GuiItemSorptions.SpeedUpgrade<>(this, tile, info.speedUpgradeSlot));
            return true;
        } else if (button.id == info.energyUpgradeSorptionButtonID) {
            Minecraft.getInstance().setScreen(new GuiItemSorptions.EnergyUpgrade<>(this, tile, info.energyUpgradeSlot));
            return true;
        }
        return false;
    }

    @Override
    protected void renderSorptionButtonTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderSorptionButtonTooltips(guiGraphics, mouseX, mouseY);

        drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, ChatFormatting.DARK_BLUE, MODID + ".tooltip.speed_upgrade_config", info.speedUpgradeGuiXYWH);
        drawSorptionButtonTooltip(guiGraphics, mouseX, mouseY, ChatFormatting.YELLOW, MODID + ".tooltip.energy_upgrade_config", info.energyUpgradeGuiXYWH);
    }

    @Override
    protected List<Component> energyInfo(IEnergyStorage energyStorage) {
        List<Component> info = super.energyInfo(energyStorage);
        info.add(multiplierInfo(MODID + ".tooltip.speed_multiplier", tile.getSpeedMultiplier()));
        info.add(multiplierInfo(MODID + ".tooltip.power_multiplier", tile.getPowerMultiplier()));
        return info;
    }

    protected Component multiplierInfo(String unloc, double mult) {
        return Component.translatable(unloc, Component.literal(" x" + NCMath.decimalPlaces(mult, 2)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA);
    }

    public class SideConfigScreen extends GuiUpgradableProcessor<MENU, TILE, PACKET, INFO> {
        private final Screen parent;

        public SideConfigScreen(MENU menu, Screen parent, Inventory inventory, Component title, ResourceLocation resourceLocation) {
            super(menu, inventory, title, resourceLocation);
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
        protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
            drawMainBackground(guiGraphics);
            drawBars(guiGraphics);
            drawTanks(guiGraphics);
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
