package com.nred.nuclearcraft.block.collector.nitrogen_collector;

import com.nred.nuclearcraft.block.collector.MACHINE_LEVEL;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class NitrogenCollectorEntityCompact extends NitrogenCollectorEntity {
    public NitrogenCollectorEntityCompact(BlockPos pos, BlockState blockState) {
        super(pos, blockState, MACHINE_LEVEL.COMPACT);
    }
}
