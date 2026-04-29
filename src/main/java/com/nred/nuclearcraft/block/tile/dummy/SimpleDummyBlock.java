package com.nred.nuclearcraft.block.tile.dummy;

import com.nred.nuclearcraft.block.tile.SimpleTileBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SimpleDummyBlock<TILE extends BlockEntity> extends SimpleTileBlock<TILE> {
    public SimpleDummyBlock(String name) {
        super(name);
    }
}