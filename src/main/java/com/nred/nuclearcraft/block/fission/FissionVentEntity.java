package com.nred.nuclearcraft.block.fission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionVentEntity extends AbstractFissionEntity {
    public FissionVentEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("vent").get(), position, blockState);
    }
}