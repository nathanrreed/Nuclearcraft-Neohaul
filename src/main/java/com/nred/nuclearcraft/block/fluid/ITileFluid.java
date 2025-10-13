package com.nred.nuclearcraft.block.fluid;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block.ITile;
import com.nred.nuclearcraft.block.ITilePort;
import com.nred.nuclearcraft.block.internal.fluid.*;
import com.nred.nuclearcraft.block.passive.ITilePassive;
import com.nred.nuclearcraft.block.processor.IProcessor;
import com.nred.nuclearcraft.block.processor.IProcessor.HandlerPair;
import com.nred.nuclearcraft.block.internal.fluid.Tank;
import com.nred.nuclearcraft.util.BlockHelper;
import mekanism.api.Action;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ITileFluid extends ITile {
    // Tanks

    @Nonnull
    List<Tank> getTanks();

    // Tank Logic

    /**
     * Only concerns ordering, not whether fluid is actually valid for the tank due to filters or sorption
     */
    default boolean isNextToFill(@Nonnull Direction side, int tankNumber, FluidStack resource) {
        if (!getInputTanksSeparated()) {
            return true;
        }

        List<Tank> tanks = getTanks();
        for (int i = 0; i < tanks.size(); ++i) {
            Tank tank = tanks.get(i);
            if (i != tankNumber && getTankSorption(side, i).canFill()) {
                FluidStack fluid = tank.getFluid();
                if (FluidStack.isSameFluidSameComponents(fluid, resource)) {
                    return false;
                }
            }
        }
        return true;
    }

    default boolean isFluidValidForTank(int tankNumber, FluidStack stack) {
        return getTanks().get(tankNumber).canFillFluidType(stack);
    }

    default void clearTank(int tankNumber) {
        getTanks().get(tankNumber).setFluidStored(FluidStack.EMPTY);
    }

    default void clearAllTanks() {
        for (Tank tank : getTanks()) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
    }

    // Fluid Connections

    @Nonnull
    FluidConnection[] getFluidConnections();

    void setFluidConnections(@Nonnull FluidConnection[] connections);

    default @Nonnull FluidConnection getFluidConnection(@Nonnull Direction side) {
        return getFluidConnections()[side.ordinal()];
    }

    default @Nonnull TankSorption getTankSorption(@Nonnull Direction side, int tankNumber) {
        return getFluidConnections()[side.ordinal()].getTankSorption(tankNumber);
    }

    default void setTankSorption(@Nonnull Direction side, int tankNumber, @Nonnull TankSorption sorption) {
        getFluidConnections()[side.ordinal()].setTankSorption(tankNumber, sorption);
    }

    default void toggleTankSorption(@Nonnull Direction side, int tankNumber, TankSorption.Type type, boolean reverse) {
        if (!hasConfigurableFluidConnections()) {
            return;
        }
        getFluidConnection(side).toggleTankSorption(tankNumber, type, reverse);
        markDirtyAndNotify(true);
    }

    default boolean canConnectFluid(@Nonnull Direction side) {
        return getFluidConnection(side).canConnect();
    }

    static FluidConnection[] fluidConnectionAll(@Nonnull List<TankSorption> sorptionList) {
        FluidConnection[] array = new FluidConnection[6];
        for (int i = 0; i < 6; ++i) {
            array[i] = new FluidConnection(sorptionList);
        }
        return array;
    }

    static FluidConnection[] fluidConnectionAll(TankSorption sorption) {
        return fluidConnectionAll(Lists.newArrayList(sorption));
    }

    default boolean hasConfigurableFluidConnections() {
        return false;
    }

    // Fluid Wrapper Methods

    default int fill(@Nonnull Direction side, FluidStack resource, FluidAction doFill) {
        List<Tank> tanks = getTanks();
        for (int i = 0; i < tanks.size(); ++i) {
            if (getTankSorption(side, i).canFill()) {
                Tank tank;
                if (isFluidValidForTank(i, resource) && isNextToFill(side, i, resource) && (tank = tanks.get(i)).getFluidAmount() < tank.getCapacity()) {
                    FluidStack fluid = tank.getFluid();
                    if (fluid.isEmpty() || FluidStack.isSameFluidSameComponents(fluid, resource)) {
                        return tank.fill(resource, doFill);
                    }
                }
            }
        }
        return 0;
    }

    default FluidStack drain(@Nonnull Direction side, FluidStack resource, FluidAction doDrain) {
        List<Tank> tanks = getTanks();
        for (int i = 0; i < tanks.size(); ++i) {
            if (getTankSorption(side, i).canDrain()) {
                Tank tank = tanks.get(i);
                if (tank.getFluidAmount() > 0 && FluidStack.isSameFluidSameComponents(resource, tank.getFluid()) && !tank.drain(resource, FluidAction.SIMULATE).isEmpty()) {
                    if (!tank.drain(resource, FluidAction.SIMULATE).isEmpty()) {
                        return tank.drain(resource, doDrain);
                    }
                }
            }
        }
        return FluidStack.EMPTY;
    }

    default FluidStack drain(@Nonnull Direction side, int maxDrain, FluidAction doDrain) {
        List<Tank> tanks = getTanks();
        for (int i = 0; i < tanks.size(); ++i) {
            if (getTankSorption(side, i).canDrain()) {
                Tank tank = tanks.get(i);
                if (tank.getFluidAmount() > 0 && !tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty()) {
                    if (!tank.drain(maxDrain, FluidAction.SIMULATE).isEmpty()) {
                        return tank.drain(maxDrain, doDrain);
                    }
                }
            }
        }
        return FluidStack.EMPTY;
    }

    // Fluid Wrappers

    @Nonnull
    FluidTileWrapper[] getFluidSides();

    default @Nonnull FluidTileWrapper getFluidSide(@Nonnull Direction side) {
        return getFluidSides()[side.ordinal()];
    }

    static @Nonnull FluidTileWrapper[] getDefaultFluidSides(@Nonnull ITileFluid tile) {
        return new FluidTileWrapper[]{new FluidTileWrapper(tile, Direction.DOWN), new FluidTileWrapper(tile, Direction.UP), new FluidTileWrapper(tile, Direction.NORTH), new FluidTileWrapper(tile, Direction.SOUTH), new FluidTileWrapper(tile, Direction.WEST), new FluidTileWrapper(tile, Direction.EAST)};
    }

    default void onWrapperFill(int fillAmount, FluidAction doFill) {
        if (doFill == FluidAction.EXECUTE && fillAmount != 0) {
            if (this instanceof IProcessor<?, ?, ?> processor) {
                processor.refreshRecipe();
                processor.refreshActivity();
            }
//            else if (this instanceof IMachinePart part) { TODO
//                part.refreshMachineRecipe();
//                part.refreshMachineActivity();
//            } else if (this instanceof IHeatExchangerPart part) {
//                part.refreshHeatExchangerRecipe();
//                part.refreshHeatExchangerActivity();
//            }

            if (this instanceof ITilePort<?, ?, ?, ?> port) {
                port.setRefreshTargetsFlag(true);
            }
        }
    }

    default void onWrapperDrain(FluidStack drainStack, FluidAction doDrain) {
        if (doDrain == FluidAction.EXECUTE && drainStack != null && drainStack.getAmount() != 0) {
            if (this instanceof IProcessor<?, ?, ?> processor) {
                processor.refreshActivity();
            }
//            else if (this instanceof IMachinePart part) { TODO
//                part.refreshMachineActivity();
//            } else if (this instanceof IHeatExchangerPart part) {
//                part.refreshHeatExchangerActivity();
//            }

            if (this instanceof ITilePort<?, ?, ?, ?> port) {
                port.setRefreshTargetsFlag(true);
            }
        }
    }

    default void onWrapperReceiveGas(int receiveAmount, Action doTransfer) {
        if (doTransfer.execute() && receiveAmount != 0) {
            if (this instanceof IProcessor<?, ?, ?> processor) {
                processor.refreshRecipe();
                processor.refreshActivity();
            }
//            else if (this instanceof IMachinePart part) { TODO
//                part.refreshMachineRecipe();
//                part.refreshMachineActivity();
//            } else if (this instanceof IHeatExchangerPart part) {
//                part.refreshHeatExchangerRecipe();
//                part.refreshHeatExchangerActivity();
//            }

            if (this instanceof ITilePort<?, ?, ?, ?> port) {
                port.setRefreshTargetsFlag(true);
            }
        }
    }

    default void onWrapperDrawGas(ChemicalStack drawStack, Action doTransfer) {
        if (doTransfer.execute() && drawStack != null && drawStack.getAmount() != 0) {
            if (this instanceof IProcessor<?, ?, ?> processor) {
                processor.refreshActivity();
            }
//            else if (this instanceof IMachinePart part) { TODO
//                part.refreshMachineActivity();
//            } else if (this instanceof IHeatExchangerPart part) {
//                part.refreshHeatExchangerActivity();
//            }

            if (this instanceof ITilePort<?, ?, ?, ?> port) {
                port.setRefreshTargetsFlag(true);
            }
        }
    }

    // Mekanism Chemical Wrapper

    @Nonnull
    ChemicalTileWrapper[] getChemicalSides();

    default @Nonnull ChemicalTileWrapper getChemicalSide(@Nonnull Direction side) {
        if (side == null) return null; // Jade is using null side (We don't want it showing chemical anyway)
        return getChemicalSides()[side.ordinal()];
    }

    static @Nonnull ChemicalTileWrapper[] getDefaultChemicalSides(@Nonnull ITileFluid tile) {
        return new ChemicalTileWrapper[]{new ChemicalTileWrapper(tile, Direction.DOWN), new ChemicalTileWrapper(tile, Direction.UP), new ChemicalTileWrapper(tile, Direction.NORTH), new ChemicalTileWrapper(tile, Direction.SOUTH), new ChemicalTileWrapper(tile, Direction.WEST), new ChemicalTileWrapper(tile, Direction.EAST)};
    }

    // Fluid Distribution

    default void pushFluid() {
        if (getTanks().isEmpty()) {
            return;
        }
        for (Direction side : Direction.values()) {
            pushFluidToSide(side);
        }
    }

    default void pushFluidToSide(@Nonnull Direction side) {
        if (!getFluidConnection(side).canConnect()) {
            return;
        }

        BlockEntity tile = getTileWorld().getBlockEntity(getTilePos().relative(side));
        if (tile == null || (tile instanceof ITilePassive tilePassive && !tilePassive.canPushFluidsTo())) {
            return;
        }

        IFluidHandler adjStorage = getTileWorld().getCapability(Capabilities.FluidHandler.BLOCK, getTilePos(), side.getOpposite());

        if (adjStorage == null) {
            return;
        }

        boolean drained = false;

        List<Tank> tanks = getTanks();
        int tankCount = tanks.size();
        for (int i = 0; i < tankCount; ++i) {
            drained |= pushTankToHandler(adjStorage, tanks, side, i);
        }

        if (drained) {
            if (this instanceof IProcessor<?, ?, ?> processor) {
                processor.refreshActivity();
            }
//            else if (this instanceof IMachinePart part) { TODO
//                part.refreshMachineActivity();
//            } else if (this instanceof IHeatExchangerPart part) {
//                part.refreshHeatExchangerActivity();
//            }

            if (this instanceof ITilePort<?, ?, ?, ?> port) {
                port.setRefreshTargetsFlag(true);
            }
        }
    }

    default boolean pushTankToHandler(IFluidHandler handler, List<Tank> tanks, @Nonnull Direction side, int tankNumber) {
        Tank tank;
        if (!getTankSorption(side, tankNumber).canDrain() || (tank = tanks.get(tankNumber)).getFluid().isEmpty()) {
            return false;
        }

        FluidStack drain = tank.drain(handler.fill(tank.drain(tank.getCapacity(), FluidAction.SIMULATE), FluidAction.EXECUTE), FluidAction.EXECUTE);
        return !drain.isEmpty() && drain.getAmount() != 0;
    }

    default boolean tryPushTank(HandlerPair[] adjacentHandlers, List<Tank> tanks, int tankIndex, List<Direction> dirs, int dirCount, int indexOffset) {
        if (!tanks.get(tankIndex).isEmpty()) {
            for (int i = 0; i < dirCount; ++i) {
                Direction dir = dirs.get((i + indexOffset) % dirCount);
                if (getTankSorption(dir, tankIndex).equals(TankSorption.AUTO_OUT)) {
                    IFluidHandler handler = adjacentHandlers[dir.ordinal()].fluidHandler;
                    if (handler != null && pushTankToHandler(handler, tanks, dir, tankIndex)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // NBT

    default CompoundTag writeTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        List<Tank> tanks = getTanks();
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).writeToNBT(nbt, registries, "tanks" + i);
        }
        return nbt;
    }

    default void readTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        List<Tank> tanks = getTanks();
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).readFromNBT(nbt, registries, "tanks" + i);
        }
    }

    default CompoundTag writeFluidConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        return writeFluidConnectionsDirectional(nbt, registries, null);
    }

    default CompoundTag writeFluidConnectionsDirectional(CompoundTag nbt, HolderLookup.Provider registries, @Nullable Direction facing) {
        for (int i = 0; i < 6; ++i) {
            getFluidConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).writeToNBT(nbt, registries, Direction.from3DDataValue(i));
        }
        return nbt;
    }

    default void readFluidConnections(CompoundTag nbt, HolderLookup.Provider registries) {
        readFluidConnectionsDirectional(nbt, registries, null);
    }

    default void readFluidConnectionsDirectional(CompoundTag nbt, HolderLookup.Provider registries, @Nullable Direction facing) {
        if (!hasConfigurableFluidConnections()) {
            return;
        }
        for (int i = 0; i < 6; ++i) {
            getFluidConnection(BlockHelper.DIR_FROM_FACING[i].apply(facing)).readFromNBT(nbt, registries, Direction.from3DDataValue(i));
        }
    }

    default CompoundTag writeTankSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        nbt.putBoolean("inputTanksSeparated", getInputTanksSeparated());
        int tankCount = getTanks().size();
        for (int i = 0; i < tankCount; ++i) {
            nbt.putBoolean("voidUnusableFluidInput" + i, getVoidUnusableFluidInput(i));
            nbt.putInt("tankOutputSetting" + i, getTankOutputSetting(i).ordinal());
        }
        return nbt;
    }

    default void readTankSettings(CompoundTag nbt, HolderLookup.Provider registries) {
        setInputTanksSeparated(nbt.getBoolean("inputTanksSeparated"));
        int tankCount = getTanks().size();
        for (int i = 0; i < tankCount; ++i) {
            setVoidUnusableFluidInput(i, nbt.getBoolean("voidUnusableFluidInput" + i));
            int ordinal = nbt.contains("voidExcessFluidOutput" + i) ? nbt.getBoolean("voidExcessFluidOutput" + i) ? 1 : 0 : nbt.getInt("tankOutputSetting" + i);
            setTankOutputSetting(i, TankOutputSetting.values()[ordinal]);
        }
    }

    // Fluid Functions

    boolean getInputTanksSeparated();

    void setInputTanksSeparated(boolean separated);

    boolean getVoidUnusableFluidInput(int tankNumber);

    void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput);

    TankOutputSetting getTankOutputSetting(int tankNumber);

    void setTankOutputSetting(int tankNumber, TankOutputSetting setting);

    // Capabilities

    default boolean hasFluidSideCapability(@Nullable Direction side) {
        return side == null || getFluidConnection(side).canConnect();
    }

    default FluidTileWrapper getFluidSideCapability(@Nullable Direction side) {
        if (hasFluidSideCapability(side)) {
            return getFluidSide(nonNullSide(side));
        }
        return null;
    }
    default ChemicalTileWrapper getChemicalCapability(@Nullable Direction side) {
        if (hasFluidSideCapability(side)) {
            return getChemicalSide(side);
        }
        return null;
    }
}