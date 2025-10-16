package com.nred.nuclearcraft.block_entity.internal.energy;

import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class EnergyStorage implements IEnergyStorage {
    private long energyStored, energyCapacity;
    private int maxTransfer;

    public EnergyStorage(long capacity) {
        this(capacity, NCMath.toInt(capacity));
    }

    public EnergyStorage(long capacity, int maxTransfer) {
        setStorageCapacity(capacity);
        setMaxTransfer(maxTransfer);
    }

    public EnergyStorage(long capacity, int maxTransfer, long energy) {
        this(capacity, maxTransfer);
        setEnergyStored(energy);
    }

    // Forge Energy

    @Override
    public final int getEnergyStored() {
        return NCMath.toInt(getEnergyStoredLong());
    }

    @Override
    public final int getMaxEnergyStored() {
        return NCMath.toInt(getMaxEnergyStoredLong());
    }

    public int getMaxTransfer() {
        return maxTransfer;
    }

    public long getEnergyStoredLong() {
        return Math.min(energyStored, energyCapacity);
    }

    public long getMaxEnergyStoredLong() {
        return energyCapacity;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public int receiveEnergy(int receive, boolean simulated) {
        int energyReceived = Math.min(NCMath.toInt(energyCapacity - energyStored), Math.min(maxTransfer, receive));
        if (energyReceived <= 0) {
            return 0;
        }

        if (!simulated) {
            changeEnergyStored(energyReceived);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int extract, boolean simulated) {
        int energyExtracted = Math.min(NCMath.toInt(energyStored), Math.min(maxTransfer, extract));
        if (energyExtracted <= 0) {
            return 0;
        }

        if (!simulated) {
            changeEnergyStored(-energyExtracted);
        }
        return energyExtracted;
    }

    public void changeEnergyStored(long energy) {
        energyStored = NCMath.clamp(energyStored + energy, 0, energyCapacity);
    }

    /**
     * Ignores energy capacity!
     */
    public void setEnergyStored(long energy) {
        energyStored = Math.max(0, energy);
    }

    /**
     * Ignores energy stored!
     */
    public void setStorageCapacity(long newCapacity) {
        energyCapacity = newCapacity;
        // cullEnergyStored();
    }

    public void setMaxTransfer(long newMaxTransfer) {
        maxTransfer = NCMath.toInt(newMaxTransfer);
    }

    /**
     * Use to remove excess stored energy
     */
    public void cullEnergyStored() {
        if (energyStored > energyCapacity) {
            setEnergyStored(energyCapacity);
        }
    }

    public boolean isFull() {
        return energyStored >= energyCapacity;
    }

    public boolean isEmpty() {
        return energyStored == 0;
    }

    public void mergeEnergyStorage(EnergyStorage other) {
        setStorageCapacity(getMaxEnergyStoredLong() + other.getMaxEnergyStoredLong());
        setEnergyStored(getEnergyStoredLong() + other.getEnergyStoredLong());
        other.setEnergyStored(0L);
    }

    @Override
    public String toString() {
        return "EnergyStorage[" + energyStored + "/" + energyCapacity + "]";
    }

    // NBT

    public final CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider registries, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("energy", energyStored);
        tag.putLong("capacity", energyCapacity);
        tag.putInt("maxTransfer", maxTransfer);
        nbt.put(name, tag);
        return nbt;
    }

    public final EnergyStorage readFromNBT(CompoundTag nbt, HolderLookup.Provider registries, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            energyStored = tag.getLong("energy");
            if (tag.contains("capacity", 99)) {
                energyCapacity = tag.getLong("capacity");
            }
            if (tag.contains("maxTransfer", 99)) {
                maxTransfer = tag.getInt("maxTransfer");
            }
        }
        return this;
    }
}
