package com.nred.nuclearcraft.block.energyFluid;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block.energy.TileEnergy;
import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.internal.energy.EnergyConnection;
import com.nred.nuclearcraft.block.internal.fluid.*;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TileEnergyFluid extends TileEnergy implements ITileFluid {

    private @Nonnull List<Tank> tanks = null;

    private @Nonnull FluidConnection[] fluidConnections = null;

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    private boolean inputTanksSeparated = false;
    private List<Boolean> voidUnusableFluidInputs;
    private List<TankOutputSetting> tankOutputSettings;

    public TileEnergyFluid(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long capacity, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, Set<ResourceKey<Fluid>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, capacity, NCMath.toInt(capacity), energyConnections, new IntArrayList(new int[]{fluidCapacity}), Lists.<Set<ResourceKey<Fluid>>>newArrayList(allowedFluids), fluidConnections);
    }

    public TileEnergyFluid(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long capacity, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceKey<Fluid>>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, capacity, NCMath.toInt(capacity), energyConnections, fluidCapacity, allowedFluids, fluidConnections);
    }

    public TileEnergyFluid(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, int fluidCapacity, Set<ResourceKey<Fluid>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        this(type, pos, blockState, capacity, maxTransfer, energyConnections, new IntArrayList(new int[]{fluidCapacity}), Lists.<Set<ResourceKey<Fluid>>>newArrayList(allowedFluids), fluidConnections);
    }

    public TileEnergyFluid(BlockEntityType<?> type, BlockPos pos, BlockState blockState, long capacity, int maxTransfer, @Nonnull EnergyConnection[] energyConnections, @Nonnull IntList fluidCapacity, List<Set<ResourceKey<Fluid>>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        super(type, pos, blockState, capacity, maxTransfer, energyConnections);
        initTileEnergyFluid(fluidCapacity, allowedFluids, fluidConnections);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    protected void initTileEnergyFluid(@Nonnull IntList fluidCapacity, List<Set<ResourceKey<Fluid>>> allowedFluids, @Nonnull FluidConnection[] fluidConnections) {
        tanks = new ArrayList<>();
        voidUnusableFluidInputs = new ArrayList<>();
        tankOutputSettings = new ArrayList<>();
        for (int i = 0, len = fluidCapacity.size(); i < len; ++i) {
            tanks.add(new Tank(fluidCapacity.get(i), allowedFluids == null || allowedFluids.size() <= i ? null : allowedFluids.get(i)));
            voidUnusableFluidInputs.add(false);
            tankOutputSettings.add(TankOutputSetting.DEFAULT);
        }
        this.fluidConnections = fluidConnections;
    }

    @Override
    public @Nonnull List<Tank> getTanks() {
        return tanks;
    }

    @Override
    public @Nonnull FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    public @Nonnull FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return inputTanksSeparated;
    }

    @Override
    public void setInputTanksSeparated(boolean shared) {
        inputTanksSeparated = shared;
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return voidUnusableFluidInputs.get(tankNumber);
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
        voidUnusableFluidInputs.set(tankNumber, voidUnusableFluidInput);
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return tankOutputSettings.get(tankNumber);
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
        tankOutputSettings.set(tankNumber, setting);
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeTanks(nbt, registries);
        writeFluidConnections(nbt, registries);
        writeTankSettings(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readTanks(nbt, registries);
        readFluidConnections(nbt, registries);
        readTankSettings(nbt, registries);
    }

//	// Capability TODO
//
//	@Override
//	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
//			return !getTanks().isEmpty() && hasFluidSideCapability(side);
//		}
//		return super.hasCapability(capability, side);
//	}
//
//	@Override
//	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
//				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
//			}
//			return null;
//		}
//		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
//			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
//				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
//			}
//			return null;
//		}
//		return super.getCapability(capability, side);
//	}
}
