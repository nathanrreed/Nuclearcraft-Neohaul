package com.nred.nuclearcraft.helpers;

import com.ibm.icu.impl.Pair;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.config.Config.UPGRADES_CONFIG;
import static com.nred.nuclearcraft.datagen.ModRecipeProvider.tag;
import static com.nred.nuclearcraft.menu.ProcessorMenu.ENERGY;
import static com.nred.nuclearcraft.menu.ProcessorMenu.SPEED;

public class RecipeHelpers {
    public static double calculateTicks(String typeName, double recipeTimeModifier, IItemHandler itemStackHandler) {
        return (recipeTimeModifier * PROCESSOR_CONFIG_MAP.get(typeName).base_time()) / getSpeedMultiplier(itemStackHandler);
    }

    public static double calculateEnergy(String typeName, double recipePowerModifier, IItemHandler itemStackHandler) {
        return PROCESSOR_CONFIG_MAP.get(typeName).base_power() * recipePowerModifier * getPowerMultiplier(itemStackHandler);
    }

    public static double getSpeedMultiplier(IItemHandler itemStackHandler) {
        return 1.0 + UPGRADES_CONFIG.speed_time_mult() * powerLawFactor(getSpeedCount(itemStackHandler), UPGRADES_CONFIG.speed_time());
    }

    public static double getPowerMultiplier(IItemHandler itemStackHandler) {
        return (1D + UPGRADES_CONFIG.speed_power_mult() * powerLawFactor(getSpeedCount(itemStackHandler), UPGRADES_CONFIG.speed_power())) / (1D + UPGRADES_CONFIG.energy_power_mult() * powerLawFactor(getEnergyCount(itemStackHandler), UPGRADES_CONFIG.energy_power()));
    }

    public static double powerLawFactor(int upgradeCount, double power) {
        return Math.pow(upgradeCount, power) - 1D;
    }

    public static int getSpeedCount(IItemHandler itemStackHandler) {
        return 1 + itemStackHandler.getStackInSlot(SPEED).getCount();
    }

    public static int getEnergyCount(IItemHandler itemStackHandler) {
        return Math.min(getSpeedCount(itemStackHandler), 1 + itemStackHandler.getStackInSlot(ENERGY).getCount());
    }

    public static List<@NotNull SizedIngredient> ingotDusts(String input1, int count1, String input2, int count2) {
        return List.of(tags(List.of(tag(Tags.Items.DUSTS, input1), tag(Tags.Items.INGOTS, input1)), count1), tags(List.of(tag(Tags.Items.DUSTS, input2), tag(Tags.Items.INGOTS, input2)), count2));
    }

    public static @NotNull SizedIngredient ingotDust(String input, int count) {
        return tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.INGOTS, input)), count);
    }

    public static @NotNull SizedIngredient gemDust(String input, int count) {
        return tags(List.of(tag(Tags.Items.DUSTS, input), tag(Tags.Items.GEMS, input)), count);
    }

    public static SizedIngredient tags(List<TagKey<Item>> tags, int count) {
        return new SizedIngredient(Ingredient.fromValues(tags.stream().map(Ingredient.TagValue::new)), count);
    }

    public static int probabilityPacker(int probability, int amount) {
        return (probability << 16) | (amount & 0xFFFF);
    }

    public static Pair<Short, Short> probabilityUnpacker(int packed) {
        short probability = (short) (packed >> 16);
        short amount = (short) packed;

        return Pair.of(probability, amount);
    }
}
