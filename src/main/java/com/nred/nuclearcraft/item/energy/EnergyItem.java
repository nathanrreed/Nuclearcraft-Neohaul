package com.nred.nuclearcraft.item.energy;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.registration.DataComponentRegistration.ENERGY_COMPONENT;

public class EnergyItem extends Item implements IChargeableItem {
    public final Supplier<Integer> capacity;
    public final Supplier<Integer> maxTransfer;
    private final EnergyConnection energyConnection;

    public EnergyItem(Properties properties, Supplier<Integer> capacity, Supplier<Integer> maxTransfer, EnergyConnection energyConnection) {
        super(properties);
        this.capacity = capacity;
        this.maxTransfer = maxTransfer;
        this.energyConnection = energyConnection;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(ENERGY_COMPONENT, 0) > 0;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) stack.getOrDefault(ENERGY_COMPONENT, 0) / capacity.get() * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.lerp((float) stack.getOrDefault(ENERGY_COMPONENT, 0) / capacity.get(), ChatFormatting.RED.getColor(), ChatFormatting.GREEN.getColor());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (!stack.has(ENERGY_COMPONENT)) {
            stack.set(ENERGY_COMPONENT, 0);
        }

        tooltipComponents.add(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.energy_stored", getFEString(stack.getOrDefault(ENERGY_COMPONENT, 0)), getFEString(capacity.get())).withStyle(ChatFormatting.LIGHT_PURPLE));
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