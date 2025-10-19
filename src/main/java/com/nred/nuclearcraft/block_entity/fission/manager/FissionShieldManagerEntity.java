package com.nred.nuclearcraft.block_entity.fission.manager;

import com.nred.nuclearcraft.block_entity.fission.FissionShieldEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;

public class FissionShieldManagerEntity extends FissionManagerEntity<FissionShieldManagerEntity, FissionShieldEntity> {
    public FissionShieldManagerEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("shield_manager").get(), position, blockState, FissionShieldManagerEntity.class);
    }

    @Override
    public int[] weakSidesToCheck(Level world, BlockPos pos) {
        return new int[]{2, 3, 4, 5};
    }

    @Override
    public String getManagerType() {
        return "fissionShieldManager";
    }

    @Override
    public Class<FissionShieldEntity> getListenerClass() {
        return FissionShieldEntity.class;
    }

    @Override
    public boolean isManagerActive() {
        return getIsRedstonePowered();
    }
}