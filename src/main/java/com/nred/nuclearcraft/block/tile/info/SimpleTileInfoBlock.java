package com.nred.nuclearcraft.block.tile.info;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class SimpleTileInfoBlock<TILE extends BlockEntity> extends TileInfoBlock<TILE> {
    public SimpleTileInfoBlock(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier) {
        super(name, tileClass, tileSupplier);
    }
}