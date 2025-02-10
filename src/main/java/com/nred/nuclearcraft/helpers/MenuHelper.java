package com.nred.nuclearcraft.helpers;

import net.minecraft.core.NonNullList;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MenuHelper {
    public static ArrayList<int[]> listPlayerInventoryPos(int offset) {
        ArrayList<int[]> list = new ArrayList<>();
        for (int y : List.of(0, 1, 2)) {
            for (int x : IntStream.rangeClosed(0, 8).toArray()) {
                list.add(new int[]{x + y * 9 + 9, 8 + x * 18, 84 + y * 18 + offset});
            }
        }
        return list;
    }

    public static ArrayList<int[]> listPlayerHotbarPos(int offset) {
        ArrayList<int[]> list = new ArrayList<>();
        for (int x : IntStream.rangeClosed(0, 8).toArray()) {
            list.add(new int[]{x, 8 + x * 18, 142 + offset});
        }
        return list;
    }

    public static ArrayList<int[]> listPlayerInventoryHotbarPos(int offset) {
        ArrayList<int[]> rtn = listPlayerInventoryPos(offset);
        rtn.addAll(listPlayerHotbarPos(offset));
        return rtn;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    public static ItemStack quickMoveStack(Player playerIn, int pIndex, NonNullList<Slot> slots, PropertyDispatch.QuadFunction<ItemStack, Integer, Integer, Boolean, Boolean> moveItemStackTo, int TE_INVENTORY_SLOT_COUNT) { //(ItemStack, int, int, Boolean) -> Boolean moveItemStackTo,
        Slot sourceSlot = slots.get(pIndex);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY; //EMPTY_ITEM

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo.apply(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY; // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo.apply(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:$pIndex");
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
}
