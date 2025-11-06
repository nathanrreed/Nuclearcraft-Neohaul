package com.nred.nuclearcraft.block.tile;

import com.nred.nuclearcraft.block.tile.info.BlockSimpleTileInfo;
import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockSimpleTile<TILE extends BlockEntity> extends BlockTile {
    protected final BlockSimpleTileInfo<TILE> tileInfo;
    private final boolean hasTicker;

    public BlockSimpleTile(String name, boolean hasTicker) {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
        tileInfo = TileInfoHandler.getBlockSimpleTileInfo(name);
        this.hasTicker = hasTicker;
    }

    public BlockSimpleTile(String name) {
        this(name, true);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return tileInfo.getNewTile(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide || !hasTicker) return null;
        return (level1, pos, state1, blockEntity) -> ((ITickable) blockEntity).update();
    }
}