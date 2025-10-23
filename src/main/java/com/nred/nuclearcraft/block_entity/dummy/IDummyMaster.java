package com.nred.nuclearcraft.block_entity.dummy;

import com.nred.nuclearcraft.block_entity.ITile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IDummyMaster extends ITile {
	default void onDummyNeighborChanged(BlockState state, Level level, BlockPos pos, BlockPos fromPos) {
		refreshIsRedstonePowered(level, pos);
	}
	
	@Override
	default boolean checkIsRedstonePowered(Level level, BlockPos pos) {
		return ITile.super.checkIsRedstonePowered(level, pos) || level.hasNeighborSignal(getTilePos());
	}
}