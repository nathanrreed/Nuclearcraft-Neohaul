package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.menu.InfoTileMenu;
import com.nred.nuclearcraft.menu.slot.ProcessorInputSlot;
import com.nred.nuclearcraft.menu.slot.ProcessorResultSlot;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class ProcessorMenu<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>> extends InfoTileMenu<TILE, PACKET, INFO> {
    protected final TILE tile;
    protected final BasicRecipeHandler<?> recipeHandler;

    public ProcessorMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
        super(menuType, containerId, inventory, tile);
        this.tile = tile;
        this.recipeHandler = tile.getRecipeHandler();

        addMachineSlots(inventory.player);
        info.addPlayerSlots(this::addSlot, inventory.player);
        tile.addTileUpdatePacketListener(inventory.player);
    }

    protected void addMachineSlots(Player player) {
        for (int i = 0; i < info.itemInputSize; ++i) {
            int[] stackXY = info.itemInputStackXY.get(i);
            addInputSlot(player, i, stackXY[0], stackXY[1]);
        }

        for (int i = 0; i < info.itemOutputSize; ++i) {
            int[] stackXY = info.itemOutputStackXY.get(i);
            addOutputSlot(player, i + info.itemInputSize, stackXY[0], stackXY[1]);
        }
    }

    protected void addInputSlot(Player player, int index, int xPosition, int yPosition) {
        addSlot(new ProcessorInputSlot(tile, recipeHandler, index, xPosition, yPosition));
    }

    protected void addOutputSlot(Player player, int index, int xPosition, int yPosition) {
        addSlot(new ProcessorResultSlot(player, tile, index, xPosition, yPosition));
    }

    @Override
    public boolean stillValid(Player player) {
        return tile.isUsableByPlayer(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        tile.removeTileUpdatePacketListener(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        int invStart = info.getInventorySize();
        int invEnd = info.getCombinedInventorySize();

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            stackCopy = stack.copy();

            if (index >= info.itemInputSize && index < invStart) {
                if (!moveItemStackTo(stack, invStart, invEnd, false)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, stackCopy);
            } else if (index >= invStart) {
                ItemStack transfer = transferPlayerStack(player, index, invStart, invEnd, stack);
                if (transfer != null) {
                    return transfer;
                }
            } else if (!moveItemStackTo(stack, invStart, invEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == stackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return stackCopy;
    }

    public ItemStack transferPlayerStack(Player player, int index, int invStart, int invEnd, ItemStack stack) {
        if (recipeHandler.isValidItemInput(stack)) {
            if (!moveItemStackTo(stack, 0, info.itemInputSize, false)) {
                return ItemStack.EMPTY;
            }
        }
        return transferPlayerStackDefault(player, index, invStart, invEnd, stack);
    }
}