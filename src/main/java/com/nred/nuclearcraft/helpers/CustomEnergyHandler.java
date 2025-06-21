package com.nred.nuclearcraft.helpers;

import net.neoforged.neoforge.energy.EnergyStorage;

public class CustomEnergyHandler extends EnergyStorage {
    private final boolean allowInput;
    private final boolean allowOutput;
    private int tempEnergy = 0;

    public CustomEnergyHandler(int capacity, boolean allowInput, boolean allowOutput) {
        super(capacity);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
    }

    // Stop other mods using capability from doing unexpected things
    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        if (allowInput) {
            int rtn = super.receiveEnergy(toReceive, simulate);
            if (rtn > 0 && !simulate) {
                onContentsChanged();
            }
            return rtn;
        }
        return 0;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        if (allowOutput) {
            int rtn = super.extractEnergy(toExtract, simulate);
            if (rtn > 0 && !simulate) {
                this.tempEnergy = 0;
                onContentsChanged();
            }
            return rtn;
        }
        return 0;
    }

    public void setCapacity(int capacity) {
        if (this.capacity == capacity) return;
        int oldEnergy = Math.max(energy, this.tempEnergy);

        if (this.tempEnergy > this.capacity) {
            this.energy = Math.min(this.tempEnergy, capacity);
        } else {
            this.energy = Math.min(this.energy, capacity);
        }

        this.capacity = capacity;
        this.tempEnergy = oldEnergy; // Save capacity encase of miss-click
    }

    public void setMaxTransfer(int maxTransfer) {
        this.maxExtract = maxTransfer;
        this.maxReceive = maxTransfer;
    }

    public void mergeEnergyStorage(CustomEnergyHandler other) {
        setCapacity(getMaxEnergyStored() + other.getMaxEnergyStored());
        energy = getEnergyStored() + other.getEnergyStored();
        other.energy = 0;
    }

    public void cullEnergyStored() {
        if (getEnergyStored() > getMaxEnergyStored()) {
            setCapacity(getMaxEnergyStored());
        }
    }

    // Normal insert and extract for use within the mod
    public int internalInsertEnergy(int toReceive, boolean simulate) {
        int rtn = super.receiveEnergy(toReceive, simulate);
        if (rtn > 0 && !simulate) {
            onContentsChanged();
        }
        return rtn;
    }

    public int internalExtractEnergy(int toExtract, boolean simulate) {
        int rtn = super.extractEnergy(toExtract, simulate);
        if (rtn > 0 && !simulate) {
            this.tempEnergy = 0;
            onContentsChanged();
        }
        return rtn;
    }

    protected void onContentsChanged() {
    }
}