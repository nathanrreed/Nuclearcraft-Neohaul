package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.block_entity.ITile;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.render.BlockHighlightTracker;
import com.nred.nuclearcraft.util.BlockHelper;
import it.zerono.mods.zerocore.base.multiblock.part.GenericDeviceBlock;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.lib.multiblock.validation.ValidationError;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;

import java.util.Optional;

public class GenericTooltipDeviceBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericDeviceBlock<Controller, PartType> {
    public GenericTooltipDeviceBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos blockPosition, Block block, BlockPos neighborPosition, boolean isMoving) {
        super.neighborChanged(state, world, blockPosition, block, neighborPosition, isMoving);
        BlockEntity tile = world.getBlockEntity(blockPosition);
        if (tile instanceof ITile t) {
            t.onBlockNeighborChanged(state, tile.getLevel(), blockPosition, neighborPosition);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof ITileFluid tileFluid && FluidUtil.getFluidHandler(player.getItemInHand(hand)).isPresent()) {
            if (BlockHelper.accessTanks(player, hand, hitResult.getDirection(), tileFluid)) {
                return ItemInteractionResult.CONSUME;
            }
        }
        if (!level.isClientSide && player.getItemInHand(hand).isEmpty()) {
            Optional<IMultiblockPart<Controller>> part = WorldHelper.getMultiblockPartFrom(level, pos);
            if (player.isCrouching()) {
                Optional<Controller> controller = part.flatMap(IMultiblockPart::getMultiblockController);
                ValidationError error = controller.isEmpty() ? ValidationError.VALIDATION_ERROR_NOT_CONNECTED : controller.filter(IMultiblockValidator::hasLastError).flatMap(IMultiblockValidator::getLastError).orElse((ValidationError) null);
                if (null != error && error.getPosition() != null) {
                    BlockHighlightTracker.sendPacket((ServerPlayer) player, error.getPosition(), 5000);
                }
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}