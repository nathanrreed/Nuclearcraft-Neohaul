package com.nred.nuclearcraft.block.tile;

import com.nred.nuclearcraft.block.NCBlock;
import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.block_entity.ITileInstallable;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public abstract class BlockTile extends NCBlock implements EntityBlock {
    public BlockTile(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Nullable
    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity instanceof MenuProvider ? (MenuProvider) blockentity : null;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof ITileInstallable installable && installable.tryInstall(player, hand, hitResult.getDirection())) {
            return ItemInteractionResult.CONSUME;
        }

        if (player.isCrouching()) {
            return ItemInteractionResult.CONSUME;
        }

        boolean isTileFluid = tile instanceof ITileFluid, isTileGui = tile instanceof ITileGui;
        if (!isTileFluid && !isTileGui) {
            return ItemInteractionResult.FAIL;
        }
        if (isTileFluid && !isTileGui && FluidUtil.getFluidHandler(player.getItemInHand(hand)).isEmpty()) {
            return ItemInteractionResult.FAIL;
        }

        if (tile instanceof ITileFluid tileFluid) {
            if (level.isClientSide()) {
                return ItemInteractionResult.CONSUME;
            }
            boolean accessedTanks = BlockHelper.accessTanks(player, hand, hitResult.getDirection(), tileFluid);
            if (accessedTanks) {
                if (tile instanceof IProcessor<?, ?, ?> processor) {
                    processor.refreshRecipe();
                    processor.refreshActivity();
                }
                return ItemInteractionResult.CONSUME;
            }
        }
        if (isTileGui) {
            if (level.isClientSide()) {
                onGuiOpened(level, pos);
                return ItemInteractionResult.CONSUME;
            } else {
                onGuiOpened(level, pos);
                if (tile instanceof IProcessor<?, ?, ?> processor) {
                    processor.refreshRecipe();
                    processor.refreshActivity();
                }
                player.openMenu(state.getMenuProvider(level, pos), buf -> buf.writeBlockPos(pos));
            }
        } else {
            return ItemInteractionResult.FAIL;
        }

        return ItemInteractionResult.CONSUME;
    }

    public void onGuiOpened(Level level, BlockPos pos) {

    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !keepInventory) {
            BlockEntity tileentity = level.getBlockEntity(pos);

            Container inv = null;
            if (tileentity instanceof Container) {
                inv = (Container) tileentity;
            }

            if (inv != null) {
                dropItems(level, pos, inv);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity tileentity = level.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(id, param);
    }
}