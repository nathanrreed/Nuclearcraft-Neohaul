package com.nred.nuclearcraft.block.quantum;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block_entity.quantum.QuantumComputerQubitEntity;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.quantum.IQuantumComputerPartType;
import com.nred.nuclearcraft.multiblock.quantum.QuantumComputer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class QuantumComputerQubitBlock extends GenericTooltipDeviceBlock<QuantumComputer, IQuantumComputerPartType> {
    public QuantumComputerQubitBlock(@NotNull MultiblockPartProperties<IQuantumComputerPartType> properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!MultitoolItem.isMultitool(player.getItemInHand(hand))) {
            if (level.getBlockEntity(pos) instanceof QuantumComputerQubitEntity qubit) {
                if (!level.isClientSide()) {
                    player.sendSystemMessage(Component.translatable(MODID + ".multitool.quantum_computer.qubit_id", qubit.id));
                }
                return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction != null;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof QuantumComputerQubitEntity qubit) {
            return qubit.redstone ? 15 : 0;
        }
        return 0;
    }
}