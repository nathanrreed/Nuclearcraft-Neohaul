package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.block.item.energy.ItemBlockEnergy;
import com.nred.nuclearcraft.item.energy.IChargeableItem;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ItemEnergyStorage implements IEnergyStorage {
    protected final ItemStack parent;
    protected final long capacity;
    protected final int maxTransfer;


    public ItemEnergyStorage(ItemStack parent) {
        this.parent = parent;
        this.capacity = ((ItemBlockEnergy) parent.getItem()).getMaxEnergyStored(parent);
        this.maxTransfer = ((ItemBlockEnergy) parent.getItem()).getMaxTransfer(parent);
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (!canReceive() || toReceive <= 0) {
            return 0;
        }

        long energy = this.getEnergyStored();
        long energyReceived = Mth.clamp(this.capacity - energy, 0, Math.min(this.maxTransfer, toReceive));
        if (!simulate && energyReceived > 0) {
            this.setEnergy(energy + energyReceived);
        }
        return Math.toIntExact(energyReceived);
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (!canExtract() || toExtract <= 0) {
            return 0;
        }

        int energy = this.getEnergyStored();
        int energyExtracted = Math.min(energy, Math.min(this.maxTransfer, toExtract));
        if (!simulate && energyExtracted > 0) {
            this.setEnergy(energy - energyExtracted);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        long rawEnergy = IChargeableItem.getEnergyStored(this.parent);
        return Math.toIntExact(Mth.clamp(rawEnergy, 0, this.capacity));
    }

    @Override
    public int getMaxEnergyStored() {
        return Math.toIntExact(this.capacity);
    }

    @Override
    public boolean canExtract() {
        return this.maxTransfer > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxTransfer > 0;
    }

    protected void setEnergy(long energy) {
        long realEnergy = Mth.clamp(energy, 0, this.capacity);
        IChargeableItem.setEnergyStored(this.parent, realEnergy);
    }
}
