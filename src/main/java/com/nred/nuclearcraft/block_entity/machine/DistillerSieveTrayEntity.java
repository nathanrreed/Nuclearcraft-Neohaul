package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.multiblock.machine.Machine;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.INVISIBLE;

public class DistillerSieveTrayEntity extends AbstractMachineEntity {
    public DistillerSieveTrayEntity(BlockPos pos, BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("sieve_tray").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    @Override
    public void onPreMachineAssembled(Machine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(INVISIBLE, true), 2);
        }
    }

    @Override
    public void onPreMachineBroken() {
        if (!level.isClientSide()) {
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(INVISIBLE, false), 2);
        }
        super.onPreMachineBroken();
    }
}