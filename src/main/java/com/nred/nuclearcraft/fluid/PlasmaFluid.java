package com.nred.nuclearcraft.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import javax.annotation.Nonnull;

import static com.nred.nuclearcraft.config.NCConfig.fusion_plasma_craziness;
import static net.minecraft.world.level.GameRules.RULE_DOFIRETICK;

public abstract class PlasmaFluid extends BaseFlowingFluid {
    protected PlasmaFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    public void randomTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (fusion_plasma_craziness && level.getGameRules().getBoolean(RULE_DOFIRETICK) && updateFire(level, pos, state, random)) {
            return;
        }
        super.tick(level, pos, state);
    }

    private static boolean updateFire(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull FluidState state, RandomSource random) {
        Direction side = Direction.from3DDataValue(random.nextInt(6));
        BlockPos offPos = pos.relative(side);
        if (!level.isLoaded(offPos)) {
            return false;
        }
        if (level.isEmptyBlock(offPos.above()) && isFlammable(level, offPos, Direction.UP)) {
            level.setBlockAndUpdate(offPos.above(), net.neoforged.neoforge.event.EventHooks.fireFluidPlaceBlockEvent(level, offPos.above(), pos, BaseFireBlock.getState(level, offPos)));
            return true;
        }

        return false;
    }

    private static boolean isFlammable(LevelReader level, BlockPos pos, Direction face) {
        if (pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && !level.hasChunkAt(pos)) {
            return false;
        }
        BlockState state = level.getBlockState(pos);
        return state.ignitedByLava() && state.isFlammable(level, pos, face);
    }

    public static class Source extends NCSourceFluid {
        public Source(Properties properties, int level) {
            super(properties, level);
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}