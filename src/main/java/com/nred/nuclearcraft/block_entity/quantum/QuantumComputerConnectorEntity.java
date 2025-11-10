package com.nred.nuclearcraft.block_entity.quantum;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.QUANTUM_ENTITY_TYPE;

public class QuantumComputerConnectorEntity extends AbstractQuantumComputerEntity {
    public QuantumComputerConnectorEntity(final BlockPos position, final BlockState blockState) {
        super(QUANTUM_ENTITY_TYPE.get("quantum_computer_connector").get(), position, blockState);
    }
}