package com.nred.nuclearcraft.block.turbine;

import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_CASING;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_GLASS;

public class TurbineGlassEntity extends AbstractTurbineEntity {
    public TurbineGlassEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_GLASS.get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFrame() || super.isGoodForPosition(position, validatorCallback);
    }
}