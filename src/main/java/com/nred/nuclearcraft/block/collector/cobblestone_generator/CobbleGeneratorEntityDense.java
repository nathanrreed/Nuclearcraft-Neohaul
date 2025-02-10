package com.nred.nuclearcraft.block.collector.cobblestone_generator;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CobbleGeneratorEntityDense extends CobbleGeneratorEntity{
    public CobbleGeneratorEntityDense(BlockPos pos, BlockState blockState) {
        super(pos, blockState, MACHINE_LEVEL.DENSE);
    }
}
