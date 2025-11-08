package com.nred.nuclearcraft.block.machine;

import com.nred.nuclearcraft.block.GenericHorizontalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IDynamicState;
import com.nred.nuclearcraft.block_entity.machine.MachineProcessPortEntity;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.property.MachinePortSorption;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockRegistration.AXIS_ALL;
import static com.nred.nuclearcraft.registration.BlockRegistration.MACHINE_PORT_SORPTION;

public class MachineProcessPortBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericHorizontalTooltipDeviceBlock<Controller, PartType> implements IDynamicState {
    public MachineProcessPortBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS_ALL, Direction.Axis.Z).setValue(MACHINE_PORT_SORPTION, MachinePortSorption.ITEM_IN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_ALL, MACHINE_PORT_SORPTION);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(AXIS_ALL, context.getNearestLookingDirection().getAxis());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!MultitoolItem.isMultitool(player.getItemInHand(hand))) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof MachineProcessPortEntity port) {
                if (!level.isClientSide()) {
                    if (port.getMultiblockController().isPresent()) {
                        player.sendSystemMessage(Component.translatable(MODID + ".tooltip.port_toggle", port.getMachinePortSettingString()));
                    }
                }
                return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}