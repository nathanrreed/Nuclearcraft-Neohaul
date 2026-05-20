package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.PebbleFissionCoolerEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCoolerPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;

public class FissionCoolerPortBlock extends FissionFluidPortBlock<FissionCoolerPortEntity, PebbleFissionCoolerEntity> {
    public FissionCoolerPortBlock(MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionCoolerPortEntity.class);
    }
}