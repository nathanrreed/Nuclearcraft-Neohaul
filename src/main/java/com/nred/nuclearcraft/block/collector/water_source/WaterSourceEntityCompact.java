package com.nred.nuclearcraft.block.collector.water_source;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WaterSourceEntityCompact extends WaterSourceEntity {
    public WaterSourceEntityCompact(BlockPos pos, BlockState blockState) {
        super(pos, blockState, MACHINE_LEVEL.COMPACT);
    }
}
