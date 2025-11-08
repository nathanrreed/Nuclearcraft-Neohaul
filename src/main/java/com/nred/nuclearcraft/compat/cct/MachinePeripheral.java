package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.machine.MachineComputerPortEntity;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.util.CCHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public final class MachinePeripheral extends MultiblockPeripheral<Machine> implements IPeripheral {
    public MachinePeripheral(MachineComputerPortEntity computerPort) {
        super(computerPort);
    }

    @LuaFunction(mainThread = true)
    public boolean isMachineOn() {
        if (!test()) return false;
        return getMultiblock().isMachineOn;
    }

    @LuaFunction(mainThread = true)
    public boolean getIsProcessing() {
        if (!test()) return false;
        return getMultiblock().processor.isProcessing;
    }

    @LuaFunction(mainThread = true)
    public double getCurrentTime() {
        if (!test()) return 0;
        return getMultiblock().processor.time;
    }

    @LuaFunction(mainThread = true)
    public double getBaseProcessTime() {
        if (!test()) return 0;
        return getMultiblock().getLogic().getProcessTimeFP();
    }

    @LuaFunction(mainThread = true)
    public Object[] getItemInputs() {
        if (!test()) return new Object[]{};
        return CCHelper.stackInfoArray(getMultiblock().processor.getItemInputs(false));
    }

    @LuaFunction(mainThread = true)
    public Object[] getFluidInputs() {
        if (!test()) return new Object[]{};
        return new Object[]{CCHelper.tankInfoArray(getMultiblock().processor.getFluidInputs(false))};
    }

    @LuaFunction(mainThread = true)
    public Object[] getItemOutputs() {
        if (!test()) return new Object[]{};
        return new Object[]{CCHelper.stackInfoArray(getMultiblock().processor.getItemOutputs())};
    }

    @LuaFunction(mainThread = true)
    public Object[] getFluidOutputs() {
        if (!test()) return new Object[]{};
        return new Object[]{CCHelper.tankInfoArray(getMultiblock().processor.getFluidOutputs())};
    }

    @LuaFunction(mainThread = true)
    public void haltProcess() {
        if (test()) {
            getMultiblock().fullHalt = true;
        }
    }

    @LuaFunction(mainThread = true)
    public void resumeProcess() {
        if (test()) {
            getMultiblock().fullHalt = false;
        }
    }

    @Override
    public String getType() {
        return ncLoc("machine").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof MachinePeripheral && ((MachinePeripheral) other).computerPort.equals(computerPort);
    }
}