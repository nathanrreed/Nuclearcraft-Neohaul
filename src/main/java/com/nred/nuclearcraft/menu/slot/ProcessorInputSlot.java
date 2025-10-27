package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.screen.processor.GuiFluidSorptions;
import com.nred.nuclearcraft.screen.processor.GuiItemSorptions;
import com.nred.nuclearcraft.screen.processor.GuiProcessor;
import com.nred.nuclearcraft.screen.processor.GuiUpgradableProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ProcessorInputSlot extends Slot {
    protected final BasicRecipeHandler<?> recipeHandler;

    public ProcessorInputSlot(Container tile, BasicRecipeHandler<?> recipeHandler, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.recipeHandler = recipeHandler;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return recipeHandler.isValidItemInput(stack);
    }

    @Override
    public boolean isActive() {
        return !(Minecraft.getInstance().screen instanceof GuiProcessor.SideConfigScreen || Minecraft.getInstance().screen instanceof GuiUpgradableProcessor.SideConfigScreen || Minecraft.getInstance().screen instanceof GuiItemSorptions<?,?,?> || Minecraft.getInstance().screen instanceof GuiFluidSorptions<?,?,?>);
    }
}