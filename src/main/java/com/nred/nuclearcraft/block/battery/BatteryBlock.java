package com.nred.nuclearcraft.block.battery;

import com.nred.nuclearcraft.block.GenericTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IDynamicState;
import com.nred.nuclearcraft.block_entity.battery.BatteryEntity;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.battery.BatteryMultiblock;
import com.nred.nuclearcraft.multiblock.battery.IBatteryPartType;
import com.nred.nuclearcraft.property.ISidedEnergy;
import com.nred.nuclearcraft.util.NBTHelper;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class BatteryBlock extends GenericTooltipDeviceBlock<BatteryMultiblock, IBatteryPartType> implements IDynamicState, ISidedEnergy {
    public BatteryBlock(@NotNull MultiblockPartProperties<IBatteryPartType> properties) {
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
                    player.sendSystemMessage(Component.translatable(MODID + ".tooltip.energy_stored", UnitHelper.prefix(storage.getEnergyStoredLong(), storage.getMaxEnergyStoredLong(), 5,  "FE")));
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

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (placer == null || !stack.has(DataComponents.CUSTOM_DATA)) {
            return;
        }
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof BatteryEntity battery) {
            CompoundTag nbt = NBTHelper.getStackNBT(stack);

            battery.waitingEnergy += new EnergyStorage(battery.batteryType.getCapacity().get()).readFromNBT(nbt, null, "energyStorage").getEnergyStoredLong();

            if (placer.isCrouching()) {
                battery.readEnergyConnections(nbt, null);
            } else {
                battery.setEnergyConnection(EnergyConnection.OUT, Direction.orderedByNearest(placer)[0].getOpposite());
            }
        }
    }
}