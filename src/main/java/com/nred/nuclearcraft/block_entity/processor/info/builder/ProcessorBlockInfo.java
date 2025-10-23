package com.nred.nuclearcraft.block_entity.processor.info.builder;

import com.nred.nuclearcraft.block.tile.info.BlockTileInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;

public class ProcessorBlockInfo<TILE extends BlockEntity> extends BlockTileInfo<TILE> {
    public final List<String> particles;

    public ProcessorBlockInfo(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, List<String> particles) {
        super(name, tileClass, tileSupplier);
        this.particles = particles;
    }
}