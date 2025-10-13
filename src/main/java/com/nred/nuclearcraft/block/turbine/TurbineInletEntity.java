package com.nred.nuclearcraft.block.turbine;

import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.internal.fluid.*;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.TURBINE_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;

public class TurbineInletEntity extends AbstractTurbineEntity implements ITileFluid {
    private final @Nonnull List<Tank> backupTanks = Collections.emptyList();

    private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.IN);

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public TurbineInletEntity(final BlockPos position, final BlockState blockState) {
        super(TURBINE_ENTITY_TYPE.get("inlet").get(), position, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(Turbine controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }
    // Fluids

    @Override
    @Nonnull
    public List<Tank> getTanks() {
        if (!isMachineAssembled()) {
            return backupTanks;
        }
        return getMultiblockController().get().tanks.subList(0, 1);
    }

    @Override
    @Nonnull
    public FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    @Nonnull
    public FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeFluidConnections(nbt, registries);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readFluidConnections(nbt, registries);
    }

//    // Capability TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
//            return hasFluidSideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//            if (hasFluidSideCapability(side)) {
//                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
//            }
//            return null;
//        }
//        else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
//            if (enable_mek_gas && hasFluidSideCapability(side)) {
//                return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
}