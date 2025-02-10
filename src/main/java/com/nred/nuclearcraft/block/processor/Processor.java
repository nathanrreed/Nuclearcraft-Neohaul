package com.nred.nuclearcraft.block.processor;

import com.nred.nuclearcraft.menu.ProcessorInfo;
import com.nred.nuclearcraft.menu.ProcessorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public abstract class Processor extends BaseEntityBlock {
    private final String typeName;

    protected Processor(Properties properties, String typeName) {
        super(properties);
        this.typeName = typeName;
        this.registerDefaultState(this.defaultBlockState().setValue(PROCESSOR_ON, false).setValue(POWERED, false));
    }

    public static final BooleanProperty PROCESSOR_ON = BooleanProperty.create(MODID + "_processor_on");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;
        return createTickerHelper(blockEntityType, PROCESSOR_ENTITY_TYPE.get(typeName).get(), (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1, blockEntity));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip." + typeName).withStyle(ChatFormatting.AQUA));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ProcessorEntity entity) {
            ItemStack rtnStack = stack;
            if (stack.is(UPGRADE_MAP.get("speed"))) {
                if (Screen.hasShiftDown()) {
                    rtnStack = entity.itemStackHandler.internalInsertItem(ProcessorMenu.SPEED, stack, false);
                } else if (entity.itemStackHandler.internalInsertItem(ProcessorMenu.SPEED, stack.copyWithCount(1), false).getCount() == 0) {
                    rtnStack = stack.copyWithCount(stack.getCount() - 1);
                }
            } else if (stack.is(UPGRADE_MAP.get("energy"))) {
                if (Screen.hasShiftDown()) {
                    rtnStack = entity.itemStackHandler.internalInsertItem(ProcessorMenu.ENERGY, stack, false);
                } else if (entity.itemStackHandler.internalInsertItem(ProcessorMenu.ENERGY, stack.copyWithCount(1), false).getCount() == 0) {
                    rtnStack = stack.copyWithCount(stack.getCount() - 1);
                }
            }

            if (rtnStack.getCount() < stack.getCount()) { // At least 1 was transferred
                player.setItemInHand(hand, rtnStack);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof ProcessorEntity entity) {
                player.openMenu(state.getMenuProvider(level, pos), buf -> new ProcessorInfo(pos, entity.redstoneMode, typeName).write(buf));
            } else {
                throw new IllegalStateException("Missing Container Provider");
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        boolean flag = level.hasNeighborSignal(blockpos);
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(POWERED, flag);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos);
        if (!this.defaultBlockState().is(block) && flag != state.getValue(POWERED)) {
            ProcessorEntity entity = (ProcessorEntity) level.getBlockEntity(pos);
            level.setBlock(pos, state.setValue(POWERED, flag).setValue(PROCESSOR_ON, !(entity.redstoneMode && flag)), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PROCESSOR_ON, FACING, POWERED);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}