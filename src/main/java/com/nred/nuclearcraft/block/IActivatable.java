package com.nred.nuclearcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;

public interface IActivatable extends IDynamicState {
    default void setActivity(boolean isActive, BlockEntity tile) {
        Level level = tile.getLevel();
        BlockPos pos = tile.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!level.isClientSide && getClass().isInstance(state.getBlock())) {
            if (isActive != state.getValue(ACTIVE)) {
                level.setBlock(pos, state.setValue(ACTIVE, isActive), 2);
            }
        }
    }
}