package com.nred.nuclearcraft.block_entity.turbine;

import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineDynamoCoilEntity extends TurbineDynamoEntityPart {
    public final TurbineDynamoCoilType dynamoCoilType;

    public TurbineDynamoCoilEntity(final BlockPos position, final BlockState blockState, TurbineDynamoCoilType dynamoCoilType) {
        super(TURBINE_ENTITY_TYPE.get("dynamo").get(), position, blockState);
        this.dynamoCoilType = dynamoCoilType;
        this.placementRule = dynamoCoilType.getRule();
        this.conductivity = dynamoCoilType.getConductivity();
    }
}