package com.nred.nuclearcraft.item.energy;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public interface IChargeableItem {
    static CompoundTag getEnergyStorageNBT(ItemStack stack) {
        if (!(stack.getItem() instanceof IChargeableItem item)) {
            return null;
        }

        CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!nbt.contains("energyStorage")) {
            new EnergyStorage(item.getMaxEnergyStored(stack), item.getMaxTransfer(stack), nbt.getLong("waitingEnergy")).writeToNBT(nbt, null, "energyStorage");
        }

        return nbt.getCompound("energyStorage");
    }

    static long getEnergyStored(ItemStack stack) {
        CompoundTag nbt = getEnergyStorageNBT(stack);
        return nbt == null ? 0L : nbt.getLong("energy") + nbt.getLong("waitingEnergy");
    }

    static void setEnergyStored(ItemStack stack, long energy) {
        CompoundTag nbt = getEnergyStorageNBT(stack);
        if (nbt != null && nbt.contains("energy")) {
            nbt.putLong("energy", energy);
            stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, customData -> customData.update(tag -> tag.put("energyStorage", nbt)));
        }
    }

    long getMaxEnergyStored(ItemStack stack);

    int getMaxTransfer(ItemStack stack);

    boolean canReceive(ItemStack stack);

    boolean canExtract(ItemStack stack);

    EnergyConnection getEnergyConnection(ItemStack stack);
}