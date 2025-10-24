package com.nred.nuclearcraft.multiblock.rtg;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.rtg.RTGEntity;
import com.nred.nuclearcraft.multiblock.Multiblock;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class RTGMultiblock extends Multiblock<RTGMultiblock> {
    protected final @Nonnull EnergyStorage storage = new EnergyStorage(1);
    protected long power = 0L;

    protected boolean refreshEnergy = false;

    public RTGMultiblock(Level level) {
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
            long powerSum = 0L;
            for (RTGEntity rtg : getParts(RTGEntity.class)) {
                powerSum += rtg.rtgType.getPower();
                rtg.onMultiblockRefresh();
            }
            this.power = powerSum;
            storage.setStorageCapacity(4 * powerSum);
            storage.setMaxTransfer(4 * powerSum);
            refreshEnergy = true;
        }
    }

    @Override
    protected void onMachinePaused() {
    }

    @Override
    protected void onMachineDisassembled() {
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
    protected void onAssimilate(IMultiblockController<RTGMultiblock> assimilated) {
        storage.mergeEnergyStorage(((RTGMultiblock) assimilated).storage);
    }

    @Override
    protected boolean updateServer() {
        if (refreshEnergy) {
            storage.cullEnergyStored();
            refreshEnergy = false;
        }

        getEnergyStorage().changeEnergyStored(power);
        return super.updateServer();
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
    }

    @Override
    public CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        writeEnergy(storage, data, registries, "energyStorage");
        return data;
    }
}