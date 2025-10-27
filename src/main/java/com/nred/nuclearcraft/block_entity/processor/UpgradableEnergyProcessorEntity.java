package com.nred.nuclearcraft.block_entity.processor;

import com.nred.nuclearcraft.block_entity.ITileInstallable;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.processor.info.UpgradableProcessorMenuInfo;
import com.nred.nuclearcraft.payload.processor.EnergyProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public abstract class UpgradableEnergyProcessorEntity<TILE extends UpgradableEnergyProcessorEntity<TILE, INFO>, INFO extends UpgradableProcessorMenuInfo<TILE, EnergyProcessorUpdatePacket, INFO>> extends EnergyProcessorEntity<TILE, INFO> implements ITileInstallable {
    protected UpgradableEnergyProcessorEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, String name) {
        super(type, pos, blockState, name);
    }

    @Override
    public boolean autoPushInternal(HandlerPair[] adjacentHandlers, NonNullList<ItemStack> stacks, List<Tank> tanks, List<Direction> dirs, int dirCount, int indexOffset) {
        boolean pushed = super.autoPushInternal(adjacentHandlers, stacks, tanks, dirs, dirCount, indexOffset);
        pushed |= tryPushSlot(adjacentHandlers, stacks, info.speedUpgradeSlot, dirs, dirCount, indexOffset);
        pushed |= tryPushSlot(adjacentHandlers, stacks, info.energyUpgradeSlot, dirs, dirCount, indexOffset);
        return pushed;
    }

    @Override
    public void refreshEnergyCapacity() {
        long capacity = getEnergyCapacity();
        getEnergyStorage().setStorageCapacity(capacity);
        getEnergyStorage().setMaxTransfer(capacity);
    }

    @Override
    public double getSpeedMultiplier() {
        if (info.isGenerator) {
            return (1D + speed_upgrade_multipliers_fp[2] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[2])) / (1D + energy_upgrade_multipliers_fp[1] * powerLawFactor(getEnergyCount(), energy_upgrade_power_laws_fp[1]));
        } else {
            return 1D + speed_upgrade_multipliers_fp[0] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[0]);
        }
    }

    @Override
    public double getPowerMultiplier() {
        if (info.isGenerator) {
            return 1D + speed_upgrade_multipliers_fp[3] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[3]);
        } else {
            return (1D + speed_upgrade_multipliers_fp[1] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[1])) / (1D + energy_upgrade_multipliers_fp[0] * powerLawFactor(getEnergyCount(), energy_upgrade_power_laws_fp[0]));
        }
    }

    public static double powerLawFactor(int upgradeCount, double power) {
        return Math.pow(upgradeCount, power) - 1D;
    }

    public int getSpeedCount() {
        return 1 + getInventoryStacks().get(info.speedUpgradeSlot).getCount();
    }

    public int getEnergyCount() {
        return Math.min(getSpeedCount(), 1 + getInventoryStacks().get(info.energyUpgradeSlot).getCount());
    }

    // ITileInventory

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = super.removeItem(slot, amount);
        if (!level.isClientSide()) {
            if (slot < info.itemInputSize) {
                refreshRecipe();
                refreshActivity();
            } else if (slot < info.itemInputSize + info.itemOutputSize) {
                refreshActivity();
            } else if (slot == info.speedUpgradeSlot || slot == info.energyUpgradeSlot) {
                refreshEnergyCapacity();
            }
        }
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        if (!level.isClientSide()) {
            if (slot == info.speedUpgradeSlot || slot == info.energyUpgradeSlot) {
                refreshEnergyCapacity();
            }
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == info.speedUpgradeSlot) {
            return stack.getItem() == UPGRADE_MAP.get("speed").asItem();
        } else if (slot == info.energyUpgradeSlot) {
            return stack.getItem() == UPGRADE_MAP.get("energy").asItem();
        }
        return super.canPlaceItem(slot, stack);
    }
}