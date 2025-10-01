package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericHorizontalTooltipDeviceBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class TurbineRedstonePortBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericHorizontalTooltipDeviceBlock<Controller, PartType> {
    public static final BooleanProperty REDSTONE_ON = BooleanProperty.create(MODID + "_redstone_on");

    public TurbineRedstonePortBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(REDSTONE_ON, false));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction == state.getValue(FACING).getOpposite();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPosition, boolean isMoving) {
        level.setBlock(pos, state.setValue(REDSTONE_ON, level.hasNeighborSignal(pos)), Block.UPDATE_ALL);
        if (level.getBlockEntity(pos) instanceof TurbineRedstonePortEntity controller) {
            controller.getMultiblockController().get().setIsTurbineOn();
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REDSTONE_ON, FACING);
    }
}