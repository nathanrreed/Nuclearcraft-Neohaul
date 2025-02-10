package com.nred.nuclearcraft.block.collector;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class CollectorEntity extends BlockEntity {
    public MACHINE_LEVEL machineLevel;
    public CollectorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, MACHINE_LEVEL level) {
        super(type, pos, blockState);
        this.machineLevel = level;
    }

    public abstract double getAmountPerTick(Level level);
    public abstract int getMax();

    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
    }
}

