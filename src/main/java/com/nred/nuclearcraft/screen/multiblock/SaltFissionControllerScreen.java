package com.nred.nuclearcraft.screen.multiblock;

import com.nred.nuclearcraft.menu.multiblock.SaltFissionControllerMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class SaltFissionControllerScreen extends AbstractMultiblockScreen<SaltFissionControllerMenu, FissionReactor> {
    private static final ResourceLocation BASE = ncLoc("screen/" + "salt_fission_controller");

    public SaltFissionControllerScreen(SaltFissionControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blitSprite(BASE, 256, 256, 0, 0, base.left(), base.top(), imageWidth, imageHeight);

    }
}