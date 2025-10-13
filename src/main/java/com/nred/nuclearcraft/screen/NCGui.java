package com.nred.nuclearcraft.screen;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.menu.slot.SlotFiltered;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public abstract class NCGui<MENU extends AbstractContainerMenu> extends AbstractContainerScreen<MENU> {
    public static final Font FONT = Minecraft.getInstance().font;

    public NCGui(MENU menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    //	@Override TODO
//	public void initGui() {
//		super.initGui();
//		buttonList.clear();
//	}
//
//	@Override
//	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		drawDefaultBackground();
//		super.drawScreen(mouseX, mouseY, partialTicks);
//		renderHoveredToolTip(mouseX, mouseY);
//		renderTooltips(mouseX, mouseY);
//	}
//
//	@Override
//	protected void renderHoveredToolTip(int x, int y) {
//		Slot slot = getSlotUnderMouse();
//		if (slot != null && mc.player.inventory.getItemStack().isEmpty()) {
//			if (slot instanceof SlotFiltered slotFiltered && slotFiltered.hasStackForRender()) {
//				renderToolTip(slotFiltered.getStackForRender(), x, y);
//			}
//			else if (slot.getHasStack()) {
//				renderToolTip(slot.getStack(), x, y);
//			}
//		}
//	}
    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        Slot slot = getSlotUnderMouse();
        if (slot != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
            if (slot instanceof SlotFiltered slotFiltered && slotFiltered.hasStackForRender()) {
                ItemStack itemstack = slotFiltered.getStackForRender();
                guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, x, y);
            } else if (slot.hasItem()) {
                ItemStack itemstack = slot.getItem();
                guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, x, y);
            }
        }
    }
//	protected void renderTooltips(int mouseX, int mouseY) {}
//
//	@Override
//	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//		if (mouseButton == 1 || mouseButton == 2) {
//			for (int i = 0; i < buttonList.size(); ++i) {
//				GuiButton button = buttonList.get(i);
//				boolean mousePressed = button instanceof NCButton ncButton && ncButton.mousePressed(mc, mouseX, mouseY, mouseButton);
//				if (mousePressed) {
//					GuiScreenEvent.ActionPerformedEvent.Pre event = new GuiScreenEvent.ActionPerformedEvent.Pre(this, button, buttonList);
//					if (MinecraftForge.EVENT_BUS.post(event)) {
//						break;
//					}
//					button = event.getButton();
//					selectedButton = button;
//					float soundPitch = 1F;
//					if (mouseButton == 1) {
//						actionPerformedRight(button);
//						soundPitch = SoundHelper.getPitch(1D);
//					}
//					else if (mouseButton == 2) {
//						actionPerformedMiddle(button);
//					}
//					mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, soundPitch));
//					if (equals(mc.currentScreen)) {
//						MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), buttonList));
//					}
//				}
//			}
//		}
//		super.mouseClicked(mouseX, mouseY, mouseButton);
//	}
//
//	protected void actionPerformedRight(GuiButton guiButton) {}
//
//	protected void actionPerformedMiddle(GuiButton guiButton) {}
//
//	protected boolean isEscapeKeyDown(int keyCode) {
//		return keyCode == 1 || mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode);
//	}

    protected void drawTooltip(GuiGraphics guiGraphics, List<Component> text, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        int xPos = x + getGuiLeft();
        int yPos = y + getGuiTop();
        if (mouseX >= xPos && mouseY >= yPos && mouseX < xPos + tooltipWidth && mouseY < yPos + tooltipHeight) {
            guiGraphics.renderTooltip(FONT, text, Optional.empty(), mouseX, mouseY);
        }
    }

    protected void drawTooltip(GuiGraphics guiGraphics, Component text, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        drawTooltip(guiGraphics, Lists.newArrayList(text), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
    }

//	protected List<String> fluidInfo(Tank tank) {
//		String fluidName = tank.getFluidLocalizedName();
//		String fluidAmount = UnitHelper.prefix(tank.getFluidAmount(), tank.getCapacity(), 5, "B", -1);
//		return Lists.newArrayList(TextFormatting.GREEN + fluidName + TextFormatting.WHITE + " [" + fluidAmount + "]", TextFormatting.ITALIC + Lang.localize("gui.nc.container.shift_clear_tank"));
//	}
//
//	protected List<String> fluidFilterInfo(Tank tank) {
//		String fluidName = tank.getFluidLocalizedName();
//		return Lists.newArrayList(TextFormatting.GREEN + fluidName + TextFormatting.WHITE, TextFormatting.ITALIC + Lang.localize("gui.nc.container.shift_clear_filter"));
//	}
//
//	protected void drawFluidTooltip(Tank tank, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
//		if (!tank.isEmpty()) {
//			drawTooltip(fluidInfo(tank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
//		}
//	}
//
//	protected void drawFilteredFluidTooltip(Tank tank, Tank filterTank, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
//		if (tank.isEmpty()) {
//			if (!filterTank.isEmpty()) {
//				drawTooltip(fluidFilterInfo(filterTank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
//			}
//		}
//		else {
//			drawTooltip(fluidInfo(tank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
//		}
//	}
//
//	protected List<String> energyInfo(IEnergyStorage energyStorage) {
//		String energy = UnitHelper.prefix(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored(), 5, "RF");
//		return Lists.newArrayList(TextFormatting.LIGHT_PURPLE + Lang.localize("gui.nc.container.energy_stored") + TextFormatting.WHITE + " " + energy);
//	}
//
//	protected List<String> noEnergyInfo() {
//		return Lists.newArrayList(TextFormatting.RED + Lang.localize("gui.nc.container.no_energy"));
//	}
//
//	protected void drawEnergyTooltip(IEnergyStorage energyStorage, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
//		drawTooltip(energyInfo(energyStorage), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
//	}
//
//	protected void drawNoEnergyTooltip(int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
//		drawTooltip(noEnergyInfo(), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
//	}
//
//	public List<String> noClusterInfo() {
//		return Lists.newArrayList(TextFormatting.RED + Lang.localize("gui.nc.container.no_cluster"));
//	}
}