package com.nred.nuclearcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;

public class EnergyItem extends Item {
    public final Supplier<Integer> capacity;
    public final Supplier<Integer> rate;

    public EnergyItem(Properties properties, Supplier<Integer> capacity, Supplier<Integer> rate) {
        super(properties);
        this.capacity = capacity;
        this.rate = rate;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains("energy") && tag.getInt("energy") > 0;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return !tag.contains("energy") ? 0 : Math.round((float) tag.getInt("energy") / capacity.get() * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return FastColor.ARGB32.lerp((float) tag.getInt("energy") / capacity.get(), ChatFormatting.RED.getColor(), ChatFormatting.GREEN.getColor());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!tag.contains("energy")) {
            stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> data.update(compoundTag -> compoundTag.putInt("energy", 0)));
        }

        tooltipComponents.add(Component.translatable("tooltip.energy_stored", getFEString(tag.getInt("energy")), getFEString(capacity.get())).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}