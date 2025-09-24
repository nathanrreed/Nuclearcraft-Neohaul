package com.nred.nuclearcraft.helpers;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

public class CustomEnergyHandler extends EnergyStorage {
    private final boolean allowInput;
    private final boolean allowOutput;
    private int tempEnergy = 0;

    public CustomEnergyHandler(int capacity, boolean allowInput, boolean allowOutput) {
        super(capacity);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
    }

    public CustomEnergyHandler(int capacity, int stored, int transfer, boolean allowInput, boolean allowOutput) {
        super(capacity);
        this.allowInput = allowInput;
        this.allowOutput = allowOutput;
        this.energy = stored;
        this.setMaxTransfer(transfer);
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

    public void setEnergyStored(int stored) {
        this.energy = stored;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public void copy(CustomEnergyHandler other) {
        this.setCapacity(other.getMaxEnergyStored());
        this.setMaxTransfer(other.getMaxExtract());
        this.setEnergyStored(other.getEnergyStored());
    }

    public void setCapacitySimple(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxTransfer(int maxTransfer) {
        this.maxExtract = maxTransfer;
        this.maxReceive = maxTransfer;
    }

    public void mergeEnergyStorage(CustomEnergyHandler other) {
        setCapacity(getMaxEnergyStored() + other.getMaxEnergyStored());
        energy = getEnergyStored() + other.getEnergyStored();
        other.energy = 0;
        onContentsChanged();
        other.onContentsChanged();
    }

    public void changeEnergyStored(int energy) {
        this.energy = Mth.clamp(this.energy + energy, 0, capacity);
        onContentsChanged();
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

    public static final StreamCodec<RegistryFriendlyByteBuf, CustomEnergyHandler> ENERGY_HANDLER_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull CustomEnergyHandler decode(RegistryFriendlyByteBuf buffer) { //int capacity, int stored, int transfer, boolean allowInput, boolean allowOutput
            return new CustomEnergyHandler(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, CustomEnergyHandler value) {
            buffer.writeInt(value.capacity);
            buffer.writeInt(value.energy);
            buffer.writeInt(value.maxExtract);
            buffer.writeBoolean(value.allowInput);
            buffer.writeBoolean(value.allowOutput);
        }
    };
}