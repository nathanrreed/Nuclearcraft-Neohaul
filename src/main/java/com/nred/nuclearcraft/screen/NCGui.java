package com.nred.nuclearcraft.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.menu.processor.ProcessorMenu;
import com.nred.nuclearcraft.menu.slot.FilteredSlot;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.GuiHelper.blitTile;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;

public abstract class NCGui<MENU extends AbstractContainerMenu> extends AbstractContainerScreen<MENU> {
    public static final Font FONT = Minecraft.getInstance().font;

    public NCGui(MENU menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        inventoryLabelY = Integer.MIN_VALUE;
        titleLabelY = Integer.MIN_VALUE;
    }

    @Override
    public void init() {
        super.init();
        clearWidgets();
    }
//
//	@Override
//	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		drawDefaultBackground();
//		super.drawScreen(mouseX, mouseY, partialTicks);
//		renderHoveredToolTip(mouseX, mouseY);
//		renderTooltips(mouseX, mouseY);
//	}

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        drawGuiContainerBackgroundLayer(guiGraphics, partialTick, mouseX, mouseY);
        drawGuiContainerForegroundLayer(guiGraphics, partialTick, mouseX, mouseY);
    }

    //TODO RENAME
    protected void drawGuiContainerForegroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
    }

    public void renderFakeItem(GuiGraphics guiGraphics, @Nonnull ItemStack stack, int x, int y, float alpha) {
        guiGraphics.setColor(1, 1, 1, alpha);
        guiGraphics.renderFakeItem(stack, x, y);
        guiGraphics.setColor(1, 1, 1, 1);
    }

    public void renderGuiTank(GuiGraphics guiGraphics, Tank tank, int x, int y, int w, int h, float alpha) {
        if (!tank.isEmpty()) {
            guiGraphics.setColor(1, 1, 1, alpha);
            blitTile(guiGraphics, Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(tank.getFluid().getFluid()).getStillTexture()), x, y, w, h, 16, 16, IClientFluidTypeExtensions.of(tank.getFluid().getFluid()).getTintColor());
            guiGraphics.setColor(1, 1, 1, 1);
        }
    }

    private static final ResourceLocation back = ncLoc("block/processor/back");
    private static final ResourceLocation side = ncLoc("block/processor/side");
    private static final ResourceLocation top = ncLoc("block/processor/top");
    private static final ResourceLocation bottom = ncLoc("block/processor/bottom");

    public void renderGuiBlock(GuiGraphics guiGraphics, BlockState state, Direction facing, int x, int y, int w, int h) {
        ResourceLocation texture = switch (facing) {
            case UP -> top;
            case DOWN -> bottom;
            case NORTH, SOUTH -> side;
            case WEST->ncLoc("block/processor/" + ((ProcessorMenu<?, ?, ?>) menu).info.name + "_front" + (state.getValue(ACTIVE) ? "_on" : "_off"));
            case EAST -> back;
        };

        guiGraphics.blit(leftPos + x, topPos + y, 1, w, h, Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture), 1f, 1f, 1f,1f);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        Slot slot = getSlotUnderMouse();
        if (slot != null && this.menu.getCarried().isEmpty() && this.hoveredSlot != null) {
            if (slot instanceof FilteredSlot filteredSlot && filteredSlot.hasStackForRender()) {
                ItemStack itemstack = filteredSlot.getStackForRender();
                guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, x, y);
            } else if (slot.hasItem()) {
                ItemStack itemstack = slot.getItem();
                guiGraphics.renderTooltip(this.font, this.getTooltipFromContainerItem(itemstack), itemstack.getTooltipImage(), itemstack, x, y);
            }
        }
    }

    protected boolean isEscapeKeyDown(int keyCode, int scanCode) {
        return scanCode == 1 || this.minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode));
    }

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

    protected List<Component> fluidInfo(Tank tank) {
        Component fluidName = tank.getFluidName();
        String fluidAmount = UnitHelper.prefix(tank.getFluidAmount(), tank.getCapacity(), 5, "B", -1);
        return Lists.newArrayList(fluidName.copy().withStyle(ChatFormatting.GREEN).append(" [" + fluidAmount + "]"), Component.translatable(MODID + ".tooltip.tank.clear").withStyle(ChatFormatting.ITALIC));
    }

    protected List<Component> fluidFilterInfo(Tank tank) {
        Component fluidName = tank.getFluidName();
        return Lists.newArrayList(fluidName.copy().withStyle(ChatFormatting.GREEN), Component.translatable(MODID + ".tooltip.tank.clear").withStyle(ChatFormatting.ITALIC));
    }

    protected void drawFluidTooltip(GuiGraphics guiGraphics, Tank tank, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        if (!tank.isEmpty()) {
            drawTooltip(guiGraphics, fluidInfo(tank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
        }
    }

    protected void drawFilteredFluidTooltip(GuiGraphics guiGraphics, Tank tank, Tank filterTank, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        if (tank.isEmpty()) {
            if (!filterTank.isEmpty()) {
                drawTooltip(guiGraphics, fluidFilterInfo(filterTank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
            }
        } else {
            drawTooltip(guiGraphics, fluidInfo(tank), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight + 1);
        }
    }

    protected List<Component> energyInfo(IEnergyStorage energyStorage) {
        String energy = UnitHelper.prefix(energyStorage.getEnergyStored(), energyStorage.getMaxEnergyStored(), 5, "RF");
        return Lists.newArrayList(Component.translatable(MODID + ".tooltip.energy_stored", Component.literal(energy).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE));
    }

    protected List<Component> noEnergyInfo() {
        return Lists.newArrayList(Component.translatable(MODID + ".tooltip.no_energy").withStyle(ChatFormatting.RED));
    }

    protected void drawEnergyTooltip(GuiGraphics guiGraphics, IEnergyStorage energyStorage, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        drawTooltip(guiGraphics, energyInfo(energyStorage), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
    }

    protected void drawNoEnergyTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, int x, int y, int tooltipWidth, int tooltipHeight) {
        drawTooltip(guiGraphics, noEnergyInfo(), mouseX, mouseY, x, y, tooltipWidth, tooltipHeight);
    }

    public List<Component> noClusterInfo() {
        return Lists.newArrayList(Component.translatable(MODID + ".menu.fission.no_cluster").withStyle(ChatFormatting.RED));
    }
}