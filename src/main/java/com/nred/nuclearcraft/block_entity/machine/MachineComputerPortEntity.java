package com.nred.nuclearcraft.block_entity.machine;

import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;

public class MachineComputerPortEntity extends AbstractMachineEntity {
    public MachineComputerPortEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("computer_port").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }
}