package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.processor.TileProcessorImpl.BasicUpgradableEnergyProcessorEntity;
import com.nred.nuclearcraft.util.CCHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record ProcessorPeripheral(BasicUpgradableEnergyProcessorEntity<?> processorEntity) implements IPeripheral {
    @LuaFunction(mainThread = true)
    public String getComponentName() {
        return processorEntity.getContainerInfo().ocComponentName;
    }

    @LuaFunction(mainThread = true)
    public boolean getIsProcessing() {
        return processorEntity.getIsProcessing();
    }

    @LuaFunction(mainThread = true)
    public double getCurrentTime() {
        return processorEntity.getCurrentTime();
    }

    @LuaFunction(mainThread = true)
    public double getBaseProcessTime() {
        return processorEntity.getBaseProcessTime();
    }

    @LuaFunction(mainThread = true)
    public double getBaseProcessPower() {
        return processorEntity.getBaseProcessPower();
    }


    @LuaFunction(mainThread = true)
    public Object[] getItemInputs() {
        return CCHelper.stackInfoArray(processorEntity.getItemInputs(false));
    }

    @LuaFunction(mainThread = true)
    public Object[] getFluidInputs() {
        return new Object[]{CCHelper.tankInfoArray(processorEntity.getFluidInputs(false))};
    }

    @LuaFunction(mainThread = true)
    public Object[] getItemOutputs() {
        return new Object[]{CCHelper.stackInfoArray(processorEntity.getItemOutputs())};
    }

    @LuaFunction(mainThread = true)
    public Object[] getFluidOutputs() {
        return new Object[]{CCHelper.tankInfoArray(processorEntity.getFluidOutputs())};
    }

    @LuaFunction(mainThread = true)
    public Object[] setItemInputSorption(int a, int b, int c) {
        processorEntity.setItemSorption(Direction.values()[a], processorEntity.getContainerInfo().itemInputSlots[b], ItemSorption.fromInt(ItemSorption.Type.INPUT, c));
        processorEntity.markDirtyAndNotify(true);
        return new Object[]{};
    }

    @LuaFunction(mainThread = true)
    public Object[] setFluidInputSorption(int a, int b, int c) {
        processorEntity.setTankSorption(Direction.values()[a], processorEntity.getContainerInfo().fluidInputTanks[b], TankSorption.fromInt(TankSorption.Type.INPUT, c));
        processorEntity.markDirtyAndNotify(true);
        return new Object[]{};
    }

    @LuaFunction(mainThread = true)
    public Object[] setItemOutputSorption(int a, int b, int c) {
        processorEntity.setItemSorption(Direction.values()[a], processorEntity.getContainerInfo().itemOutputSlots[b], ItemSorption.fromInt(ItemSorption.Type.OUTPUT, c));
        processorEntity.markDirtyAndNotify(true);
        return new Object[]{};
    }

    @LuaFunction(mainThread = true)
    public Object[] setFluidOutputSorption(int a, int b, int c) {
        processorEntity.setTankSorption(Direction.values()[a], processorEntity.getContainerInfo().fluidOutputTanks[b], TankSorption.fromInt(TankSorption.Type.OUTPUT, c));
        processorEntity.markDirtyAndNotify(true);
        return new Object[]{};
    }

    @LuaFunction(mainThread = true)
    public Object[] haltProcess() {
        processorEntity.fullHalt = true;
        return new Object[]{};
    }

    @LuaFunction(mainThread = true)
    public Object[] resumeProcess() {
        processorEntity.fullHalt = false;
        return new Object[]{};
    }

    @Override
    public String getType() {
        return ncLoc(processorEntity.getContainerInfo().name).toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof ProcessorPeripheral && ((ProcessorPeripheral) other).processorEntity.equals(processorEntity);
    }
}