package com.nred.nuclearcraft.block_entity.energy;

import com.nred.nuclearcraft.block_entity.NCTile;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.util.NCMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public abstract class TileEnergy extends NCTile implements ITileEnergy {
    protected @Nonnull EnergyStorage storage = null;

    protected @Nonnull EnergyConnection[] energyConnections = null;

    private final @Nonnull EnergyTileWrapper[] energySides;

    public TileEnergy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long capacity, @Nonnull EnergyConnection[] energyConnections) {
        this(type, pos, blockState, capacity, NCMath.toInt(capacity), energyConnections);
    }

    public TileEnergy(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
        super(type, pos, blockState);
        initTileEnergy(capacity, maxTransfer, energyConnections);
        this.energySides = ITileEnergy.getDefaultEnergySides(this);
    }

    protected void initTileEnergy(long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
        this.storage = new EnergyStorage(capacity, maxTransfer);
        this.energyConnections = energyConnections;
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return storage;
    }

    @Override
    public EnergyConnection[] getEnergyConnections() {
        return energyConnections;
    }

    @Override
    public @Nonnull EnergyTileWrapper[] getEnergySides() {
        return energySides;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeEnergy(nbt, registries);
        writeEnergyConnections(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readEnergy(nbt, registries);
        readEnergyConnections(nbt, registries);
    }

    // Energy Connections

    public void setEnergyConnectionAll(EnergyConnection energyConnection) {
        energyConnections = ITileEnergy.energyConnectionAll(energyConnection);
    }
}