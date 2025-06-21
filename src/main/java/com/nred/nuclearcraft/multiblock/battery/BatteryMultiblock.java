package com.nred.nuclearcraft.multiblock.battery;

import com.nred.nuclearcraft.block.batteries.BatteryEntity;
import com.nred.nuclearcraft.helpers.CustomEnergyHandler;
import com.nred.nuclearcraft.multiblock.turbine.CuboidalMultiblock;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class BatteryMultiblock extends CuboidalMultiblock<BatteryMultiblock> {
    protected final @Nonnull CustomEnergyHandler storage = new CustomEnergyHandler(1, true, true);
    protected int comparatorStrength = 0;

    protected boolean refreshEnergy = false;

    public BatteryMultiblock(Level level) {
        super(level);
    }

    public @Nonnull CustomEnergyHandler getEnergyStorage() {
        return storage;
    }

    @Override
    public int getMinimumInteriorLength() {
        return 0;
    }

    @Override
    public int getMaximumInteriorLength() {
        return Integer.MAX_VALUE - 2;
    }

    @Override
    protected int getMinimumNumberOfPartsForAssembledMachine() {
        return 1;
    }

    @Override
    protected void onPartAdded(IMultiblockPart<BatteryMultiblock> iMultiblockPart) {

    }

    @Override
    protected void onPartRemoved(IMultiblockPart<BatteryMultiblock> iMultiblockPart) {

    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        storage.deserializeNBT(registries, data.get("energy"));
        comparatorStrength = data.getInt("comparatorStrength");
    }

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        data.put("energy", storage.serializeNBT(registries));
        data.putInt("comparatorStrength", comparatorStrength);
        return data;
    }

    @Override
    protected void onMachineAssembled() {
        onMultiblockFormed();
    }

    @Override
    protected void onMachineRestored() {
        onMultiblockFormed();
    }

    @Override
    protected void onMachinePaused() {

    }

    @Override
    protected void onMachineDisassembled() {

    }

    @Override
    protected void onAssimilate(IMultiblockController<BatteryMultiblock> iMultiblockController) {
        if (iMultiblockController instanceof BatteryMultiblock assimilated) {
            storage.mergeEnergyStorage(assimilated.storage);
        }
    }

    @Override
    protected void onAssimilated(IMultiblockController<BatteryMultiblock> iMultiblockController) {
    }

    @Override
    protected boolean updateServer() {
        if (refreshEnergy) {
            storage.cullEnergyStored();
            refreshEnergy = false;
        }

        boolean shouldUpdate = false;
        int compStrength = getComparatorStrength();
        if (comparatorStrength != compStrength) {
            shouldUpdate = true;
        }
        comparatorStrength = compStrength;
        if (shouldUpdate) {
            for (BatteryEntity battery : getConnectedParts(test -> test instanceof BatteryEntity).map(e -> (BatteryEntity) e).toList()) {
                getWorld().setBlocksDirty(battery.getBlockPos(), battery.getBlockState(), battery.getBlockState());
            }
        }
        return shouldUpdate;
    }

    @Override
    protected void updateClient() {
    }

    @Override
    protected boolean isBlockGoodForFrame(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForTop(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForBottom(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForSides(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    protected void onMultiblockFormed() {
        if (!getWorld().isClientSide) {
            long capacity = 0L;
            for (IMultiblockPart<BatteryMultiblock> part : getConnectedParts()) {
                if (part instanceof BatteryEntity batteryEntity) {
                    capacity += batteryEntity.capacity;
                    batteryEntity.onMultiblockRefresh();
                }
            }

            storage.setCapacity((int) capacity);
            storage.setMaxTransfer((int) capacity);
            refreshEnergy = true;
        }
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        return true;
    }

    public int getComparatorStrength() {
        return NCMath.getComparatorSignal(storage.getEnergyStored(), storage.getMaxEnergyStored(), 0D);
    }
}