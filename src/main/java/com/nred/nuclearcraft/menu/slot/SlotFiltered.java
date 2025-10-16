package com.nred.nuclearcraft.menu.slot;

import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotFiltered extends Slot {
    protected final int slotIndex;
    protected final ITileFilteredInventory tile;

    public SlotFiltered(ITileFilteredInventory tile, int index, int x, int y) {
        super(tile, index, x, y);
        slotIndex = index;
        this.tile = tile;
    }

    public ItemStack getFilterStack() {
        return tile.getFilterStacks().get(slotIndex);
    }

    public void setFilterStack(ItemStack stack) {
        tile.getFilterStacks().set(slotIndex, stack);
        setChanged();
        tile.onFilterChanged(slotIndex);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        boolean itemValidRaw = isItemValidRaw(stack);

        if (tile.canModifyFilter(slotIndex)) {
            if (!itemValidRaw) {
                setFilterStack(ItemStack.EMPTY);
                return false;
            } else if (!ItemStack.isSameItem(stack, getFilterStack())) {
                ItemStack filter = stack.copy();
                filter.setCount(1);
                setFilterStack(filter);
                return false;
            }
        }

        return itemValidRaw && (getFilterStack().isEmpty() || ItemStack.isSameItem(stack, getFilterStack()));
    }

    public boolean isItemValidRaw(ItemStack stack) {
        return true;
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        if (!stack.isEmpty() && tile.canModifyFilter(slotIndex)) {
            setFilterStack(ItemStack.EMPTY);
        }
        super.onTake(player, stack);
    }

    public boolean hasStackForRender() {
        return !getItem().isEmpty() || !getFilterStack().isEmpty();
    }

    @Override
    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        super.setByPlayer(newStack, oldStack);
    }

    public ItemStack getStackForRender() {
        return !getItem().isEmpty() ? getItem() : getFilterStack();
    }

    public static class ProcessorInput extends SlotFiltered {
        protected final BasicRecipeHandler<?> recipeHandler;

        public ProcessorInput(ITileFilteredInventory tile, BasicRecipeHandler<?> recipeHandler, int index, int xPosition, int yPosition) {
            super(tile, index, xPosition, yPosition);
            this.recipeHandler = recipeHandler;
        }

        @Override
        public boolean isItemValidRaw(ItemStack stack) {
            return recipeHandler.isValidItemInput(stack);
        }
    }
}