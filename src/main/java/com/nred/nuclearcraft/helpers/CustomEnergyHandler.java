package com.nred.nuclearcraft.helpers;

import net.neoforged.neoforge.energy.EnergyStorage;

public class CustomEnergyHandler extends EnergyStorage {
    private final boolean allowInput;
    private final boolean allowOutput;

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
                onContentsChanged();
            }
            return rtn;
        }
        return 0;
    }

    // Normal insert and extract for use within the mod
    public int internalInsertEnergy(int toReceive, boolean simulate) {
        return super.receiveEnergy(toReceive, simulate);
    }

    public int internalExtractEnergy(int toExtract, boolean simulate) {
        return super.extractEnergy(toExtract, simulate);
    }

    protected void onContentsChanged() {
    }
}