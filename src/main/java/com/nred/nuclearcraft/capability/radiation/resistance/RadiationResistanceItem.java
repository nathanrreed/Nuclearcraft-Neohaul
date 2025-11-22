package com.nred.nuclearcraft.capability.radiation.resistance;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class RadiationResistanceItem implements IRadiationResistance {
    private double baseRadResistance, shieldingRadResistance = 0D;

    public RadiationResistanceItem(double baseRadResistance) {
        this.baseRadResistance = baseRadResistance;
    }

    public RadiationResistanceItem(double baseRadResistance, double shieldingRadResistance) {
        this.baseRadResistance = baseRadResistance;
        this.shieldingRadResistance = shieldingRadResistance;
    }

    public static final Codec<RadiationResistanceItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.DOUBLE.fieldOf("radiationResistance").forGetter(RadiationResistanceItem::getBaseRadResistance),
                    Codec.DOUBLE.fieldOf("shieldingRadResistance").forGetter(RadiationResistanceItem::getShieldingRadResistance)
            ).apply(instance, RadiationResistanceItem::new)
    );

    public static final StreamCodec<ByteBuf, RadiationResistanceItem> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, RadiationResistanceItem::getBaseRadResistance,
            ByteBufCodecs.DOUBLE, RadiationResistanceItem::getShieldingRadResistance,
            RadiationResistanceItem::new
    );

    @Override
    public CompoundTag writeNBT(IRadiationResistance instance, Direction side, CompoundTag nbt) {
        nbt.putDouble("radiationResistance", getBaseRadResistance());
        nbt.putDouble("shieldingRadResistance", getShieldingRadResistance());
        return nbt;
    }

    @Override
    public void readNBT(IRadiationResistance instance, Direction side, CompoundTag nbt) {
        setBaseRadResistance(nbt.getDouble("radiationResistance"));
        setShieldingRadResistance(nbt.getDouble("shieldingRadResistance"));
    }

    @Override
    public double getBaseRadResistance() {
        return baseRadResistance;
    }

    @Override
    public void setBaseRadResistance(double newResistance) {
        baseRadResistance = Math.max(newResistance, 0D);
    }

    @Override
    public double getShieldingRadResistance() {
        return shieldingRadResistance;
    }

    @Override
    public void setShieldingRadResistance(double newResistance) {
        shieldingRadResistance = Math.max(newResistance, 0D);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.baseRadResistance, this.shieldingRadResistance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return obj instanceof RadiationResistanceItem ex
                    && this.baseRadResistance == ex.baseRadResistance
                    && this.shieldingRadResistance == ex.shieldingRadResistance;
        }
    }
}