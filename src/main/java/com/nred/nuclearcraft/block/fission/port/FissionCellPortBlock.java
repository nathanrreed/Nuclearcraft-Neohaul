package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.SolidFissionCellEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCellPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;

public class FissionCellPortBlock extends FissionItemPortBlock<FissionCellPortEntity, SolidFissionCellEntity> {
    public FissionCellPortBlock(MultiblockPartBlock.MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionCellPortEntity.class);
    }
}