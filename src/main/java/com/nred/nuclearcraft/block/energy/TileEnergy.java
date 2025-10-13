package com.nred.nuclearcraft.block.energy;

import com.nred.nuclearcraft.block.NCTile;
import com.nred.nuclearcraft.block.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.block.internal.fluid.TankVoid;
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
        energySides = ITileEnergy.getDefaultEnergySides(this);
    }

    protected void initTileEnergy(long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections) {
        storage = new EnergyStorage(capacity, maxTransfer);
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

//    // Capability TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityEnergy.ENERGY || (ModCheck.gregtechLoaded() && enable_gtce_eu && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER)) {
//            return hasEnergySideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityEnergy.ENERGY) {
//            if (hasEnergySideCapability(side)) {
//                return CapabilityEnergy.ENERGY.cast(getEnergySide(nonNullSide(side)));
//            }
//            return null;
//        } else if (ModCheck.gregtechLoaded() && capability == GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER) {
//            if (enable_gtce_eu && hasEnergySideCapability(side)) {
//                return GregtechCapabilities.CAPABILITY_ENERGY_CONTAINER.cast(getEnergySideGT(nonNullSide(side)));
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
}
