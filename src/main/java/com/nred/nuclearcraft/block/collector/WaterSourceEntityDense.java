package com.nred.nuclearcraft.block.collector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WaterSourceEntityDense extends WaterSourceEntity {
    public WaterSourceEntityDense(BlockPos pos, BlockState blockState) {
        super(pos, blockState, MACHINE_LEVEL.DENSE);
    }
}
