package com.nred.nuclearcraft.block_entity.internal.inventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class InventoryConnection {
    private final @Nonnull List<ItemSorption> sorptionList;
    private final @Nonnull List<ItemSorption> defaultSorptions;

    public InventoryConnection(@Nonnull List<ItemSorption> sorptionList) {
        this.sorptionList = new ArrayList<>(sorptionList);
        defaultSorptions = new ArrayList<>(sorptionList);
    }

    private InventoryConnection(@Nonnull InventoryConnection connection) {
        sorptionList = new ArrayList<>(connection.sorptionList);
        defaultSorptions = new ArrayList<>(connection.defaultSorptions);
    }

    private InventoryConnection copy() {
        return new InventoryConnection(this);
    }

    public static InventoryConnection[] cloneArray(@Nonnull InventoryConnection[] connections) {
        InventoryConnection[] clone = new InventoryConnection[6];
        for (int i = 0; i < 6; ++i) {
            clone[i] = connections[i].copy();
        }
        return clone;
    }

    public ItemSorption getItemSorption(int slot) {
        return sorptionList.get(slot);
    }

    public void setItemSorption(int slot, ItemSorption sorption) {
        sorptionList.set(slot, sorption);
    }

    public ItemSorption getDefaultItemSorption(int slot) {
        return defaultSorptions.get(slot);
    }

    public boolean canConnect() {
        for (ItemSorption sorption : sorptionList) {
            if (sorption.canConnect()) {
                return true;
            }
        }
        return false;
    }

    public boolean anyOfSorption(ItemSorption itemSorption) {
        for (ItemSorption sorption : sorptionList) {
            if (sorption.equals(itemSorption)) {
                return true;
            }
        }
        return false;
    }

    public int[] getSlotsForFace() {
        IntList slotList = new IntArrayList();
        for (int i = 0; i < sorptionList.size(); ++i) {
            if (getItemSorption(i).canConnect()) {
                slotList.add(i);
            }
        }
        return slotList.stream().mapToInt(i -> i).toArray();
    }

    public void toggleItemSorption(int slot, ItemSorption.Type type, boolean reverse) {
        setItemSorption(slot, getItemSorption(slot).next(type, reverse));
    }

    public final CompoundTag writeToNBT(CompoundTag nbt, @Nonnull Direction side) {
        CompoundTag connectionTag = new CompoundTag();
        for (int i = 0; i < sorptionList.size(); ++i) {
            connectionTag.putInt("sorption" + i, getItemSorption(i).ordinal());
        }
        nbt.put("inventoryConnection" + side.ordinal(), connectionTag);
        return nbt;
    }

    public final InventoryConnection readFromNBT(CompoundTag nbt, @Nonnull Direction side) {
        if (nbt.contains("inventoryConnection" + side.ordinal())) {
            CompoundTag connectionTag = nbt.getCompound("inventoryConnection" + side.ordinal());
            for (int i = 0; i < sorptionList.size(); ++i) {
                if (connectionTag.contains("sorption" + i)) {
                    setItemSorption(i, ItemSorption.values()[connectionTag.getInt("sorption" + i)]);
                }
            }
        }
        return this;
    }
}