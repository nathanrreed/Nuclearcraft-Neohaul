package com.nred.nuclearcraft.block_entity.hx;

import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;

public class HeatExchangerBaffleEntity extends AbstractHeatExchangerEntity {
    public HeatExchangerBaffleEntity(final BlockPos position, final BlockState blockState) {
        super(HX_ENTITY_TYPE.get("baffle").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }
}