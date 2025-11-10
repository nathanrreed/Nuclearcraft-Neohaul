package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionVesselEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;

public class FissionVesselPortBlock extends FissionFluidPortBlock<FissionVesselPortEntity, SaltFissionVesselEntity> {
    public FissionVesselPortBlock(MultiblockPartBlock.MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionVesselPortEntity.class);
    }
}