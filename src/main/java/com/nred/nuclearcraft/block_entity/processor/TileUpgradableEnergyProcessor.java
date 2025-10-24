//package com.nred.nuclearcraft.block_entity.processor;
//
//import nc.init.NCItems;
//import nc.network.tile.processor.EnergyProcessorUpdatePacket;
//import nc.tile.ITileInstallable;
//import nc.tile.internal.fluid.Tank;
//import nc.tile.processor.info.UpgradableProcessorContainerInfo;
//import nc.util.StackHelper;
//import net.minecraft.item.ItemStack;
//
//import java.util.List;
//
//import static nc.config.NCConfig.*;
//
//public abstract class TileUpgradableEnergyProcessor<TILE extends TileUpgradableEnergyProcessor<TILE, INFO>, INFO extends UpgradableProcessorContainerInfo<TILE, EnergyProcessorUpdatePacket, INFO>> extends TileEnergyProcessor<TILE, INFO> implements ITileInstallable {
//
//	/**
//	 * Don't use this constructor!
//	 */
//	protected TileUpgradableEnergyProcessor() {
//		super();
//	}
//
//	protected TileUpgradableEnergyProcessor(String name) {
//		super(name);
//	}
//
//	@Override
//	public boolean autoPushInternal(HandlerPair[] adjacentHandlers, NonNullList<ItemStack> stacks, List<Tank> tanks, List<EnumFacing> dirs, int dirCount, int indexOffset) {
//		boolean pushed = super.autoPushInternal(adjacentHandlers, stacks, tanks, dirs, dirCount, indexOffset);
//		pushed |= tryPushSlot(adjacentHandlers, stacks, info.speedUpgradeSlot, dirs, dirCount, indexOffset);
//		pushed |= tryPushSlot(adjacentHandlers, stacks, info.energyUpgradeSlot, dirs, dirCount, indexOffset);
//		return pushed;
//	}
//
//	@Override
//	public void refreshEnergyCapacity() {
//		long capacity = getEnergyCapacity();
//		getEnergyStorage().setStorageCapacity(capacity);
//		getEnergyStorage().setMaxTransfer(capacity);
//	}
//
//	@Override
//	public double getSpeedMultiplier() {
//		if (info.isGenerator) {
//			return (1D + speed_upgrade_multipliers_fp[2] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[2])) / (1D + energy_upgrade_multipliers_fp[1] * powerLawFactor(getEnergyCount(), energy_upgrade_power_laws_fp[1]));
//		}
//		else {
//			return 1D + speed_upgrade_multipliers_fp[0] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[0]);
//		}
//	}
//
//	@Override
//	public double getPowerMultiplier() {
//		if (info.isGenerator) {
//			return 1D + speed_upgrade_multipliers_fp[3] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[3]);
//		}
//		else {
//			return (1D + speed_upgrade_multipliers_fp[1] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[1])) / (1D + energy_upgrade_multipliers_fp[0] * powerLawFactor(getEnergyCount(), energy_upgrade_power_laws_fp[0]));
//		}
//	}
//
//	public static double powerLawFactor(int upgradeCount, double power) {
//		return Math.pow(upgradeCount, power) - 1D;
//	}
//
//	public int getSpeedCount() {
//		return 1 + getInventoryStacks().get(info.speedUpgradeSlot).getCount();
//	}
//
//	public int getEnergyCount() {
//		return Math.min(getSpeedCount(), 1 + getInventoryStacks().get(info.energyUpgradeSlot).getCount());
//	}
//
//	// ITileInventory
//
//	@Override
//	public ItemStack decrStackSize(int slot, int amount) {
//		ItemStack stack = super.decrStackSize(slot, amount);
//		if (!world.isRemote) {
//			if (slot < info.itemInputSize) {
//				refreshRecipe();
//				refreshActivity();
//			}
//			else if (slot < info.itemInputSize + info.itemOutputSize) {
//				refreshActivity();
//			}
//			else if (slot == info.speedUpgradeSlot || slot == info.energyUpgradeSlot) {
//				refreshEnergyCapacity();
//			}
//		}
//		return stack;
//	}
//
//	@Override
//	public void setInventorySlotContents(int slot, ItemStack stack) {
//		super.setInventorySlotContents(slot, stack);
//		if (!world.isRemote) {
//			if (slot == info.speedUpgradeSlot || slot == info.energyUpgradeSlot) {
//				refreshEnergyCapacity();
//			}
//		}
//	}
//
//	@Override
//	public boolean isItemValidForSlot(int slot, ItemStack stack) {
//		if (slot == info.speedUpgradeSlot) {
//			return stack.getItem() == NCItems.upgrade && StackHelper.getMetadata(stack) == 0;
//		}
//		else if (slot == info.energyUpgradeSlot) {
//			return stack.getItem() == NCItems.upgrade && StackHelper.getMetadata(stack) == 1;
//		}
//
//		return super.isItemValidForSlot(slot, stack);
//	}
//}
