package com.nred.nuclearcraft.block.item.energy;

import com.nred.nuclearcraft.block.item.NCItemBlock;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.item.energy.IChargableItem;
import com.nred.nuclearcraft.util.InfoHelper;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class ItemBlockEnergy extends NCItemBlock implements IChargableItem {

    private final Supplier<Integer> capacity;
    private final Supplier<Integer> maxTransfer;
    private final EnergyConnection energyConnection;

    public ItemBlockEnergy(Block block, Supplier<Integer> capacity, Supplier<Integer> maxTransfer, EnergyConnection connection, Component... tooltip) {
        super(block, tooltip);
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        energyConnection = connection;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        InfoHelper.infoLine(tooltip, ChatFormatting.LIGHT_PURPLE, Component.translatable(MODID + ".tooltip.energy_stored", UnitHelper.getFormattedFraction(getEnergyStored(stack), getMaxEnergyStored(stack), "RF")));
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        CompoundTag nbt = IChargableItem.getEnergyStorageNBT(stack);
        return nbt != null && nbt.getLong("energy") > 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.lerp((float) getEnergyStored(stack) / capacity.get(), ChatFormatting.RED.getColor(), ChatFormatting.GREEN.getColor());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Mth.ceil(Mth.clamp((double) getEnergyStored(stack) / capacity.get(), 0D, 1D) * 13f);
    }

    @Override
    public long getMaxEnergyStored(ItemStack stack) {
        return capacity.get();
    }

    @Override
    public int getMaxTransfer(ItemStack stack) {
        return maxTransfer.get();
    }

    @Override
    public boolean canReceive(ItemStack stack) {
        return energyConnection.canReceive();
    }

    @Override
    public boolean canExtract(ItemStack stack) {
        return energyConnection.canExtract();
    }

    @Override
    public EnergyConnection getEnergyConnection(ItemStack stack) {
        return energyConnection;
    }
}