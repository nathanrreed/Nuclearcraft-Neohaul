package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.menu.slot.FilteredSlot;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class FilteredProcessorMenu<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends ProcessorMenu<TILE, PACKET, INFO> {
    public FilteredProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
        super(menuType, containerId, inventory, tile);
    }

    @Override
    protected void addInputSlot(Player player, int index, int xPosition, int yPosition) {
        addSlot(new FilteredSlot.ProcessorInput(tile, recipeHandler, index, xPosition, yPosition));
    }

    @Override
    public ItemStack transferPlayerStack(Player player, int index, int invStart, int invEnd, ItemStack stack) {
        if (recipeHandler.isValidItemInput(stack)) {
            if (!moveItemStackTo(stack, 0, info.itemInputSize, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            NonNullList<ItemStack> filterStacks = tile.getFilterStacks();
            for (int i = 0; i < filterStacks.size(); ++i) {
                if (tile.canModifyFilter(i) && !filterStacks.get(i).isEmpty()) {
                    filterStacks.set(i, ItemStack.EMPTY);
                    tile.onFilterChanged(i);
                    slots.get(i).setChanged();
                    return ItemStack.EMPTY;
                }
            }
        }
        return transferPlayerStackDefault(player, index, invStart, invEnd, stack);
    }
}