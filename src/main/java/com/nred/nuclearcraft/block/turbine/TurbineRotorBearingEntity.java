package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;

public class TurbineRotorBearingEntity extends AbstractTurbineEntity {
    public TurbineRotorBearingEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("rotor_bearing").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace() || super.isGoodForPosition(position, validatorCallback);
    }

    public void onBearingFailure(Iterator<TurbineRotorBearingEntity> bearingIterator) {
        Turbine turbine = getMultiblockController().orElse(null);
        if (turbine != null) {
            bearingIterator.remove();
            level.removeBlockEntity(worldPosition);
            level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 3);
            level.explode(null, worldPosition.getX() + turbine.rand.nextDouble() - 0.5D, worldPosition.getY() + turbine.rand.nextDouble() - 0.5D, worldPosition.getZ() + turbine.rand.nextDouble() - 0.5D, 4F, Level.ExplosionInteraction.NONE);
        }
    }
}
