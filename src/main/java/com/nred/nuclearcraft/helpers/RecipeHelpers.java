package com.nred.nuclearcraft.helpers;

import net.neoforged.neoforge.items.IItemHandler;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.config.Config.UPGRADES_CONFIG;
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
}
