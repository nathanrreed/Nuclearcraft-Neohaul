package com.nred.nuclearcraft.block_entity.machine;

import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_ENTITY_TYPE;

public class MachineRedstonePortEntity extends AbstractMachineEntity {
    public MachineRedstonePortEntity(final BlockPos position, final BlockState blockState) {
        super(MACHINE_ENTITY_TYPE.get("redstone_port").get(), position, blockState);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onBlockNeighborChanged(BlockState state, Level worldIn, BlockPos posIn, BlockPos fromPos) {
        super.onBlockNeighborChanged(state, worldIn, posIn, fromPos);
        setActivity(getIsRedstonePowered());
    }
}