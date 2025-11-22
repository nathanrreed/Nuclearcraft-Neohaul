package com.nred.nuclearcraft.util;

import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class FourPos {
	private final BlockPos pos;
	private final ResourceKey<Level> dim;
	
	public FourPos(BlockPos pos, ResourceKey<Level> dim) {
		this.pos = pos;
		this.dim = dim;
	}
	
	public BlockPos getBlockPos() {
		return pos;
	}
	
	public ResourceKey<Level> getDimension() {
		return dim;
	}
	
	public FourPos add(BlockPos added) {
		return new FourPos(pos.offset(added), dim);
	}
	
	public FourPos add(int x, int y, int z) {
		return new FourPos(pos.offset(x, y, z), dim);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof FourPos fourPos)) {
			return false;
		}
		
		if (dim != fourPos.dim) {
			return false;
		}
		return Objects.equals(pos, fourPos.pos);
		
	}
	
	@Override
	public int hashCode() {
		int result = pos != null ? pos.hashCode() : 0;
		result = 31 * result + dim.hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("dim", dim.location()).add("x", pos.getX()).add("y", pos.getY()).add("z", pos.getZ()).toString();
	}
}