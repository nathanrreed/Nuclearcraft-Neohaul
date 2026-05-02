package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block_entity.fission.SolidFissionCellEntity;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.IFissionPartType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class SolidFissionCellBlock extends GenericTooltipDeviceBlock<FissionReactor, IFissionPartType> {
    public SolidFissionCellBlock(MultiblockPartProperties<IFissionPartType> properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof SolidFissionCellEntity cell) {
            FissionReactor reactor = cell.getMultiblockController().orElse(null);
            if (reactor != null) {
                ItemStack heldStack = player.getItemInHand(hand);
                if (cell.canModifyFilter(0) && cell.getInventoryStacks().get(0).isEmpty() && !ItemStack.isSameItem(heldStack, cell.getFilterStacks().get(0)) && cell.isItemValidForSlotInternal(0, heldStack)) {
                    player.sendSystemMessage(Component.translatable(MODID + ".message.filter", heldStack.getDisplayName().copy().withStyle(ChatFormatting.BOLD)));

                    ItemStack filter = heldStack.copy();
                    filter.setCount(1);
                    cell.getFilterStacks().set(0, filter);
                    cell.onFilterChanged(0);
                    return ItemInteractionResult.CONSUME;
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}