package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.PLASMA_BURN;

public class PlasmaFluidBlock extends NCFluidBlock {
    public PlasmaFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties.mapColor(MapColor.NONE).lightLevel(s -> 15));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        entity.hurt(level.damageSources().source(PLASMA_BURN), 8F);
        entity.setRemainingFireTicks(10);
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