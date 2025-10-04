package com.nred.nuclearcraft.screen.multiblock;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.menu.AbstractControllerMenu;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.ClearPayload;
import com.nred.nuclearcraft.util.NCUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public abstract class AbstractMultiblockScreen<MENU extends AbstractControllerMenu<?, MULTIBLOCK>, MULTIBLOCK extends Multiblock<MULTIBLOCK>> extends AbstractContainerScreen<MENU> {
    public AbstractMultiblockScreen(MENU menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.titleLabelY = Integer.MIN_VALUE;
        this.inventoryLabelY = Integer.MIN_VALUE;

        clearButton = this.addRenderableWidget(new ImageButton(base.right() - 23, getClearButtonY(), 18, 18, new WidgetSprites(ncLoc("button/void_excess_off"), ncLoc("button/void_excess_on")), button -> {
            PacketDistributor.sendToServer(new ClearPayload(menu.controller.getWorldPosition()));
        }));
        clearButton.visible = false;
    }

    protected final MULTIBLOCK multiblock = menu.controller.getMultiblockController().orElseThrow(IllegalStateException::new);
    protected ScreenRectangle base = ScreenRectangle.empty();
    protected static final Font FONT = Minecraft.getInstance().font;
    public static final Component CLEAR_ALL_INFO = Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.shift_clear_multiblock").withStyle(ChatFormatting.ITALIC);
    ImageButton clearButton;

    public int getClearButtonY() {
        return base.top() + 5;
    }

    public void resize(int width, int imageWidth, int height, int imageHeight) {
        base = new ScreenRectangle((width - imageWidth) / 2, (height - imageHeight) / 2, imageWidth, imageHeight);
        clearButton.setPosition(base.right() - 23, getClearButtonY());
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (NCUtil.isModifierKeyDown() && clearButton.isHovered())
            guiGraphics.renderTooltip(font, CLEAR_ALL_INFO, mouseX, mouseY);

        super.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        clearButton.visible = Screen.hasShiftDown();
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        clearButton.visible = Screen.hasShiftDown();
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        resize(this.width, imageWidth, this.height, imageHeight);
    }

    @Override
    protected void init() {
        super.init();

        resize(width, imageWidth, height, imageHeight);
    }
}
