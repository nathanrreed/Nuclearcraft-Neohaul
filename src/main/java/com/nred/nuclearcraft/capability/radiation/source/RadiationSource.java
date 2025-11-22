package com.nred.nuclearcraft.capability.radiation.source;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class RadiationSource implements IRadiationSource, INBTSerializable<CompoundTag> {
    private double radiationLevel, radiationBuffer = 0D, scrubbingFraction = 0D, effectiveScrubberCount = 0D;

    public RadiationSource(double startRadiation) {
        radiationLevel = startRadiation;
    }

    @Override
    public CompoundTag writeNBT(IRadiationSource instance, Direction side, CompoundTag nbt) {
        nbt.putDouble("radiationLevel", getRadiationLevel());
        nbt.putDouble("radiationBuffer", getRadiationBuffer());
        nbt.putDouble("scrubbingFraction", scrubbingFraction);
        nbt.putDouble("effectiveScrubberCount", effectiveScrubberCount);
        return nbt;
    }

    @Override
    public void readNBT(IRadiationSource instance, Direction side, CompoundTag nbt) {
        setRadiationLevel(nbt.getDouble("radiationLevel"));
        setRadiationBuffer(nbt.getDouble("radiationBuffer"));
        scrubbingFraction = nbt.getDouble("scrubbingFraction");
        effectiveScrubberCount = nbt.getDouble("effectiveScrubberCount");
    }

    @Override
    public double getRadiationLevel() {
        return radiationLevel;
    }

    @Override
    public void setRadiationLevel(double newRads) {
        radiationLevel = Math.max(newRads, 0D);
    }

    @Override
    public double getRadiationBuffer() {
        return radiationBuffer;
    }

    @Override
    public void setRadiationBuffer(double newBuffer) {
        radiationBuffer = newBuffer;
    }

    @Override
    public double getScrubbingFraction() {
        return scrubbingFraction;
    }

    @Override
    public void setScrubbingFraction(double newFraction) {
        scrubbingFraction = Mth.clamp(newFraction, 0D, 1D);
    }

    @Override
    public double getEffectiveScrubberCount() {
        return effectiveScrubberCount;
    }

    @Override
    public void setEffectiveScrubberCount(double newScrubberCount) {
        effectiveScrubberCount = Math.max(0D, newScrubberCount);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        return writeNBT(this, null, tag);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        readNBT(this, null, nbt);
    }
}