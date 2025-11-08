package com.nred.nuclearcraft.block_entity.machine;


import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.DISTILLER_ENTITY_TYPE;

public class TileDistillerLiquidDistributor extends AbstractMachineEntity {
    public TileDistillerLiquidDistributor(BlockPos pos, BlockState blockState) {
        super(DISTILLER_ENTITY_TYPE.get("liquid_distributor").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }
}