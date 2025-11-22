package com.nred.nuclearcraft.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public interface ICapability<T extends ICapability<T>> { // TODO DELETE?
	CompoundTag writeNBT(T instance, Direction side, CompoundTag nbt);
	
	void readNBT(T instance, Direction side, CompoundTag nbt);
}
