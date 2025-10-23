package com.nred.nuclearcraft.block.tile.dummy;

import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.block_entity.dummy.MachineInterfaceEntity;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.util.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockMachineInterface extends BlockSimpleDummy<MachineInterfaceEntity> {
    public BlockMachineInterface(String name) {
        super(name);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide()) {
            return ItemInteractionResult.CONSUME;
        }
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof MachineInterfaceEntity iface) {
            if (iface.masterPosition == null) {
                iface.findMaster();
            }
            if (iface.masterPosition == null) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            BlockPos masterPos = iface.masterPosition;
            BlockEntity master = level.getBlockEntity(masterPos);
            if (master instanceof ITileFluid tileFluid) {
                boolean accessedTanks = BlockHelper.accessTanks(player, hand, hitResult.getDirection(), tileFluid);
                if (accessedTanks) {
                    if (master instanceof IProcessor<?, ?, ?> processor) {
                        processor.refreshRecipe();
                        processor.refreshActivity();
                    }
                    return ItemInteractionResult.CONSUME;
                }
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCrouching()) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }

        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof MachineInterfaceEntity iface) {
            if (iface.masterPosition == null) {
                iface.findMaster();
            }
            if (iface.masterPosition == null) {
                return InteractionResult.PASS;
            }
            BlockPos masterPos = iface.masterPosition;
            BlockEntity master = level.getBlockEntity(masterPos);
            if (master instanceof ITileGui<?, ?, ?>) {
                return level.getBlockState(masterPos).useWithoutItem(level, player, hitResult.withPosition(masterPos));
            }
        }
        return InteractionResult.CONSUME;
    }
}