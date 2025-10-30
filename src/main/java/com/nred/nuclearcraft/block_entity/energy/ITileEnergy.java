package com.nred.nuclearcraft.block_entity.energy;

import com.nred.nuclearcraft.block_entity.ITile;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.block_entity.passive.ITilePassive;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ITileEnergy extends ITile {

    // Storage

    EnergyStorage getEnergyStorage();

    // Energy Connection

    EnergyConnection[] getEnergyConnections();

    default EnergyConnection getEnergyConnection(@Nonnull Direction side) {
        return getEnergyConnections()[side.ordinal()];
    }

    default void setEnergyConnection(@Nonnull EnergyConnection energyConnection, @Nonnull Direction side) {
        getEnergyConnections()[side.ordinal()] = energyConnection;
    }

    default void toggleEnergyConnection(@Nonnull Direction side, @Nonnull EnergyConnection.Type type) {
        setEnergyConnection(getEnergyConnection(side).next(type), side);
        markDirtyAndNotify(true);
    }

    default boolean canConnectEnergy(@Nonnull Direction side) {
        return getEnergyConnection(side).canConnect();
    }

    static @Nonnull EnergyConnection[] energyConnectionAll(@Nonnull EnergyConnection connection) {
        EnergyConnection[] array = new EnergyConnection[6];
        for (int i = 0; i < 6; ++i) {
            array[i] = connection;
        }
        return array;
    }

    default boolean hasConfigurableEnergyConnections() {
        return false;
    }

    // Energy Connection Wrapper Methods

    default int getEnergyStored() {
        return getEnergyStorage().getEnergyStored();
    }

    default int getMaxEnergyStored() {
        return getEnergyStorage().getMaxEnergyStored();
    }

    default long getEnergyStoredLong() {
        return getEnergyStorage().getEnergyStoredLong();
    }

    default long getMaxEnergyStoredLong() {
        return getEnergyStorage().getMaxEnergyStoredLong();
    }

    default boolean canReceiveEnergy(Direction side) {
        return getEnergyConnection(side).canReceive();
    }

    default boolean canExtractEnergy(Direction side) {
        return getEnergyConnection(side).canExtract();
    }

    default int receiveEnergy(int maxReceive, Direction side, boolean simulate) {
        return canReceiveEnergy(side) ? getEnergyStorage().receiveEnergy(maxReceive, simulate) : 0;
    }

    default int extractEnergy(int maxExtract, Direction side, boolean simulate) {
        return canExtractEnergy(side) ? getEnergyStorage().extractEnergy(maxExtract, simulate) : 0;
    }

    // Energy Wrappers

    @Nonnull
    EnergyTileWrapper[] getEnergySides();

    default @Nonnull EnergyTileWrapper getEnergySide(@Nonnull Direction side) {
        return getEnergySides()[side.ordinal()];
    }

    static @Nonnull EnergyTileWrapper[] getDefaultEnergySides(@Nonnull ITileEnergy tile) {
        return new EnergyTileWrapper[]{new EnergyTileWrapper(tile, Direction.DOWN), new EnergyTileWrapper(tile, Direction.UP), new EnergyTileWrapper(tile, Direction.NORTH), new EnergyTileWrapper(tile, Direction.SOUTH), new EnergyTileWrapper(tile, Direction.WEST), new EnergyTileWrapper(tile, Direction.EAST)};
    }

    // Energy Distribution

    default void pushEnergy() {
        for (Direction side : Direction.values()) {
            if (getEnergyStoredLong() <= 0) {
                return;
            }
            pushEnergyToSide(side);
        }
    }

    default void pushEnergyToSide(@Nonnull Direction side) {
        if (!getEnergyConnection(side).canExtract()) {
            return;
        }

        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }

        if (tile instanceof ITileEnergy tileEnergy) {
            if (!tileEnergy.getEnergyConnection(side.getOpposite()).canReceive()) {
                return;
            }
        }
        if (tile instanceof ITilePassive tilePassive) {
            if (!tilePassive.canPushEnergyTo()) {
                return;
            }
        }

        IEnergyStorage adjStorage = getTileWorld().getCapability(Capabilities.EnergyStorage.BLOCK, tile.getBlockPos(), side.getOpposite());

        if (adjStorage != null && getEnergyStorage().canExtract()) {
            getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getMaxEnergyStored(), true), false), false);
            return;
        }
    }

    // NBT

    default CompoundTag writeEnergy(CompoundTag nbt, HolderLookup.Provider registries) {
        getEnergyStorage().writeToNBT(nbt, registries, "energyStorage");
        return nbt;
    }

    default void readEnergy(CompoundTag nbt, HolderLookup.Provider registries) {
        getEnergyStorage().readFromNBT(nbt, registries, "energyStorage");
    }

    default CompoundTag writeEnergyConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < 6; ++i) {
            nbt.putInt("energyConnections" + i, getEnergyConnections()[i].ordinal());
        }
        return nbt;
    }

    default void readEnergyConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        if (hasConfigurableEnergyConnections()) {
            for (int i = 0; i < 6; ++i) {
                if (nbt.contains("energyConnections" + i)) {
                    getEnergyConnections()[i] = EnergyConnection.values()[nbt.getInt("energyConnections" + i)];
                }
            }
        }
    }

//    // Capabilities

    default boolean hasEnergySideCapability(@Nullable Direction side) {
        return side == null || getEnergyConnection(side).canConnect();
    }

    default EnergyTileWrapper getEnergySideCapability(@Nullable Direction side) {
        if (hasEnergySideCapability(side)) {
            return getEnergySide(nonNullSide(side));
        }
        return null;
    }

//    // TOP
//
//    @Override
//    @Optional.Method(modid = "theoneprobe")
//    default long getStoredPower() {
//        return getEnergyStoredLong();
//    }
//
//    @Override
//    @Optional.Method(modid = "theoneprobe")
//    default long getCapacity() {
//        return getMaxEnergyStoredLong();
//    }
}
