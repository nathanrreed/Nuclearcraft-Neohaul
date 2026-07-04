package com.nred.nuclearcraft.compat.create;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static com.nred.nuclearcraft.compat.create.CreateRegistration.CREATE_TURBINE_ROTOR_BEARING_ENTITY;

public class GeneratingKineticTurbineRotorBearingBlock extends DirectionalKineticBlock implements IBE<GeneratingKineticTurbineRotorBearingEntity> {
    public GeneratingKineticTurbineRotorBearingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
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

    VoxelShaper SHAPE = new AllShapes.Builder(Block.box(0,0,0,16,16,14)).forDirectional();

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }
}