package com.nred.nuclearcraft.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

public class GasFluid extends BaseFlowingFluid.Source {
    public GasFluid(Properties properties) {
        super(properties);
    }

    @Override
    public int getAmount(FluidState state) {
        return 2;
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    public void tick(Level level, BlockPos pos, FluidState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        if (level.getBlockState(pos.above()).isAir()) {
            level.setBlock(pos.above(), state.createLegacyBlock(), 3);
        } else { // Hit ceiling
            level.playLocalSound(pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.2f, 1f, false);
        }
    }

    @Override
    protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {

    }
//        super.animateTick(state, level, pos.above(), random);
//    }

//    @Override
//    protected void spread(Level level, BlockPos pos, FluidState state) {
//        super.spread(level, pos, state);
//    }

    @Override
    protected boolean canSpreadTo(BlockGetter level, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, FluidState toFluidState, Fluid fluid) {
        return false;
    }
}
