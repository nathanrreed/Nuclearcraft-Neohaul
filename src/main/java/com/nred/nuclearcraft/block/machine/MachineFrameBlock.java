package com.nred.nuclearcraft.block.machine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.IHeatExchangerPartType;
import com.nred.nuclearcraft.registration.BlockRegistration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public class MachineFrameBlock extends GenericTooltipDeviceBlock<HeatExchanger, IHeatExchangerPartType> {
    public MachineFrameBlock(@NotNull MultiblockPartProperties<IHeatExchangerPartType> properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(BlockRegistration.FRAME, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockRegistration.FRAME);
    }
}