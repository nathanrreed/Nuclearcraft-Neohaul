package com.nred.nuclearcraft.block.machine;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.machine.InfiltratorControllerEntity;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.block.multiblock.MultiblockPartBlock;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static com.nred.nuclearcraft.registration.TriggerTypeRegistration.INFILTRATOR_ASSEMBLE_TRIGGER;

public class InfiltratorControllerBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericDirectionalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    public InfiltratorControllerBlock(MultiblockPartBlock.MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_ALL);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos position, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity tile = level.getBlockEntity(position);
            if (tile instanceof InfiltratorControllerEntity controller && controller.isMachineAssembled()) {
                INFILTRATOR_ASSEMBLE_TRIGGER.get().trigger((ServerPlayer) player);
            }
        }

        return super.useWithoutItem(state, level, position, player, hit);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction != null;
    }
}