package com.nred.nuclearcraft.item.energy;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IChargableItem {
    static CompoundTag getEnergyStorageNBT(ItemStack stack) {
        if (!(stack.getItem() instanceof IChargableItem item)) {
            return null;
        }

        CompoundTag nbt = NBTHelper.getStackNBT(stack);
        if (!nbt.contains("energyStorage")) {
            new EnergyStorage(item.getMaxEnergyStored(stack), item.getMaxTransfer(stack), nbt.getLong("waitingEnergy")).writeToNBT(nbt, null, "energyStorage");
        }

        return nbt.getCompound("energyStorage");
    }

    default long getEnergyStored(ItemStack stack) {
        CompoundTag nbt = getEnergyStorageNBT(stack);
        return nbt == null ? 0L : nbt.getLong("energy") + nbt.getLong("waitingEnergy");
    }

    default void setEnergyStored(ItemStack stack, long energy) {
        CompoundTag nbt = getEnergyStorageNBT(stack);
        if (nbt != null && nbt.contains("energy")) {
            nbt.putLong("energy", energy);
        }
    }

    long getMaxEnergyStored(ItemStack stack);

    int getMaxTransfer(ItemStack stack);

    boolean canReceive(ItemStack stack);

    boolean canExtract(ItemStack stack);

    EnergyConnection getEnergyConnection(ItemStack stack);
}