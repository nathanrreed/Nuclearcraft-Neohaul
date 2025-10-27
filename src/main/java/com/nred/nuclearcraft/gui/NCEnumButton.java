package com.nred.nuclearcraft.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class NCEnumButton<T extends Enum<T>> extends NCButton {
    protected T value;

    public NCEnumButton(int id, int x, int y, int width, int height, T startingValue, OnPressInfo onPress) {
        super(id, x, y, width, height, onPress);
        value = startingValue;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        toggle(button);
        super.onClick(mouseX, mouseY, button);
    }

    public abstract void toggle(int mouseButton);

    @OnlyIn(Dist.CLIENT)
    public static abstract class Image<T extends Enum<T> & IGuiButton> extends NCEnumButton<T> {
        protected int textureWidth, textureHeight;

        public Image(int id, int x, int y, T startingValue, OnPressInfo onPress) {
            super(id, x, y, startingValue.getTextureWidth(), startingValue.getTextureHeight(), startingValue, onPress);
            this.textureWidth = startingValue.getTextureWidth();
            this.textureHeight = startingValue.getTextureHeight();
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.blitSprite(value.getTexture(isHovered()), getX(), getY(), value.getTextureWidth(), value.getTextureHeight());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemSorption extends Image<com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption> {
        private final com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption.Type sorptionType;

        public ItemSorption(int id, int x, int y, com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption startingValue, com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption.Type sorptionType, OnPressInfo onPress) {
            super(id, x, y, startingValue, onPress);
            this.sorptionType = sorptionType;
        }

        @Override
        public void toggle(int mouseButton) {
            value = value.next(sorptionType, mouseButton == 1);
        }

        public void set(com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption sorption) {
            value = sorption;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class TankSorption extends Image<com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption> {
        private final com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption.Type sorptionType;

        public TankSorption(int id, int x, int y, com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption startingValue, com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption.Type sorptionType, OnPressInfo onPress) {
            super(id, x, y, startingValue, onPress);
            this.sorptionType = sorptionType;
        }

        @Override
        public void toggle(int mouseButton) {
            value = value.next(sorptionType, mouseButton == 1);
        }

        public void set(com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption sorption) {
            value = sorption;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemOutputSetting extends Image<com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting> {
        public ItemOutputSetting(int id, int x, int y, com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting startingValue, OnPressInfo onPress) {
            super(id, x, y, startingValue, onPress);
        }

        @Override
        public void toggle(int mouseButton) {
            value = value.next(mouseButton == 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class TankOutputSetting extends Image<com.nred.nuclearcraft.block_entity.internal.fluid.TankOutputSetting> {
        public TankOutputSetting(int id, int x, int y, com.nred.nuclearcraft.block_entity.internal.fluid.TankOutputSetting startingValue, OnPressInfo onPress) {
            super(id, x, y, startingValue, onPress);
        }

        @Override
        public void toggle(int mouseButton) {
            value = value.next(mouseButton == 1);
        }
    }
}