package com.nred.nuclearcraft.block.machine_interface;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.MACHINE_INTERFACE_ENTITY_TYPE;

public class MachineInterfaceEntity extends BlockEntity {
    public BlockPos proxyPos = null;

    public MachineInterfaceEntity(BlockPos pos, BlockState blockState) {
        super(MACHINE_INTERFACE_ENTITY_TYPE.get(), pos, blockState);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            findProxy();
        }
    }

    public boolean findProxy() {
        for (Direction dir : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(dir)) instanceof MenuProvider) { // TODO is this too general?
                proxyPos = worldPosition.relative(dir);
                return true;
            }
        }
        proxyPos = null;
        return false;
    }
}