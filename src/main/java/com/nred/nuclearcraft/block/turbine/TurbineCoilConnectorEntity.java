package com.nred.nuclearcraft.block.turbine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_COIL_CONNECTOR;

public class TurbineCoilConnectorEntity extends TurbineDynamoEntityPart {
    public TurbineCoilConnectorEntity(BlockPos position, BlockState blockState) {
        super(TURBINE_COIL_CONNECTOR.get(), position, blockState);
        conductivity = null;

        ruleID = "connector";
    }
}
