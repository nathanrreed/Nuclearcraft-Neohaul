package com.nred.nuclearcraft.block.machine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.machine.IMachinePartType;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.registration.BlockRegistration.INVISIBLE;

public class DistillerSieveTrayBlock extends GenericTooltipDeviceBlock<Machine, IMachinePartType> {
    public DistillerSieveTrayBlock(@NotNull MultiblockPartProperties<IMachinePartType> properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(INVISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INVISIBLE);
    }
}