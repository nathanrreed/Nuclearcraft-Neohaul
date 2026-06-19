package com.nred.nuclearcraft.block.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import static com.nred.nuclearcraft.registration.BlockRegistration.SOLIDIFIED_CORIUM;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.CORIUM_BURN;

public class CoriumFluidBlock extends FissionFluidBlock {
    public CoriumFluidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        entity.hurt(level.damageSources().source(CORIUM_BURN), 4F);
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public BlockState getSourceMixingState() {
        return SOLIDIFIED_CORIUM.get().defaultBlockState();
    }

    @Override
    public BlockState getFlowingMixingState() {
        return Blocks.STONE.defaultBlockState();
    }
}