package com.nred.nuclearcraft.compat.create;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.nred.nuclearcraft.compat.create.CreateRegistration.CREATE_TURBINE_ROTOR_BEARING_ENTITY;

public class GeneratingKineticTurbineRotorBearingBlock extends DirectionalKineticBlock implements IBE<GeneratingKineticTurbineRotorBearingEntity>, IRotate {
    public GeneratingKineticTurbineRotorBearingBlock(Properties properties) {
        super(properties);
    }

    // IRotate:
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public Class<GeneratingKineticTurbineRotorBearingEntity> getBlockEntityClass() {
        return GeneratingKineticTurbineRotorBearingEntity.class;
    }

    @Override
    public BlockEntityType<? extends GeneratingKineticTurbineRotorBearingEntity> getBlockEntityType() {
        return CREATE_TURBINE_ROTOR_BEARING_ENTITY.get();
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}