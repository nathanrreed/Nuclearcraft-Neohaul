package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericHorizontalTooltipDeviceBlock;
import com.nred.nuclearcraft.block_entity.fission.FissionSourceEntity;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_HORIZONTAL;

public class FissionSourceBlock extends GenericHorizontalTooltipDeviceBlock<FissionReactor, IFissionPartType> {
    public FissionSourceBlock(@NotNull MultiblockPartProperties<IFissionPartType> iFissionPartTypeMultiblockPartProperties) {
        super(iFissionPartTypeMultiblockPartProperties);
        registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_HORIZONTAL, ACTIVE);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction == state.getValue(FACING_HORIZONTAL).getOpposite();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_HORIZONTAL, context.getHorizontalDirection());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPosition, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, neighborPosition, isMoving);
        level.setBlock(pos, state.setValue(ACTIVE, level.hasNeighborSignal(pos)), Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof FissionSourceEntity source) {
            source.onBlockNeighborChanged(state, level, pos, neighborPosition);
        }
    }
}