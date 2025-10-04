package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.GenericHorizontalTooltipDeviceBlock;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_HORIZONTAL;
import static com.nred.nuclearcraft.registration.ItemRegistration.MULTITOOL;

public class FissionVentBlock extends GenericHorizontalTooltipDeviceBlock<FissionReactor, IFissionPartType> {
    public FissionVentBlock(@NotNull MultiblockPartProperties<IFissionPartType> iFissionPartTypeMultiblockPartProperties) {
        super(iFissionPartTypeMultiblockPartProperties);
        registerDefaultState(this.defaultBlockState().setValue(INPUT_STATE, false));
    }

    public static final BooleanProperty INPUT_STATE = BooleanProperty.create(MODID + "_input_state");

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INPUT_STATE, FACING_HORIZONTAL);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(MULTITOOL)) {
            if (!Screen.hasShiftDown()) {
                boolean value = !state.getValue(INPUT_STATE);
                level.setBlockAndUpdate(pos, state.setValue(INPUT_STATE, value));
                if (level.isClientSide)
                    player.sendSystemMessage(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.vent_toggle", Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.side_config." + (value ? "output" : "input")).withStyle(value ? ChatFormatting.RED : ChatFormatting.DARK_AQUA)));
                return ItemInteractionResult.CONSUME;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}