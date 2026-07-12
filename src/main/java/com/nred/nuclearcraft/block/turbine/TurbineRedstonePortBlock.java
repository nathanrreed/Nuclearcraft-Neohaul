package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericActiveTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.multiblock.turbine.ITurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TurbineRedstonePortBlock extends GenericActiveTooltipDeviceBlock<Turbine, ITurbinePartType> implements IActivatable {
    public TurbineRedstonePortBlock(MultiblockPartProperties<ITurbinePartType> properties) {
        super(properties);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction != null;
    }
}