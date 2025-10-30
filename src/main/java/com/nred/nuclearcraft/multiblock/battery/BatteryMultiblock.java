package com.nred.nuclearcraft.multiblock.battery;

import com.nred.nuclearcraft.block_entity.battery.BatteryEntity;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.util.NCMath;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class BatteryMultiblock extends Multiblock<BatteryMultiblock> {
    protected final @Nonnull EnergyStorage storage = new EnergyStorage(1);
    protected int comparatorStrength = 0;

    protected boolean refreshEnergy = false;

    public BatteryMultiblock(Level level) {
        super(level);
    }

    public @Nonnull EnergyStorage getEnergyStorage() {
        return storage;
    }

    @Override
    protected void onMachineAssembled() {
        onMultiblockFormed();
    }

    @Override
    protected void onMachineRestored() {
        onMultiblockFormed();
    }

    protected void onMultiblockFormed() {
        if (!getWorld().isClientSide()) {
            long capacity = 0L;
            for (BatteryEntity battery : getParts(BatteryEntity.class)) {
                capacity += battery.batteryType.getCapacity().get();
                battery.onMultiblockRefresh();
            }
            storage.setStorageCapacity(capacity);
            storage.setMaxTransfer(capacity);
            refreshEnergy = true;
        }
    }

    @Override
    protected void onMachinePaused() {
    }

    @Override
    protected void onMachineDisassembled() {
        int i = 0;
    }

    @Override
    protected int getMinimumNumberOfPartsForAssembledMachine() {
        return 1;
    }

    @Override
    protected int getMaximumXSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumZSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected int getMaximumYSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<BatteryMultiblock> assimilated) {
        storage.mergeEnergyStorage(((BatteryMultiblock) assimilated).storage);
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<BatteryMultiblock> oldPart) {
        super.onPartRemoved(oldPart);
        if (oldPart instanceof BatteryEntity battery) {
            EnergyStorage storage = this.getEnergyStorage();
            if (this.getPartCount(BatteryEntity.class) < 2) {
                battery.waitingEnergy += storage.getEnergyStored();
            } else {
                double fraction = (double) getEnergyStorage().getEnergyStoredLong() / (double) getEnergyStorage().getMaxEnergyStoredLong();
                long energy = (long) (fraction * battery.batteryType.getCapacity().get());
                battery.waitingEnergy += energy;
                storage.changeEnergyStored(-energy);
            }
        }
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
            for (BatteryEntity battery : getParts(BatteryEntity.class)) {
                battery.setChanged();
                battery.updateComparatorOutputLevel();
            }
        }
        return shouldUpdate || super.updateServer();
    }

    public int getComparatorStrength() {
        return NCMath.getComparatorSignal(storage.getEnergyStoredLong(), storage.getMaxEnergyStoredLong(), 0D);
    }

    @Override
    protected void updateClient() {
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int i, int i1, int i2, IMultiblockValidator iMultiblockValidator) {
        return true;
    }

    @Override
    public int getMinimumInteriorLength() {
        return 0;
    }

    @Override
    public int getMaximumInteriorLength() {
        return 0;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        readEnergy(storage, data, registries, "energyStorage");
        comparatorStrength = data.getInt("comparatorStrength");
    }

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        writeEnergy(storage, data, registries, "energyStorage");
        data.putInt("comparatorStrength", comparatorStrength);
        return data;
    }
}