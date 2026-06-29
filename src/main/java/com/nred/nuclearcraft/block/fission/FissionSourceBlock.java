package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class FissionSourceBlock extends GenericActiveDirectionalTooltipDeviceBlock<FissionReactor, IFissionPartType> implements IActivatable {
    public FissionSourceBlock(@NotNull MultiblockPartProperties<IFissionPartType> iFissionPartTypeMultiblockPartProperties) {
        super(iFissionPartTypeMultiblockPartProperties);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction side) {
        return side != null;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection()).setValue(ACTIVE, false);
    }
}