package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.turbine.ITurbinePartType;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.registration.BlockRegistration.FRAME;

public class TurbineCasingBlock extends GenericTooltipDeviceBlock<Turbine, ITurbinePartType> {
    public TurbineCasingBlock(@NotNull MultiblockPartProperties<ITurbinePartType> properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FRAME, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME);
    }
}