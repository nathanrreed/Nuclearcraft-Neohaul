package com.nred.nuclearcraft.block.collector;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public abstract class Collector<T extends Collector<?>> extends BaseEntityBlock {
    public MACHINE_LEVEL level;

    public Collector(Properties properties, String level) {
        this(properties, MACHINE_LEVEL.valueOf(level));
    }

    public Collector(Properties properties, MACHINE_LEVEL level) {
        super(properties);
        this.level = level;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}