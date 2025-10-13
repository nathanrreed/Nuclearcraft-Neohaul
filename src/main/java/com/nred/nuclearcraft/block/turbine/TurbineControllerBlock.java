package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.GenericDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.util.BlockHelper;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.registration.BlockRegistration.ACTIVE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class TurbineControllerBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType> extends GenericDirectionalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    public TurbineControllerBlock(MultiblockPartProperties<PartType> properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING_ALL, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE, FACING_ALL);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        BlockHelper.setDefaultFacing(level, pos, state, FACING_ALL);
    }

//    @Override TODO
//    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (hand != InteractionHand.MAIN_HAND || player.isCrouching()) return ItemInteractionResult.FAIL;
//
//        if (!level.isClientSide) {
//            BlockEntity tile = level.getBlockEntity(pos);
//            if (tile instanceof TurbineControllerEntity controller) {
//                if (controller.isMachineAssembled()) {
//                    NCCriterions.TURBINE_ASSEMBLED.trigger((ServerPlayer) player);
//                    controller.openGui((ServerPlayer) player);
//                    return ItemInteractionResult.CONSUME;
//                }
//            }
//        }
//        return rightClickOnPart(world, pos, player, hand, facing, true);
//        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
//    }
//
//    @Override
//    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos position, Player player, BlockHitResult hit) {
//        return super.useWithoutItem(state, world, position, player, hit);
//    }
//
//
//    @Override
//    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
//        if (!level.isClientSide) {
//            BlockEntity tile = level.getBlockEntity(pos);
//            if (tile instanceof TurbineControllerEntity controller) {
//                if (controller.isMachineAssembled()) {
////                    NCCriterions.TURBINE_ASSEMBLED.trigger((EntityPlayerMP) player);
//                    controller.openGui((ServerPlayer) player);
//                    return ItemInteractionResult.CONSUME;
//                }
//            }
//        }
//        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
//    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction != null;
    }
}