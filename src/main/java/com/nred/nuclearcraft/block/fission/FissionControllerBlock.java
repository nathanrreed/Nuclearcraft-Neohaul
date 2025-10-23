package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.fission.SaltFissionControllerEntity;
import com.nred.nuclearcraft.block_entity.fission.SolidFissionControllerEntity;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static com.nred.nuclearcraft.registration.TriggerTypeRegistration.SALT_FISSION_ASSEMBLE_TRIGGER;
import static com.nred.nuclearcraft.registration.TriggerTypeRegistration.SOLID_FISSION_ASSEMBLE_TRIGGER;

public class FissionControllerBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericDirectionalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    public FissionControllerBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(ACTIVE, false));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos position, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity tile = level.getBlockEntity(position);
            if (tile instanceof SolidFissionControllerEntity controller && controller.isMachineAssembled()) {
                SOLID_FISSION_ASSEMBLE_TRIGGER.get().trigger((ServerPlayer) player);
            } else if (tile instanceof SaltFissionControllerEntity controller && controller.isMachineAssembled()) {
                SALT_FISSION_ASSEMBLE_TRIGGER.get().trigger((ServerPlayer) player);
            }
        }

        return super.useWithoutItem(state, level, position, player, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_ALL);
    }
}