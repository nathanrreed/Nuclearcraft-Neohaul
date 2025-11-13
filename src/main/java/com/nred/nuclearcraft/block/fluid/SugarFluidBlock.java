package com.nred.nuclearcraft.block.fluid;

import com.nred.nuclearcraft.helpers.SimpleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class SugarFluidBlock extends NCFluidBlock {
    public SugarFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(SimpleHelper.newEffect(MobEffects.MOVEMENT_SLOWDOWN, 3, 100));
        }
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