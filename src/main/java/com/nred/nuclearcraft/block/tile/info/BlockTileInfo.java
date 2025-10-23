package com.nred.nuclearcraft.block.tile.info;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public abstract class BlockTileInfo<TILE extends BlockEntity> {
    public final String name;

    protected final Class<TILE> tileClass;
    protected final BiFunction<BlockPos, BlockState, TILE> tileSupplier;

    public BlockTileInfo(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier) {
        this.name = name;

        this.tileClass = tileClass;
        this.tileSupplier = tileSupplier;
    }

    public BlockEntity getNewTile(BlockPos pos, BlockState state) {
        return tileSupplier.apply(pos, state);
    }
}