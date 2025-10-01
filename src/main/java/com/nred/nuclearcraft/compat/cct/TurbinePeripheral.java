package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block.turbine.TurbineComputerPortEntity;
import com.nred.nuclearcraft.block.turbine.TurbineDynamoCoilEntity;
import com.nred.nuclearcraft.block.turbine.TurbineDynamoEntityPart;
import com.nred.nuclearcraft.util.OCHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public record TurbinePeripheral(TurbineComputerPortEntity turbineComputerPort) implements IPeripheral {
    private boolean test() {
        return turbineComputerPort.isMachineAssembled() && turbineComputerPort.getMultiblockController().isPresent();
    }

    @LuaFunction(mainThread = true)
    public boolean isComplete() {
        return turbineComputerPort.isMachineAssembled();
    }

    @LuaFunction(mainThread = true)
    public boolean isTurbineOn() {
        if (!test()) return false;
        return turbineComputerPort.getMultiblockController().get().isTurbineOn;
    }

    @LuaFunction(mainThread = true)
    public boolean isProcessing() {
        if (!test()) return false;
        return turbineComputerPort.getMultiblockController().get().isProcessing;
    }

    @LuaFunction(mainThread = true)
    public int getLengthX() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().getInteriorLengthX();
    }

    @LuaFunction(mainThread = true)
    public int getLengthY() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().getInteriorLengthY();
    }

    @LuaFunction(mainThread = true)
    public int getLengthZ() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().getInteriorLengthZ();
    }

    @LuaFunction(mainThread = true)
    public int getEnergyStored() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().energyStorage.getEnergyStored();
    }

    @LuaFunction(mainThread = true)
    public int getEnergyCapacity() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().energyStorage.getMaxEnergyStored();
    }

    @LuaFunction(mainThread = true)
    public double getPower() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().power;
    }

    @LuaFunction(mainThread = true)
    public double getInputRate() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().recipeInputRate;
    }

    @LuaFunction(mainThread = true)
    public double getNumberOfDynamoParts() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().getPartCount(TurbineDynamoEntityPart.class);
    }

    @LuaFunction(mainThread = true)
    public double getCoilConductivity() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().conductivity;
    }

    @LuaFunction(mainThread = true)
    public double getTotalExpansionLevel() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().totalExpansionLevel;
    }

    @LuaFunction(mainThread = true)
    public double getIdealTotalExpansionLevel() {
        if (!test()) return 0;
        return turbineComputerPort.getMultiblockController().get().idealTotalExpansionLevel;
    }

    @LuaFunction(mainThread = true)
    public String getFlowDirection() {
        if (!test()) return "null";
        return turbineComputerPort.getMultiblockController().get().flowDir.getName();
    }

    @LuaFunction(mainThread = true)
    public Double[] getExpansionLevels() {
        if (!test()) return new Double[]{};
        return turbineComputerPort.getMultiblockController().get().expansionLevels.toArray(Double[]::new);
    }

    @LuaFunction(mainThread = true)
    public Double[] getIdealExpansionLevels() {
        if (!test()) return new Double[]{};
        return turbineComputerPort.getMultiblockController().get().getIdealExpansionLevels().toArray(Double[]::new);
    }

    @LuaFunction(mainThread = true)
    public Double[] getBladeEfficiencies() {
        if (!test()) return new Double[]{};
        return turbineComputerPort.getMultiblockController().get().rawBladeEfficiencies.toArray(Double[]::new);
    }

    @LuaFunction(mainThread = true)
    public Object[] getDynamoPartStats() {
        if (test()) {
            List<Object[]> stats = new ArrayList<>();
            for (TurbineDynamoCoilEntity dynamoPart : turbineComputerPort.getMultiblockController().get().getParts(TurbineDynamoCoilEntity.class)) {
                BlockPos pos = dynamoPart.getBlockPos();
                stats.add(new Object[]{OCHelper.posInfo(pos), dynamoPart.dynamoCoilType.getName(), dynamoPart.isInValidPosition});
            }
            return new Object[]{stats.toArray()};
        } else {
            return new Object[]{};
        }
    }

    @LuaFunction(mainThread = true)
    public void activate() {
        if (test()) {
            turbineComputerPort.getMultiblockController().get().computerActivated = true;
            turbineComputerPort.getMultiblockController().get().setIsTurbineOn();
        }
    }

    @LuaFunction(mainThread = true)
    public void deactivate() {
        if (test()) {
            turbineComputerPort.getMultiblockController().get().computerActivated = false;
            turbineComputerPort.getMultiblockController().get().setIsTurbineOn();
        }
    }

    @LuaFunction(mainThread = true)
    public void clearAllMaterial() {
        if (test()) {
            turbineComputerPort.getMultiblockController().get().clearAllMaterial();
        }
    }

    @Override
    public String getType() {
        return ncLoc("multiblock").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof TurbinePeripheral && ((TurbinePeripheral) other).turbineComputerPort.equals(turbineComputerPort);
    }
}
