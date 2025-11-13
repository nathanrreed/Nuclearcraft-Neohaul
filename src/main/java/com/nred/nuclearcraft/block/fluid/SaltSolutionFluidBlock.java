package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class SaltSolutionFluidBlock extends NCFluidBlock {
    public SaltSolutionFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
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