package com.nred.nuclearcraft.block.turbine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineGlassEntity extends AbstractTurbineEntity {
    public TurbineGlassEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("glass").get(), position, blockState);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}