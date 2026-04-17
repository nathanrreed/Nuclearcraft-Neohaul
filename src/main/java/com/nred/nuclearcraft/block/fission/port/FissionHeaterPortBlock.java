package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionHeaterEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;

public class FissionHeaterPortBlock extends FissionFluidPortBlock<FissionHeaterPortEntity, SaltFissionHeaterEntity> {
    public FissionHeaterPortBlock(MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionHeaterPortEntity.class);
    }
}