package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.FissionIrradiatorEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionIrradiatorPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;

public class FissionIrradiatorPortBlock extends FissionItemPortBlock<FissionIrradiatorPortEntity, FissionIrradiatorEntity> {
    public FissionIrradiatorPortBlock(MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionIrradiatorPortEntity.class);
    }
}