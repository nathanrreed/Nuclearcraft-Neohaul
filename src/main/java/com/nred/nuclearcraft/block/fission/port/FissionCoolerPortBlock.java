package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.FissionCoolerEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCoolerPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;

public class FissionCoolerPortBlock extends FissionFluidPortBlock<FissionCoolerPortEntity, FissionCoolerEntity> {
    public FissionCoolerPortBlock(MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionCoolerPortEntity.class);
    }
}