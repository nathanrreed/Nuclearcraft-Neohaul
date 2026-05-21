package com.nred.nuclearcraft.block_entity.distributor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.DISTRIBUTOR_BUFFER_ENTITY_TYPE;

public class DistributorBufferEntity extends AbstractDistributorEntity {
    public DistributorBufferEntity(BlockPos position, BlockState blockState) {
        super(DISTRIBUTOR_BUFFER_ENTITY_TYPE.get(), position, blockState);
    }
}