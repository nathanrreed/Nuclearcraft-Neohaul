package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.multiblock.machine.Machine;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.ELECTROLYZER_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class TileElectrolyzerAnodeTerminal extends AbstractMachineEntity {
    public TileElectrolyzerAnodeTerminal(BlockPos pos, BlockState blockState) {
        super(ELECTROLYZER_ENTITY_TYPE.get("anode_terminal").get(), pos, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(Machine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }
}