package com.nred.nuclearcraft.block.machine;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IDynamicState;
import com.nred.nuclearcraft.multiblock.machine.IMachinePartType;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class ElectrolyzerAnodeTerminalBlock extends GenericTooltipDeviceBlock<Machine, IMachinePartType> implements IDynamicState {
    public ElectrolyzerAnodeTerminalBlock(@NotNull MultiblockPartProperties<IMachinePartType> properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(FACING_ALL, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_ALL);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() == null) return defaultBlockState().setValue(FACING_ALL, Direction.DOWN);
        return defaultBlockState().setValue(FACING_ALL, context.getPlayer().position().y + context.getPlayer().getEyeHeight() > context.getClickedPos().getY() + 0.5D ? Direction.DOWN : Direction.UP);
    }
}