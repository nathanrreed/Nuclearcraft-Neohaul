package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.IBlockRotorBlade;
import com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import it.zerono.mods.zerocore.base.multiblock.part.INeverCauseRenderingSkip;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.multiblock.turbine.TurbineRotorBladeUtil.DIR;


public class TurbineRotorBladeBlock extends MultiblockPartBlock<Turbine, ITurbinePartType> implements INeverCauseRenderingSkip, IBlockRotorBlade {
    protected static final VoxelShape X_AXIS_AABB = Block.box(0, 7, 2, 16, 9, 14);
    protected static final VoxelShape Y_AXIS_AABB = Block.box(2, 0, 7, 14, 16, 9);
    protected static final VoxelShape Z_AXIS_AABB = Block.box(2, 7, 0, 14, 9, 16);

    public TurbineRotorBladeBlock(@NotNull MultiblockPartProperties<ITurbinePartType> iTurbinePartTypeMultiblockPartProperties) {
        super(iTurbinePartTypeMultiblockPartProperties.setBlockProperties(Properties.of().noOcclusion().isSuffocating((s, g, p) -> false)));
        registerDefaultState(this.defaultBlockState().setValue(DIR, TurbinePartDir.Y));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIR);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        return defaultBlockState().setValue(DIR, TurbinePartDir.fromFacingAxis(direction.getAxis()));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getBlock() != this) {
            return super.getShape(state, level, pos, context);
        }

        return switch (state.getValue(DIR)) {
            case X -> X_AXIS_AABB;
            case Z -> Z_AXIS_AABB;
            case Y -> Y_AXIS_AABB;
            default -> super.getShape(state, level, pos, context);
        };
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }
}