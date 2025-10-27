package com.nred.nuclearcraft.screen.processor;

import com.nred.nuclearcraft.block_entity.ITileFiltered;
import com.nred.nuclearcraft.block_entity.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.gui.NCButton;
import com.nred.nuclearcraft.menu.processor.FilteredProcessorMenu;
import com.nred.nuclearcraft.payload.gui.ClearFilterTankPacket;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class GuiFilteredProcessor<MENU extends FilteredProcessorMenu<TILE, PACKET, INFO>, TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO> & ITileFiltered & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends GuiProcessor<MENU, TILE, PACKET, INFO> {
    public GuiFilteredProcessor(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
        super(menu, inventory, title, textureLocation);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        drawMainBackground(guiGraphics);
        drawBars(guiGraphics);
        drawFilters(guiGraphics);
        drawTanks(guiGraphics);
    }

    protected void drawFilters(GuiGraphics guiGraphics) {
        if (tile instanceof ITileFilteredInventory tileFilteredInventory) {
            NonNullList<ItemStack> filterStacks = tileFilteredInventory.getFilterStacks();
            for (int i = 0; i < info.itemInputSize; ++i) {
                int[] stackXY = info.itemInputStackXY.get(i);
                renderFakeItem(guiGraphics, filterStacks.get(i), leftPos + stackXY[0], topPos + stackXY[1], 0.5F);
            }

            for (int i = 0; i < info.itemOutputSize; ++i) {
                int[] stackXY = info.itemOutputStackXY.get(i);
                renderFakeItem(guiGraphics, filterStacks.get(i + info.itemInputSize), leftPos + stackXY[0], topPos + stackXY[1], 0.5F);
            }
        }

        if (tile instanceof ITileFilteredFluid tileFilteredFluid) {
            List<Tank> filterTanks = tileFilteredFluid.getFilterTanks();
            for (int i = 0; i < info.fluidInputSize; ++i) {
                Tank filterTank = filterTanks.get(i);
                if (!filterTank.isEmpty()) {
                    int[] tankXYWH = info.fluidInputGuiXYWH.get(i);
                    renderGuiTank(guiGraphics, filterTank, leftPos + tankXYWH[0], topPos + tankXYWH[1], tankXYWH[2], tankXYWH[3], 0.5f);
                }
            }

            for (int i = 0; i < info.fluidOutputSize; ++i) {
                Tank filterTank = filterTanks.get(i + info.fluidInputSize);
                if (!filterTank.isEmpty()) {
                    int[] tankXYWH = info.fluidOutputGuiXYWH.get(i);
                    renderGuiTank(guiGraphics, filterTank, leftPos + tankXYWH[0], topPos + tankXYWH[1], tankXYWH[2], tankXYWH[3], 0.5f);
                }
            }
        }
    }

    @Override
    protected void clearTankAction(NCButton button, int pressed) {
        if (NCUtil.isModifierKeyDown()) {
            if (tile instanceof ITileFilteredFluid tileFilteredFluid && tile.getTanks().get(button.id).isEmpty()) {
                new ClearFilterTankPacket(tileFilteredFluid, button.id).sendToServer();
            } else {
                super.clearTankAction(button, pressed);
            }
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