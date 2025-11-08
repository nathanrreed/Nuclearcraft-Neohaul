package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import com.nred.nuclearcraft.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class FissionVentBlock extends GenericActiveDirectionalTooltipDeviceBlock<FissionReactor, IFissionPartType> {
    public FissionVentBlock(@NotNull MultiblockPartProperties<IFissionPartType> iFissionPartTypeMultiblockPartProperties) {
        super(iFissionPartTypeMultiblockPartProperties);
        registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_ALL);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        BlockHelper.setDefaultFacing(level, pos, state, FACING_ALL);
    }
}