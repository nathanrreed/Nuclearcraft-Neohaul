package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.multiblock.machine.Machine;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FRAME;

public class MachineFrameEntity extends AbstractMachineEntity {
    public MachineFrameEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("frame").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFrame() || super.isGoodForPosition(position, validatorCallback);
    }

    @Override
    public void onPreMachineAssembled(Machine controller) {
        super.onPreMachineAssembled(controller);
        if (getPartPosition().isFrame()) {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FRAME, true), 0);
        }
    }

    @Override
    public void onPreMachineBroken() {
        if (getPartPosition().isFrame()) {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FRAME, false), 0);
        }
        super.onPreMachineBroken();
    }
}