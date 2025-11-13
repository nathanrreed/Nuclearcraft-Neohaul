package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public abstract class NCFluidBlock extends LiquidBlock {
    public final Fluid fluid;

    public NCFluidBlock(FlowingFluid fluid, BlockBehaviour.Properties properties) {
        super(fluid, properties);
        this.fluid = fluid;
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (canSetFireToSurroundings(level, pos, state, random) && level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            int i = random.nextInt(3);
            if (i > 0) {
                BlockPos blockpos = pos;

                for (int j = 0; j < i; j++) {
                    blockpos = blockpos.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                    if (!level.isLoaded(blockpos)) {
                        return;
                    }

                    BlockState blockstate = level.getBlockState(blockpos);
                    if (blockstate.isAir()) {
                        if (this.hasFlammableNeighbours(level, blockpos)) {
                            level.setBlockAndUpdate(blockpos, net.neoforged.neoforge.event.EventHooks.fireFluidPlaceBlockEvent(level, blockpos, pos, BaseFireBlock.getState(level, blockpos)));
                            return;
                        }
                    } else if (blockstate.blocksMotion()) {
                        return;
                    }
                }
            } else {
                for (int k = 0; k < 3; k++) {
                    BlockPos blockpos1 = pos.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                    if (!level.isLoaded(blockpos1)) {
                        return;
                    }

                    if (level.isEmptyBlock(blockpos1.above()) && this.isFlammable(level, blockpos1, Direction.UP)) {
                        level.setBlockAndUpdate(blockpos1.above(), net.neoforged.neoforge.event.EventHooks.fireFluidPlaceBlockEvent(level, blockpos1.above(), pos, BaseFireBlock.getState(level, blockpos1)));
                    }
                }
            }
        }
    }


    public abstract BlockState getSourceMixingState();

    public abstract BlockState getFlowingMixingState();

    protected abstract boolean canSetFireToSurroundings(Level level, BlockPos pos, BlockState state, RandomSource rand);

    public abstract BlockState getFlowingIntoWaterState();

    protected boolean hasFlammableNeighbours(Level level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.isFlammable(level, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    private boolean isFlammable(LevelReader p_level, BlockPos p_pos, Direction face) {
        if (p_pos.getY() >= p_level.getMinBuildHeight() && p_pos.getY() < p_level.getMaxBuildHeight() && !p_level.hasChunkAt(p_pos)) {
            return false;
        }
        BlockState state = p_level.getBlockState(p_pos);
        return state.ignitedByLava() && state.isFlammable(p_level, p_pos, face);
    }
}