package com.nred.nuclearcraft.block.turbine;

import it.zerono.mods.zerocore.lib.data.nbt.INestedSyncableEntity;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_CASING;

public class TurbineCasingEntity extends AbstractTurbineEntity implements INestedSyncableEntity {
    public TurbineCasingEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_CASING.get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFrame() || super.isGoodForPosition(position, validatorCallback);
    }

    @Override
    public Optional<ISyncableEntity> getNestedSyncableEntity() { //TODO REMOVE?
        return this.getMultiblockController().map(c -> c);
    }
}
