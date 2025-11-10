package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block.GenericHorizontalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.fission.port.FissionPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.AXIS_ALL;

public abstract class FissionPortBlock<PORT extends FissionPortEntity<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends GenericHorizontalTooltipDeviceBlock<FissionReactor, IFissionPartType> implements IActivatable {
    protected final Class<PORT> portClass;

    public FissionPortBlock(MultiblockPartProperties<IFissionPartType> properties, Class<PORT> portClass) {
        super(properties);
        this.portClass = portClass;
        registerDefaultState(this.defaultBlockState().setValue(AXIS_ALL, Direction.Axis.Z).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_ALL, ACTIVE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS_ALL, context.getHorizontalDirection().getAxis());
    }
}