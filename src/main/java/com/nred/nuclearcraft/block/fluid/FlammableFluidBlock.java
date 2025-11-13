package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class FlammableFluidBlock extends NCFluidBlock {
    public FlammableFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 500;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 200;
    }

    @Override
    public BlockState getSourceMixingState() {
        return null;
    }

    @Override
    public BlockState getFlowingMixingState() {
        return null;
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