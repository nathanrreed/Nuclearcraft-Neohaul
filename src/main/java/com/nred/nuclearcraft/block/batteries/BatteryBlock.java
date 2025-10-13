package com.nred.nuclearcraft.block.batteries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block.internal.fluid.TankVoid;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.nred.nuclearcraft.config.Config.BATTERY_CONFIG_CAPACITY;
import static com.nred.nuclearcraft.config.Config.VOLTAIC_PILE_CAPACITY;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.item.TooltipItem.shiftForDetails;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.BATTERY_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.ItemRegistration.MULTITOOL;

public class BatteryBlock extends BaseEntityBlock {
    private final int tier;

    public BatteryBlock(int tier) {
        super(Properties.of());
        this.tier = tier;
    }

    MapCodec<BatteryBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(Codec.INT.fieldOf("tier").forGetter(battery -> battery.tier)).apply(instance, BatteryBlock::new)
    );

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryEntity(pos, state, this.tier);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) return null;
        return createTickerHelper(blockEntityType, BATTERY_ENTITY_TYPE.get(this.tier).get(), (level1, pos, state1, blockEntity) -> blockEntity.tick(level1, pos, state1, blockEntity));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag())).copyTag();
        EnergyStorage energyHandler = new EnergyStorage(this.tier < 10 ? BATTERY_CONFIG_CAPACITY.get(this.tier) : VOLTAIC_PILE_CAPACITY.get(this.tier - 10));
        if (tag.contains("energy"))
            energyHandler.deserializeNBT(context.registries(), tag.get("energy"));

        tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.processor.energy.stored", getFEString(energyHandler.getEnergyStored()), getFEString(energyHandler.getMaxEnergyStored())).withStyle(ChatFormatting.LIGHT_PURPLE));
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.battery.info").withStyle(ChatFormatting.AQUA));
        } else {
            tooltipComponents.add(shiftForDetails);
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof BatteryEntity entity && stack.is(MULTITOOL)) {
            boolean opposite = Screen.hasShiftDown();
            Direction side = opposite ? hitResult.getDirection().getOpposite() : hitResult.getDirection();

            entity.sideOptions.put(side, entity.sideOptions.get(side).next(EnergyConnection.Type.DEFAULT));

            if (level.isClientSide) {
                EnergyConnection energyConnection = entity.sideOptions.get(side);
                player.sendSystemMessage(Component.translatable(opposite ? NuclearcraftNeohaul.MODID + ".message.multitool.energy_toggle_opposite" : NuclearcraftNeohaul.MODID + ".message.multitool.energy_toggle").append(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.side_config." + energyConnection.getSerializedName()).withStyle(energyConnection.getTextColor())));
            }
            return ItemInteractionResult.CONSUME;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity instanceof BatteryEntity be && be.getMultiblockController().isPresent() ? be.getMultiblockController().get().getComparatorStrength() : 0;
    }
}