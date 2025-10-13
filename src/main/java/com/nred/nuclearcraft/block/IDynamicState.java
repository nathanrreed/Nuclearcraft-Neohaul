package com.nred.nuclearcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public interface IDynamicState {
    default <T extends Enum<T> & StringRepresentable> void setProperty(EnumProperty<T> property, T value, BlockEntity tile) {
        Level level = tile.getLevel();
        BlockPos pos = tile.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if (!level.isClientSide && getClass().isInstance(state.getBlock())) {
            if (!value.equals(state.getValue(property))) {
                level.setBlock(pos, state.setValue(property, value), 2);
            }
        }
    }
}