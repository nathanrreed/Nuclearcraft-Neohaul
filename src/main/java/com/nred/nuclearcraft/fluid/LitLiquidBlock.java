package com.nred.nuclearcraft.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class LitLiquidBlock extends LiquidBlock {
    public LitLiquidBlock(FlowingFluid fluid, Properties properties) { // TODO refactor fluids
        super(fluid, properties);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return this.fluid.getFluidType().getLightLevel();
    }
}