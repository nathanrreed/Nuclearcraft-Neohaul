package com.nred.nuclearcraft.gui;

import com.nred.nuclearcraft.block_entity.ITile;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@OnlyIn(Dist.CLIENT)
public abstract class NCToggleButton extends NCButton {
    boolean isButtonPressed;

    public NCToggleButton(int id, int x, int y, int width, int height, boolean pressed, OnPressInfo onPress) {
        super(id, x, y, width, height, onPress);
        isButtonPressed = pressed;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        super.onClick(mouseX, mouseY, button);
        isButtonPressed = !isButtonPressed;
    }

    @OnlyIn(Dist.CLIENT)
    public static abstract class Image extends NCToggleButton {
        public final ResourceLocation unpressedTexture;
        public final ResourceLocation pressedTexture;
        protected int textureWidth, textureHeight;

        public Image(int id, int x, int y, ResourceLocation unpressedTexture, ResourceLocation pressedTexture, int textureWidth, int textureHeight, boolean pressed, OnPressInfo onPress) {
            super(id, x, y, textureWidth, textureHeight, pressed, onPress);
            this.unpressedTexture = unpressedTexture;
            this.pressedTexture = pressedTexture;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation resourcelocation = this.isButtonPressed ? pressedTexture : unpressedTexture;
            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class RedstoneControl extends Image {
        public RedstoneControl(int id, int x, int y, ITile machine, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/redstone_control_off"), ncLoc("button/redstone_control_on"), 18, 18, machine.getRedstoneControl(), onPress);
        }
    }
}