package com.nred.nuclearcraft.item.energy;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import net.minecraft.world.item.ItemStack;

import static com.nred.nuclearcraft.registration.DataComponentRegistration.ENERGY_COMPONENT;

public interface IChargeableComponentItem {
    default long getEnergyStored(ItemStack stack) {
        return stack.getOrDefault(ENERGY_COMPONENT, 0);
    }

    default void setEnergyStored(ItemStack stack, long energy) {
        stack.set(ENERGY_COMPONENT, Math.toIntExact(energy));
    }

    long getMaxEnergyStored(ItemStack stack);

    int getMaxTransfer(ItemStack stack);

    boolean canReceive(ItemStack stack);

    boolean canExtract(ItemStack stack);

    EnergyConnection getEnergyConnection(ItemStack stack);
}