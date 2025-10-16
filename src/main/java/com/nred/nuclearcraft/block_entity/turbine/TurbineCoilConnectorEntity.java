package com.nred.nuclearcraft.block_entity.turbine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineCoilConnectorEntity extends TurbineDynamoEntityPart {
    public TurbineCoilConnectorEntity(BlockPos position, BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("coil_connector").get(), position, blockState);
        conductivity = null;

        ruleID = "connector";
    }
}