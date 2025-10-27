package com.nred.nuclearcraft.block.tile;

import com.nred.nuclearcraft.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_HORIZONTAL;

public abstract class BlockSidedTile extends BlockTile {
    public BlockSidedTile(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING_HORIZONTAL, Direction.NORTH));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        BlockHelper.setDefaultFacing(level, pos, state, FACING_HORIZONTAL);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_HORIZONTAL);
    }
}