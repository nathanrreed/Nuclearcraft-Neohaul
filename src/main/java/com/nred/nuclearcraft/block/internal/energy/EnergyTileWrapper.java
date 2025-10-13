package com.nred.nuclearcraft.block.internal.energy;

import com.nred.nuclearcraft.block.energy.ITileEnergy;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record EnergyTileWrapper(ITileEnergy tile, Direction side) implements IEnergyStorage {
    @Override
    public int getEnergyStored() {
        return tile.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return tile.getMaxEnergyStored();
    }

    @Override
    public boolean canReceive() {
        return tile.canReceiveEnergy(side);
    }

    @Override
    public boolean canExtract() {
        return tile.canExtractEnergy(side);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return tile.receiveEnergy(maxReceive, side, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return tile.extractEnergy(maxExtract, side, simulate);
    }
}