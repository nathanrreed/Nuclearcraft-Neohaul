package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotProcessorInput extends Slot {
    protected final BasicRecipeHandler recipeHandler;

    public SlotProcessorInput(Inventory tile, BasicRecipeHandler<?> recipeHandler, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.recipeHandler = recipeHandler;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return recipeHandler.isValidItemInput(stack);
    }
}
