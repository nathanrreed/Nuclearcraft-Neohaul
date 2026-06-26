package com.nred.nuclearcraft.item.energy;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.item.NCItem;
import com.nred.nuclearcraft.util.UnitHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.DataComponentRegistration.ENERGY_COMPONENT;

public class EnergyItem extends NCItem implements IChargeableComponentItem {
    public final Supplier<Integer> capacity;
    public final Supplier<Integer> maxTransfer;
    private final EnergyConnection energyConnection;

    public EnergyItem(Properties properties, Supplier<Integer> capacity, Supplier<Integer> maxTransfer, EnergyConnection energyConnection, String... tooltip) {
        super(properties, Arrays.stream(tooltip).toList());
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        this.energyConnection = energyConnection;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getEnergyStored(stack) > 0;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getEnergyStored(stack) / capacity.get() * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.lerp((float) getEnergyStored(stack) / capacity.get(), ChatFormatting.RED.getColor(), ChatFormatting.GREEN.getColor());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!stack.has(ENERGY_COMPONENT)) {
            stack.set(ENERGY_COMPONENT, 0);
        }

        tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.energy_stored", UnitHelper.prefix(getEnergyStored(stack), getMaxEnergyStored(stack), 5, "FE")).withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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