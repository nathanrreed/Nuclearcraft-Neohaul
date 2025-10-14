package com.nred.nuclearcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_HORIZONTAL;

public interface ITile {
    BlockEntity getTile();

    Level getTileWorld();

    BlockPos getTilePos();

    Block getTileBlockType();

    default ItemStack getTileBlockStack() {
        return new ItemStack(getTileBlockType(), 1);
    }

    default Component getTileBlockDisplayName() {
        return Component.translatable(getTileBlockStack().getDescriptionId());
    }

    default Direction getFacingHorizontal() {
        return getTileBlockType().defaultBlockState().getValue(FACING_HORIZONTAL); // TODO is this right?
    }

    default BlockState getBlockState(BlockPos pos) {
        return getTileWorld().getBlockState(pos);
    }

    default Block getBlock(BlockPos pos) {
        return getBlockState(pos).getBlock();
    }

//    IRadiationSource getRadiationSource(); TODO

    default boolean shouldSaveRadiation() {
        return true;
    }

    default void setActivity(boolean isActive) {
        setState(isActive, getTile());
    }

    @Deprecated
    default void setState(boolean isActive, BlockEntity tile) {
        if (getTileBlockType() instanceof IActivatable block) {
            block.setActivity(isActive, tile);
        }
    }

    default <T extends Enum<T> & StringRepresentable> void setProperty(EnumProperty<T> property, T value) {
        if (getTileBlockType() instanceof IDynamicState block) {
            block.setProperty(property, value, getTile());
        }
    }

    default void onBlockNeighborChanged(BlockState state, Level world, BlockPos pos, BlockPos fromPos) {
        refreshIsRedstonePowered(world, pos);
    }

    default boolean isUsableByPlayer(Player player) {
        return player.distanceToSqr(getTilePos().getX() + 0.5D, getTilePos().getY() + 0.5D, getTilePos().getZ() + 0.5D) <= 64D;
    }

    // Redstone

    default boolean checkIsRedstonePowered(Level world, BlockPos pos) {
        return world.hasNeighborSignal(pos) || isWeaklyPowered(world, pos);
    }

    default int[] weakSidesToCheck(Level world, BlockPos pos) {
        return new int[]{};
    }

    default boolean isWeaklyPowered(Level world, BlockPos pos) {
        for (int i : weakSidesToCheck(world, pos)) {
            Direction side = Direction.from3DDataValue(i);
            BlockPos offPos = pos.relative(side);
            if (world.getSignal(offPos, side) > 0) {
                return true;
            } else {
                BlockState state = world.getBlockState(offPos);
                if (state.getBlock() == Blocks.REDSTONE_WIRE && state.getValue(RedStoneWireBlock.POWER) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    default void refreshIsRedstonePowered(Level world, BlockPos pos) {
        setIsRedstonePowered(checkIsRedstonePowered(world, pos));
    }

    boolean getIsRedstonePowered();

    void setIsRedstonePowered(boolean isRedstonePowered);

    boolean getAlternateComparator();

    void setAlternateComparator(boolean alternate);

    boolean getRedstoneControl();

    void setRedstoneControl(boolean redstoneControl);

    // State Updating

    void markTileDirty();

    default void notifyBlockUpdate() {
        BlockState state = getTileWorld().getBlockState(getTilePos());
        getTileWorld().sendBlockUpdated(getTilePos(), state, state, 3);
    }

    default void notifyNeighborsOfStateChange() {
        Level world = getTileWorld();
        BlockPos pos = getTilePos();

        world.updateNeighborsAt(pos, getTileBlockType());

//		if (ModCheck.mekanismLoaded()) { TODO
//			for (Direction side : Direction.values()) {
//				MekanismHelper.updateTransmitter(world, pos, side);
//			}
//		}
    }

    /**
     * Call after markDirty if comparators might need to know about the changes made to the TE
     */
    default void updateComparatorOutputLevel() {
        getTileWorld().updateNeighbourForOutputSignal(getTilePos(), getTileBlockType());
    }

    default void markDirtyAndNotify(boolean notifyNeighbors) {
        markTileDirty();
        notifyBlockUpdate();
        if (notifyNeighbors) {
            notifyNeighborsOfStateChange();
        }
    }

    @Deprecated
    default void markDirtyAndNotify() {
        markDirtyAndNotify(true);
    }

//	// Capabilities TODO
//

    /**
     * Use when the capability provider side argument must be non-null
     */
    default @Nonnull Direction nonNullSide(@Nullable Direction side) {
        return side == null ? Direction.DOWN : side;
    }
//
//	default <T> @Nullable T getCapabilitySafe(Capability<T> capability, TileEntity tile, Direction side) {
//		if (tile == null) {
//			return null;
//		}
//
//		if (tile.hasCapability(capability, side)) {
//			return tile.getCapability(capability, side);
//		}
//		else {
//			return null;
//		}
//	}
//
//	default <T> @Nullable T getCapabilitySafe(Capability<T> capability, BlockPos pos, Direction side) {
//		return getCapabilitySafe(capability, getTileWorld().getTileEntity(pos), side);
//	}
//
//	default <T> @Nullable T getAdjacentCapabilitySafe(Capability<T> capability, @Nonnull Direction side) {
//		return getCapabilitySafe(capability, getTilePos().offset(side), side.getOpposite());
//	}
//
//    // HWYLA
//
//    default @Nonnull List<String> addToHWYLATooltip(@Nonnull List<String> tooltip) {
//        return tooltip;
//    }
}