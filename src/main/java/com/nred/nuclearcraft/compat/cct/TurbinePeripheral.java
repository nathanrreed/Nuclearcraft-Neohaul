package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.turbine.TurbineComputerPortEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineDynamoCoilEntity;
import com.nred.nuclearcraft.block_entity.turbine.TurbineDynamoEntityPart;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.util.CCHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

@SuppressWarnings("unused")
public final class TurbinePeripheral extends MultiblockPeripheral<Turbine> implements IPeripheral {
    public TurbinePeripheral(TurbineComputerPortEntity computerPort) {
        super(computerPort);
    }

    @LuaFunction(mainThread = true)
    public boolean isTurbineOn() {
        if (!test()) return false;
        return getMultiblock().isTurbineOn;
    }

    @LuaFunction(mainThread = true)
    public boolean isProcessing() {
        if (!test()) return false;
        return getMultiblock().isProcessing;
    }

    @LuaFunction(mainThread = true)
    public int getEnergyStored() {
        if (!test()) return 0;
        return getMultiblock().energyStorage.getEnergyStored();
    }

    @LuaFunction(mainThread = true)
    public int getEnergyCapacity() {
        if (!test()) return 0;
        return getMultiblock().energyStorage.getMaxEnergyStored();
    }

    @LuaFunction(mainThread = true)
    public double getPower() {
        if (!test()) return 0;
        return getMultiblock().power;
    }

    @LuaFunction(mainThread = true)
    public double getInputRate() {
        if (!test()) return 0;
        return getMultiblock().recipeInputRate;
    }

    @LuaFunction(mainThread = true)
    public double getNumberOfDynamoParts() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(TurbineDynamoEntityPart.class);
    }

    @LuaFunction(mainThread = true)
    public double getCoilConductivity() {
        if (!test()) return 0;
        return getMultiblock().conductivity;
    }

    @LuaFunction(mainThread = true)
    public double getTotalExpansionLevel() {
        if (!test()) return 0;
        return getMultiblock().totalExpansionLevel;
    }

    @LuaFunction(mainThread = true)
    public double getIdealTotalExpansionLevel() {
        if (!test()) return 0;
        return getMultiblock().idealTotalExpansionLevel;
    }

    @LuaFunction(mainThread = true)
    public String getFlowDirection() {
        if (!test()) return "null";
        return getMultiblock().flowDir.getName();
    }

    @LuaFunction(mainThread = true)
    public Double[] getExpansionLevels() {
        if (!test()) return new Double[]{};
        return getMultiblock().expansionLevels.toArray(Double[]::new);
    }

    @LuaFunction(mainThread = true)
    public Double[] getIdealExpansionLevels() {
        if (!test()) return new Double[]{};
        return getMultiblock().getLogic().getIdealExpansionLevels().toArray(Double[]::new);
    }

    @LuaFunction(mainThread = true)
    public Double[] getBladeEfficiencies() {
        if (!test()) return new Double[]{};
        return getMultiblock().rawBladeEfficiencies.toArray(Double[]::new);
    }

    @LuaFunction(mainThread = true)
    public Object[] getDynamoPartStats() {
        if (test()) {
            List<Object[]> stats = new ArrayList<>();
            for (TurbineDynamoCoilEntity dynamoPart : getMultiblock().getParts(TurbineDynamoCoilEntity.class)) {
                BlockPos pos = dynamoPart.getBlockPos();
                stats.add(new Object[]{CCHelper.posInfo(pos), dynamoPart.dynamoCoilType.getName(), dynamoPart.isInValidPosition});
            }
            return new Object[]{stats.toArray()};
        } else {
            return new Object[]{};
        }
    }

    @LuaFunction(mainThread = true)
    public void activate() {
        if (test()) {
            getMultiblock().computerActivated = true;
            getMultiblock().getLogic().setIsTurbineOn();
        }
    }

    @LuaFunction(mainThread = true)
    public void deactivate() {
        if (test()) {
            getMultiblock().computerActivated = false;
            getMultiblock().getLogic().setIsTurbineOn();
        }
    }

    @LuaFunction(mainThread = true)
    public Object[] getTanks() {
        List<Object> map = new ArrayList<>();
        if (test()) {
            for (Tank tank : getMultiblock().tanks) {
                Object2ObjectMap<String, Object> infoMap = new Object2ObjectLinkedOpenHashMap<>();
                infoMap.put("name", tank.getFluidId().toString());
                infoMap.put("amount", tank.getFluidAmount());
                map.add(infoMap);
            }
        }
        return new Object[]{map};
    }

    @Override
    public String getType() {
        return ncLoc("turbine").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof TurbinePeripheral && ((TurbinePeripheral) other).computerPort.equals(computerPort);
    }
}