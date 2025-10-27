package com.nred.nuclearcraft.block.tile.info;

import com.nred.nuclearcraft.menu.MenuFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class BlockSimpleTileInfo<TILE extends BlockEntity> extends BlockTileInfo<TILE> {
    public BlockSimpleTileInfo(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier) {
        super(name, tileClass, tileSupplier);
    }
}