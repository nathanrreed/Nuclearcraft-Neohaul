package com.nred.nuclearcraft.block.rtg;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.rtg.IRTGPartType;
import com.nred.nuclearcraft.multiblock.rtg.RTGMultiblock;
import org.jetbrains.annotations.NotNull;

public class RTGBlock extends GenericTooltipDeviceBlock<RTGMultiblock, IRTGPartType> {
    public RTGBlock(@NotNull MultiblockPartProperties<IRTGPartType> properties) {
        super(properties);
    }
}