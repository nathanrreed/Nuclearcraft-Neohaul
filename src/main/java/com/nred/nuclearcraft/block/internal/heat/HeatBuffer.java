package com.nred.nuclearcraft.block.internal.heat;

import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class HeatBuffer {
    private long heatStored, heatCapacity;

    public HeatBuffer(long capacity) {
        heatCapacity = capacity;
    }

    public HeatBuffer(long capacity, long stored) {
        heatStored = stored;
        heatCapacity = capacity;
    }

    public long removeHeat(long heat, boolean simulated) {
        long heatRemoved = Math.min(heatStored, heat);
        if (!simulated) {
            heatStored -= heatRemoved;
        }
        return heatRemoved;
    }

    public long addHeat(long heat, boolean simulated) {
        long heatAdded = Math.min(heatCapacity - heatStored, heat);
        if (!simulated) {
            heatStored += heatAdded;
        }
        return heatAdded;
    }

    public long getHeatStored() {
        return heatStored;
    }

    public long getHeatCapacity() {
        return heatCapacity;
    }

    public void changeHeatStored(long heat) {
        heatStored = NCMath.clamp(heatStored + heat, 0, heatCapacity);
    }

    /**
     * Ignores heat capacity!
     */
    public void setHeatStored(long heat) {
        heatStored = Math.max(0, heat);
    }

    /**
     * Ignores heat stored!
     */
    public void setHeatCapacity(long newCapacity) {
        heatCapacity = Math.max(0, newCapacity);
        // if (newCapacity < heatStored) setHeatStored(newCapacity);
    }

    public boolean isFull() {
        return heatStored >= heatCapacity;
    }

    public boolean isEmpty() {
        return heatStored <= 0L;
    }

    public void mergeHeatBuffers(HeatBuffer other) {
        setHeatStored(heatStored + other.heatStored);
        setHeatCapacity(heatCapacity + other.heatCapacity);
    }

    // NBT
    public CompoundTag writeToNBT(CompoundTag nbt, String name) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("heatStored", heatStored);
        tag.putLong("heatCapacity", heatCapacity);
        nbt.put(name, tag);
        return nbt;
    }

    public HeatBuffer readFromNBT(CompoundTag nbt, String name) {
        if (nbt.contains(name, 10)) {
            CompoundTag tag = nbt.getCompound(name);
            heatStored = tag.getLong("heatStored");
            heatCapacity = tag.getLong("heatCapacity");
        }
        return this;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, HeatBuffer> HEAT_BUFFER_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, HeatBuffer::getHeatCapacity,
            ByteBufCodecs.VAR_LONG, HeatBuffer::getHeatStored,
            HeatBuffer::new
    );
}