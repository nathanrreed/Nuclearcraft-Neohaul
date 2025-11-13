package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.GAS_BURN;

public class HotGasFluidBlock extends NCFluidBlock {
    public HotGasFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties.mapColor(MapColor.NONE));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        entity.hurt(level.damageSources().source(GAS_BURN), 3F);
        entity.setRemainingFireTicks(1);
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
        return true;
    }

    @Override
    public BlockState getFlowingIntoWaterState() {
        return null;
    }
}