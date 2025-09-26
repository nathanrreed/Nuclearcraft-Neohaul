package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block.processor.ProcessorEntity;
import com.nred.nuclearcraft.helpers.SideConfigEnums;
import com.nred.nuclearcraft.util.OCHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record ProcessorPeripheral(ProcessorEntity processorEntity) implements IPeripheral {
    //    @LuaFunction(mainThread = true)
//    public Object[] getIsProcessing() {
//        return new Object[]{processorEntity.getIsProcessing()};
//    }

    @LuaFunction(mainThread = true)
    public int getCurrentTime() {
        return processorEntity.progress;
    }

    @LuaFunction(mainThread = true)
    public int getBaseProcessTime() {
        return PROCESSOR_CONFIG_MAP.get(processorEntity.typeName).base_time();
    }

    @LuaFunction(mainThread = true)
    public int getBaseProcessPower() {
        return PROCESSOR_CONFIG_MAP.get(processorEntity.typeName).base_power();
    }

    @LuaFunction(mainThread = true)
    public Object[] getItemInputs() {
        return new Object[]{OCHelper.stackInfoArray(processorEntity.getItemInputs())};
    }

    @LuaFunction(mainThread = true)
    public Object[] getFluidInputs() {
        return new Object[]{OCHelper.fluidInfoArray(processorEntity.getFluidInputs())};
    }

    @LuaFunction(mainThread = true)
    public Object[] getItemOutputs() {
        return new Object[]{OCHelper.stackInfoArray(processorEntity.getItemOutputs())};
    }

    @LuaFunction(mainThread = true)
    public Object[] getFluidOutputs() {
        return new Object[]{OCHelper.fluidInfoArray(processorEntity.getFluidOutputs())};
    }

    @LuaFunction(mainThread = true)
    public void setItemSorption(String direction, int index, String mode) {
        processorEntity.itemStackHandler.sideConfig.get(Direction.valueOf(direction)).set(index, SideConfigEnums.SideConfigSetting.valueOf(mode));
    }

    @LuaFunction(mainThread = true)
    public void setFluidSorption(String direction, int index, String mode) {
        processorEntity.fluidHandler.sideConfig.get(Direction.valueOf(direction)).set(index, SideConfigEnums.SideConfigSetting.valueOf(mode));
    }

//    @LuaFunction(mainThread = true)
//    public Object[] haltProcess() {
//        fullHalt = true;
//        return new Object[]{};
//    }
//
//    @LuaFunction(mainThread = true)
//    public Object[] resumeProcess() {
//        fullHalt = false;
//        return new Object[]{};
//    }

    @Override
    public String getType() {
        return ncLoc(processorEntity.typeName).toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof ProcessorPeripheral && ((ProcessorPeripheral) other).processorEntity.equals(processorEntity);
    }
}
