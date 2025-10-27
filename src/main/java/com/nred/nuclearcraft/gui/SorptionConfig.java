package com.nred.nuclearcraft.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@OnlyIn(Dist.CLIENT)
public abstract class SorptionConfig extends NCButton {
    protected final ResourceLocation texture;
    protected final int textureWidth, textureHeight;

    public SorptionConfig(int id, int x, int y, ResourceLocation texture, int width, int height, OnPressInfo onPress) {
        super(id, x, y, width, height, onPress);
        this.texture = texture;
        textureWidth = width;
        textureHeight = height;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(texture, this.getX(), this.getY(), this.width, this.height);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemInput extends SorptionConfig {
        public ItemInput(int id, int x, int y, int width, int height, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/item_input"), width, height, onPress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FluidInput extends SorptionConfig {
        public FluidInput(int id, int x, int y, int width, int height, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/fluid_input"), width, height, onPress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemOutput extends SorptionConfig {
        public ItemOutput(int id, int x, int y, int width, int height, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/item_output"), width, height, onPress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FluidOutput extends SorptionConfig {
        public FluidOutput(int id, int x, int y, int width, int height, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/fluid_output"), width, height, onPress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class SpeedUpgrade extends SorptionConfig {
        public SpeedUpgrade(int id, int x, int y, int width, int height, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/speed_upgrade"), width, height, onPress);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class EnergyUpgrade extends SorptionConfig {
        public EnergyUpgrade(int id, int x, int y, int width, int height, OnPressInfo onPress) {
            super(id, x, y, ncLoc("button/energy_upgrade"), width, height, onPress);
        }
    }
}