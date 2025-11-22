package com.nred.nuclearcraft.capability.radiation.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class EntityRads extends PlayerRads {
    protected boolean setMaxRads = false;

    public EntityRads() {
        super();
        giveGuidebook = false;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = super.serializeNBT(provider);
        nbt.putDouble("maxRads", getMaxRads());
        nbt.putBoolean("setMaxRads", setMaxRads);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.getDouble("maxRads") > 0) {
            maxRads = nbt.getDouble("maxRads");
        }
        setMaxRads = nbt.getBoolean("setMaxRads");
        super.deserializeNBT(provider, nbt);
    }
}