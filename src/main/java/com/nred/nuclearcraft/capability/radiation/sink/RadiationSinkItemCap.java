package com.nred.nuclearcraft.capability.radiation.sink;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class RadiationSinkItemCap implements IRadiationSink {
    private final ItemStack itemStack;
    private double radiationLevel;

    public RadiationSinkItemCap(ItemStack itemStack, double radiationLevel) {
        this.itemStack = itemStack;
        this.radiationLevel = radiationLevel;
    }

    @Override
    public CompoundTag writeNBT(IRadiationSink instance, Direction side, CompoundTag nbt) {
        nbt.putDouble("radiationLevel", radiationLevel);
        return nbt;
    }

    @Override
    public void readNBT(IRadiationSink instance, Direction side, CompoundTag nbt) {
        setRadiationLevel(nbt.getDouble("radiationLevel"));
    }

    @Override
    public double getRadiationLevel() {
        readNBT(this, null, itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
        return radiationLevel;
    }

    @Override
    public void setRadiationLevel(double newRads) {
        this.radiationLevel = Math.max(newRads, 0D);
        itemStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, customData -> customData.update(compoundTag -> writeNBT(this, null, compoundTag)));
    }
}