package com.nred.nuclearcraft.block.hx;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IDynamicState;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerOutletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeSetting;
import com.nred.nuclearcraft.property.SidedEnumProperty;
import it.zerono.mods.zerocore.base.multiblock.part.INeverCauseRenderingSkip;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

import static com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeSetting.CLOSED;

public class HeatExchangerTubeBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericTooltipDeviceBlock<Controller, PartType> implements IDynamicState, INeverCauseRenderingSkip {
    private static boolean placementSneaking = false;
    private static Direction placementSide = Direction.DOWN;

    public HeatExchangerTubeBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DOWN, UP, NORTH, SOUTH, WEST, EAST);
    }

    public static final SidedEnumProperty<HeatExchangerTubeSetting> DOWN = tubeSide("down", Direction.DOWN);
    public static final SidedEnumProperty<HeatExchangerTubeSetting> UP = tubeSide("up", Direction.UP);
    public static final SidedEnumProperty<HeatExchangerTubeSetting> NORTH = tubeSide("north", Direction.NORTH);
    public static final SidedEnumProperty<HeatExchangerTubeSetting> SOUTH = tubeSide("south", Direction.SOUTH);
    public static final SidedEnumProperty<HeatExchangerTubeSetting> WEST = tubeSide("west", Direction.WEST);
    public static final SidedEnumProperty<HeatExchangerTubeSetting> EAST = tubeSide("east", Direction.EAST);

    public static SidedEnumProperty<HeatExchangerTubeSetting> tubeSide(String name, Direction facing) {
        return SidedEnumProperty.create(name, HeatExchangerTubeSetting.class, facing);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null) {
            placementSneaking = context.getPlayer().isCrouching();
            placementSide = context.getClickedFace().getOpposite();
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof HeatExchangerTubeEntity tube) {
            boolean update = false;

            BlockPos otherPos = pos.relative(placementSide);
            if (level.getBlockEntity(otherPos) instanceof HeatExchangerTubeEntity other) {
                if (placementSneaking) {
                    tube.setTubeSettings(other.settings);
                    for (int i = 0; i < 6; ++i) {
                        Direction side = Direction.values()[i], opposite = side.getOpposite();
                        if (side.equals(placementSide)) {
                            tube.setTubeSetting(placementSide, other.getTubeSetting(opposite));
                        } else if (other.settings[i].isOpen() && level.getBlockEntity(pos.relative(side)) instanceof HeatExchangerTubeEntity offsetTube && level.getBlockEntity(otherPos.relative(side)) instanceof HeatExchangerTubeEntity offsetOther && offsetOther.getTubeSetting(opposite).isOpen()) {
                            offsetTube.setTubeSettingOpen(opposite, true);
                            offsetTube.markDirtyAndNotify(true);
                        }
                    }
                } else {
                    other.setTubeSettingOpen(placementSide.getOpposite(), true);
                    tube.setTubeSetting(placementSide, other.getTubeSetting(placementSide.getOpposite()));
                    other.markDirtyAndNotify(true);
                }
                update = true;
            }

            for (Direction side : Arrays.asList(placementSide, placementSide.getOpposite())) {
                BlockEntity otherTile = level.getBlockEntity(pos.relative(side));
                if (otherTile instanceof HeatExchangerInletEntity || otherTile instanceof HeatExchangerOutletEntity) {
                    tube.setTubeSetting(side, HeatExchangerTubeSetting.OPEN);
                    update = true;
                    break;
                }
            }

            if (update) {
                tube.markDirtyAndNotify(true);
            }
        }
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        for (Direction side : Direction.values()) {
            if (level.getBlockEntity(pos.relative(side)) instanceof HeatExchangerTubeEntity other && other.getTubeSetting(side.getOpposite()).isOpen()) {
                other.setTubeSettingOpen(side.getOpposite(), false);
                other.markDirtyAndNotify(true);
            }
        }

        super.destroy(level, pos, state);
    }

    // Rendering

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(level, pos, true);
    }

    // Bounding Box

    private static final VoxelShape CENTER_SHAPE = Block.box(2, 2, 2, 14, 14, 14);

    public static final Map<Direction, VoxelShape> SIDE_SHAPE = Map.of(
            Direction.DOWN, Block.box(2, 0, 2, 14, 2, 14),
            Direction.UP, Block.box(2, 14, 2, 14, 16, 14),
            Direction.NORTH, Block.box(2, 2, 0, 14, 14, 2),
            Direction.SOUTH, Block.box(2, 2, 14, 14, 14, 16),
            Direction.WEST, Block.box(0, 2, 2, 2, 14, 14),
            Direction.EAST, Block.box(14, 2, 2, 16, 14, 14)
    );

    public static final Map<Direction, VoxelShape> SIDE_BAFFLE_SHAPE = Map.of(
            Direction.DOWN, Block.box(0, 0, 0, 16, 2, 16),
            Direction.UP, Block.box(0, 14, 0, 16, 16, 16),
            Direction.NORTH, Block.box(0, 0, 0, 16, 16, 2D),
            Direction.SOUTH, Block.box(0, 0, 14, 16, 16, 16),
            Direction.WEST, Block.box(0, 0, 0, 2, 16, 16),
            Direction.EAST, Block.box(14, 0, 0, 16, 16, 16)
    );

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) { // TODO add caching
        if (state.getBlock() != this) {
            return super.getShape(state, level, pos, context);
        }

        return getShape(level, pos, false);
    }

    private VoxelShape getShape(BlockGetter level, BlockPos pos, boolean collision) {
        VoxelShape boundingBox = collision ? Shapes.create(CENTER_SHAPE.bounds().inflate(0.08)) : CENTER_SHAPE; // Shape was clipping for collision
        if (level.getBlockEntity(pos) instanceof HeatExchangerTubeEntity tube) {
            for (Direction dir : Direction.values()) {
                HeatExchangerTubeSetting setting = tube.getTubeSetting(dir);
                if (setting != CLOSED) {
                    boundingBox = Shapes.joinUnoptimized(boundingBox, setting.isBaffle() ? SIDE_BAFFLE_SHAPE.get(dir) : SIDE_SHAPE.get(dir), BooleanOp.OR);
                }
            }
        }

        return boundingBox.optimize();
    }
}