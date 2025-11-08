package com.nred.nuclearcraft.block_entity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;

public class MachineGlassEntity extends AbstractMachineEntity {
    public MachineGlassEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("glass").get(), position, blockState);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}