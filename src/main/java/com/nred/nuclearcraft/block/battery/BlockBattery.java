package com.nred.nuclearcraft.block.battery;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IDynamicState;
import com.nred.nuclearcraft.block_entity.battery.BatteryEntity;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.battery.BatteryMultiblock;
import com.nred.nuclearcraft.multiblock.battery.IBatteryPartType;
import com.nred.nuclearcraft.property.ISidedEnergy;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class BlockBattery extends GenericTooltipDeviceBlock<BatteryMultiblock, IBatteryPartType> implements IDynamicState, ISidedEnergy {
    public BlockBattery(@NotNull MultiblockPartProperties<IBatteryPartType> properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!MultitoolItem.isMultitool(player.getItemInHand(hand))) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof BatteryEntity battery) {
                if (!level.isClientSide()) {
                    EnergyStorage storage = battery.getEnergyStorage();
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.energy_stored", UnitHelper.prefix(storage.getEnergyStoredLong(), storage.getMaxEnergyStoredLong(), 5, "RF")));
                }
                return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        createEnergyBlockStateDefinition(builder);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof BatteryEntity battery) {
            BatteryMultiblock multiblock = battery.getMultiblockController().orElse(null);
            if (multiblock != null) {
                return multiblock.getComparatorStrength();
            }
        }
        return 0;
    }
}