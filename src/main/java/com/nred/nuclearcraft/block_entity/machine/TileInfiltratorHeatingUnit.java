package com.nred.nuclearcraft.block_entity.machine;

import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.INFILTRATOR_ENTITY_TYPE;

public class TileInfiltratorHeatingUnit extends AbstractMachineEntity {
    public TileInfiltratorHeatingUnit(BlockPos pos, BlockState blockState) {
        super(INFILTRATOR_ENTITY_TYPE.get("heating_unit").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }
}