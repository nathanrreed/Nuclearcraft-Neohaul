package com.nred.nuclearcraft.block.processor;

import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block.tile.BlockSidedTile;
import com.nred.nuclearcraft.block_entity.processor.EnergyProcessorEntity;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorBlockInfo;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_HORIZONTAL;

public class BlockProcessor<TILE extends BlockEntity> extends BlockSidedTile implements IActivatable {
    protected final ProcessorBlockInfo<TILE> tileInfo;

    public BlockProcessor(String name) {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
        tileInfo = TileInfoHandler.getProcessorBlockInfo(name);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return tileInfo.getNewTile(pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite()).setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_HORIZONTAL, ACTIVE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(ACTIVE)) {
            return;
        }
        for (String particle : tileInfo.particles) {
            BlockHelper.spawnParticleOnProcessor(state, level, pos, random, state.getValue(FACING_HORIZONTAL), particle);
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : (level1, pos, state1, blockEntity) -> ((EnergyProcessorEntity<?, ?>) blockEntity).update();
    }
}