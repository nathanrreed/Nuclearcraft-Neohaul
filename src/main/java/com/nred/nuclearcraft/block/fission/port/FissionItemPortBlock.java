package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block_entity.fission.port.FissionItemPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import com.nred.nuclearcraft.util.NCInventoryHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class FissionItemPortBlock<PORT extends FissionItemPortEntity<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET> & ITileFilteredInventory> extends FissionPortBlock<PORT, TARGET> {
    public FissionItemPortBlock(MultiblockPartProperties<IFissionPartType> properties, Class<PORT> portClass) {
        super(properties, portClass);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (portClass.isInstance(tile)) {
                PORT port = portClass.cast(tile);
                FissionReactor reactor = port.getMultiblockController().orElse(null);
                if (reactor != null) {
                    ItemStack heldStack = player.getItemInHand(hand);
                    if (port.canModifyFilter(0) && port.getInventoryStacks().get(0).isEmpty() && !ItemStack.isSameItem(heldStack, port.getFilterStacks().get(0)) && port.isItemValidForSlotInternal(0, heldStack)) {
                        player.sendSystemMessage(Component.translatable(MODID + ".message.filter", heldStack.getDisplayName().copy().withStyle(ChatFormatting.BOLD)));
                        ItemStack filter = heldStack.copy();
                        filter.setCount(1);
                        port.getFilterStacks().set(0, filter);
                        port.onFilterChanged(0);
                        return ItemInteractionResult.CONSUME;
                    }
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !keepInventory) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (portClass.isInstance(tile)) {
                PORT port = portClass.cast(tile);
                NCInventoryHelper.dropInventoryItems(level, pos, port.getInventoryStacksInternal());
                // world.updateComparatorOutputLevel(pos, this);
                // FissionReactor reactor = port.getMultiblock();
                // world.removeTileEntity(pos);
				/*if (reactor != null) {
					reactor.getLogic().refreshPorts();
				}*/
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}