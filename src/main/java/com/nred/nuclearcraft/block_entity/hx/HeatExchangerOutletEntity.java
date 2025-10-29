package com.nred.nuclearcraft.block_entity.hx;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.passive.ITilePassive;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerLogic;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeNetwork;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.HX_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.FACING_ALL;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

public class HeatExchangerOutletEntity extends AbstractHeatExchangerEntity implements ITickable, ITileFluid {

    private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(TankSorption.OUT);

    private final @Nonnull FluidTileWrapper[] fluidSides;
    private final @Nonnull ChemicalTileWrapper[] chemicalSides;

    public @Nullable HeatExchangerTubeNetwork network = null;

    public HeatExchangerOutletEntity(final BlockPos position, final BlockState blockState) {
        super(HX_ENTITY_TYPE.get("outlet").get(), position, blockState);
        fluidSides = ITileFluid.getDefaultFluidSides(this);
        chemicalSides = ITileFluid.getDefaultChemicalSides(this);
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position.isFace();
    }

    @Override
    public void onPreMachineAssembled(HeatExchanger controller) {
        super.onPreMachineAssembled(controller);
        if (!level.isClientSide()) {
            Optional<Direction> facing = getPartPosition().getDirection();
            facing.ifPresent(direction -> level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(FACING_ALL, direction), 2));
        }
    }

    @Override
    public void onPreMachineBroken() {
        network = null;
        super.onPreMachineBroken();
    }

    @Override
    public void update() {
        if (!level.isClientSide() && isMachineAssembled()) {
            List<Tank> tanks = getTanks();
            if (!tanks.isEmpty() && !tanks.get(0).isEmpty()) {
                Optional<Direction> posFacing = getPartPosition().getDirection();
                if (posFacing.isPresent() && getTankSorption(posFacing.get(), 0).canDrain()) {
                    pushFluidToSide(posFacing.get());
                }
            }
        }
    }

    // Fluids

    @Override
    public @Nonnull List<Tank> getTanks() {
        HeatExchangerLogic logic = getLogic();
        return logic == null ? Collections.emptyList() : logic.getOutletTanks(network);
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
    public void pushFluidToSide(@Nonnull Direction side) {
        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null) {
            return;
        }

        if (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo()) {
            return;
        }

        IFluidHandler adjStorage = level.getCapability(Capabilities.FluidHandler.BLOCK, tile.getBlockPos(), side.getOpposite());
        if (adjStorage == null) {
            return;
        }

        List<Tank> tanks = getTanks();
        if (!tanks.isEmpty()) {
            Tank tank = tanks.get(0);
            onWrapperDrain(tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), SIMULATE), EXECUTE), EXECUTE), EXECUTE);
        }
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
}