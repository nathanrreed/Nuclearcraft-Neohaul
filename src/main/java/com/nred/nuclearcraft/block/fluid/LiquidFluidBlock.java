package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.FLUID_BURN;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.HYPOTHERMIA;

public class LiquidFluidBlock extends NCFluidBlock {
    public LiquidFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        int temp = fluid.getFluidType().getTemperature();
        if (temp < 250) {
            entity.hurt(level.damageSources().source(HYPOTHERMIA), 0.024f * (250 - temp));
        } else if (temp > 350) {
            entity.hurt(level.damageSources().source(FLUID_BURN), 6f * (float) Math.log10(temp - 350));
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