package com.nred.nuclearcraft.block.tile.dummy;

import com.nred.nuclearcraft.block.tile.BlockSimpleTile;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockSimpleDummy<TILE extends BlockEntity> extends BlockSimpleTile<TILE> {
    public BlockSimpleDummy(String name) {
        super(name);
    }
}