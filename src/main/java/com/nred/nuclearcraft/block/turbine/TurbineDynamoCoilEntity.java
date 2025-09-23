package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
import com.nred.nuclearcraft.multiblock.turbine.TurbinePlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_DYNAMO;

public class TurbineDynamoCoilEntity extends TurbineDynamoEntityPart {
    public TurbineDynamoCoilType dynamoCoilType;

    public TurbineDynamoCoilEntity(final BlockPos position, final BlockState blockState, TurbineDynamoCoilType dynamoCoilType) {
        super(TURBINE_DYNAMO.get(), position, blockState);
        this.dynamoCoilType = dynamoCoilType;
        this.placementRule = TurbinePlacement.RULE_MAP.get(dynamoCoilType.getName() + "_coil");
    }

    public static class Variant extends TurbineDynamoCoilEntity {
        protected Variant(final BlockPos position, final BlockState blockState, TurbineDynamoCoilType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }

//        @Override TODO
//        public boolean shouldRefresh(Level level, BlockPos posIn, BlockState oldState, BlockState newState) {
//            return oldState != newState;
//        }
    }

    public static class Magnesium extends TurbineDynamoCoilEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineDynamoCoilType.MAGNESIUM);
        }
    }

    public static class Beryllium extends TurbineDynamoCoilEntity.Variant {
        public Beryllium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineDynamoCoilType.BERYLLIUM);
        }
    }

    public static class Aluminum extends TurbineDynamoCoilEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineDynamoCoilType.ALUMINUM);
        }
    }

    public static class Gold extends TurbineDynamoCoilEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineDynamoCoilType.GOLD);
        }
    }

    public static class Copper extends TurbineDynamoCoilEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineDynamoCoilType.COPPER);
        }
    }

    public static class Silver extends TurbineDynamoCoilEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, TurbineDynamoCoilType.SILVER);
        }
    }
}
