package com.nred.nuclearcraft.block.fission.port;


import com.nred.nuclearcraft.block_entity.fission.PebbleFissionChamberEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionChamberPortEntity;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;

public class FissionChamberPortBlock extends FissionItemPortBlock<FissionChamberPortEntity, PebbleFissionChamberEntity> {
    public FissionChamberPortBlock(MultiblockPartBlock.MultiblockPartProperties<IFissionPartType> properties) {
        super(properties, FissionChamberPortEntity.class);
    }
}