package com.nred.nuclearcraft.block_entity.hx;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;

public class HeatExchangerGlassEntity extends AbstractHeatExchangerEntity {
    public HeatExchangerGlassEntity(final BlockPos position, final BlockState blockState) {
        super(HX_ENTITY_TYPE.get("glass").get(), position, blockState);
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
}