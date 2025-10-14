package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.block.info.ProcessorContainerInfo;
import com.nred.nuclearcraft.block.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block.processor.IProcessor;
import com.nred.nuclearcraft.menu.slot.SlotFiltered;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ContainerFilteredProcessor<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>> extends ContainerProcessor<TILE, PACKET, INFO> {
    public ContainerFilteredProcessor(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
        super(menuType, containerId, inventory, tile);
    }

    @Override
    protected void addInputSlot(Player player, int index, int xPosition, int yPosition) {
        addSlot(new SlotFiltered.ProcessorInput(tile, recipeHandler, index, xPosition, yPosition));
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