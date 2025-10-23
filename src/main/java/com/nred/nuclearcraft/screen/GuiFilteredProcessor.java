package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.block_entity.ITileFiltered;
import com.nred.nuclearcraft.block_entity.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorContainerInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.menu.ContainerInfoTile;
import com.nred.nuclearcraft.payload.gui.ClearFilterTankPacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class GuiFilteredProcessor<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO> & ITileFiltered, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, MENU extends ContainerInfoTile<TILE, PACKET, INFO>> extends GuiProcessor<TILE, PACKET, INFO, MENU> {
    public GuiFilteredProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation gui_texture) {
        super(menu, inventory, title, gui_texture);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        drawMainBackground(guiGraphics);
        drawBars(guiGraphics);
        drawFilters(guiGraphics);
        drawTanks(guiGraphics);
    }

    protected void drawFilters(GuiGraphics guiGraphics) {
        if (tile instanceof ITileFilteredInventory tileFilteredInventory) {
            NonNullList<ItemStack> filterStacks = tileFilteredInventory.getFilterStacks();
            guiGraphics.setColor(1, 1, 1, 0.5F);
            for (int i = 0; i < info.itemInputSize; ++i) {
                int[] stackXY = info.itemInputStackXY.get(i);
                guiGraphics.renderFakeItem(filterStacks.get(i), getGuiLeft() + stackXY[0], getGuiTop() + stackXY[1]);
            }

            for (int i = 0; i < info.itemOutputSize; ++i) {
                int[] stackXY = info.itemOutputStackXY.get(i);
                guiGraphics.renderFakeItem(filterStacks.get(i + info.itemInputSize), getGuiLeft() + stackXY[0], getGuiTop() + stackXY[1]);
            }
            guiGraphics.setColor(1, 1, 1, 1);
        }

        if (tile instanceof ITileFilteredFluid tileFilteredFluid) {
            List<Tank> filterTanks = tileFilteredFluid.getFilterTanks();
            guiGraphics.setColor(1, 1, 1, 0.5f);
            for (int i = 0; i < info.fluidInputSize; ++i) {
                Tank filterTank = filterTanks.get(i);
                if (!filterTank.isEmpty()) {
                    int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
                    renderGuiTank(guiGraphics, filterTank, getGuiLeft() + tankXYWH[0], getGuiTop() + tankXYWH[1], tankXYWH[2], tankXYWH[3]);
                }
            }

            for (int i = 0; i < info.fluidOutputSize; ++i) {
                Tank filterTank = filterTanks.get(i + info.fluidInputSize);
                if (!filterTank.isEmpty()) {
                    int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
                    renderGuiTank(guiGraphics, filterTank, getGuiLeft() + tankXYWH[0], getGuiTop() + tankXYWH[1], tankXYWH[2], tankXYWH[3]);
                }
            }
            guiGraphics.setColor(1, 1, 1, 1);
        }
    }

    @Override
    protected void clearTankAction(int tankNumber) {
        if (tile instanceof ITileFilteredFluid tileFilteredFluid && tile.getTanks().get(tankNumber).isEmpty()) {
            new ClearFilterTankPacket(tileFilteredFluid, tankNumber).sendToServer();
        } else {
            super.clearTankAction(tankNumber);
        }
    }

    @Override
    protected void renderTankTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (tile instanceof ITileFilteredFluid tileFilteredFluid) {
            List<Tank> tanks = tile.getTanks(), filterTanks = tileFilteredFluid.getFilterTanks();
            for (int i = 0; i < info.fluidInputSize; ++i) {
                int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
                drawFilteredFluidTooltip(guiGraphics, tanks.get(i), filterTanks.get(i), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
            }

            for (int i = 0; i < info.fluidOutputSize; ++i) {
                int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
                drawFilteredFluidTooltip(guiGraphics, tanks.get(i + info.fluidInputSize), filterTanks.get(i + info.fluidInputSize), mouseX, mouseY, tankXYWH[0], tankXYWH[1], tankXYWH[2], tankXYWH[3]);
            }
        } else {
            super.renderTankTooltips(guiGraphics, mouseX, mouseY);
        }
    }
}