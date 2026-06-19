package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class SoulFluidBlock extends NCFluidBlock {
	public SoulFluidBlock(FlowingFluid fluid, Properties properties) {
		super(fluid, properties.mapColor(MapColor.NONE).replaceable().pushReaction(PushReaction.DESTROY));
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.tick(state, level, pos, random);
		if (state.getFluidState().isSource()) {
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@Override
	public BlockState getSourceMixingState() {
		return Blocks.OBSIDIAN.defaultBlockState();
	}

	@Override
	public BlockState getFlowingMixingState() {
		return Blocks.COBBLESTONE.defaultBlockState();
	}

	@Override
	protected boolean canSetFireToSurroundings(Level level, BlockPos pos, BlockState state, RandomSource rand) {
		return false;
	}

	@Override
	public BlockState getFlowingIntoWaterState() {
		return null;
	}
}