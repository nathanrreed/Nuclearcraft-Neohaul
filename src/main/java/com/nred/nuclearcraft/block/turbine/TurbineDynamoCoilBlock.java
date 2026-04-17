package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.turbine.ITurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;

public class TurbineDynamoCoilBlock extends GenericTooltipDeviceBlock<Turbine, ITurbinePartType> {
    public TurbineDynamoCoilBlock(MultiblockPartProperties<ITurbinePartType> properties) {
        super(properties);
    }
}