package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import static com.nred.nuclearcraft.registration.DamageTypeRegistration.MOLTEN_BURN;

public class GlowstoneFluidBlock extends NCFluidBlock {
    public GlowstoneFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties.lightLevel(s -> 15));
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        entity.hurt(level.damageSources().source(MOLTEN_BURN), 3F);
        entity.setRemainingFireTicks(10);
    }

    @Override
    public BlockState getSourceMixingState() {
        return Blocks.GLOWSTONE.defaultBlockState();
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
        return Blocks.STONE.defaultBlockState();
    }
}