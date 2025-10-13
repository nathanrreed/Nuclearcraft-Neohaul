package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericHorizontalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_HORIZONTAL;

public class TurbineRedstonePortBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericHorizontalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    public TurbineRedstonePortBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction == state.getValue(FACING_HORIZONTAL).getOpposite();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_HORIZONTAL);
    }
}