package com.nred.nuclearcraft.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@OnlyIn(Dist.CLIENT)
public abstract class NCButton extends Button {
    public final int id;

    protected NCButton(int id, int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION); // TODO add narration
        this.id = id;
    }

    @OnlyIn(Dist.CLIENT)
    public static abstract class Image extends NCButton {
        private final ResourceLocation unpressedTexture;
        private final ResourceLocation pressedTexture;
        protected int textureWidth, textureHeight;

        public Image(int id, int x, int y, ResourceLocation unpressedTexture, ResourceLocation pressedTexture, int textureWidth, int textureHeight, OnPress onPress) {
            super(id, x, y, textureWidth, textureHeight, onPress);
            this.unpressedTexture = unpressedTexture;
            this.pressedTexture = pressedTexture;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation resourcelocation = this.isActive() ? pressedTexture : unpressedTexture;
            guiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width, this.height);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Item extends NCButton {
        @Nonnull
        private final ItemStack item;

        public Item(int id, int x, int y, int width, int height, float alpha, @Nonnull ItemStack item, OnPress onPress) {
            super(id, x, y, width, height, onPress);
            this.alpha = alpha;
            this.item = item;
            this.width = width;
            this.height = height;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.renderFakeItem(item, getX(), getY());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClearTank extends NCButton {
        public ClearTank(int id, int x, int y, int width, int height, OnPress onPress) {
            super(id, x, y, width, height, onPress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class MachineConfig extends Image {
        public MachineConfig(int id, int x, int y, OnPress onPress) {
            super(id, x, y, ncLoc("button/side_config_on"), ncLoc("button/side_config_off"), 18, 18, onPress);
        }
    }
}