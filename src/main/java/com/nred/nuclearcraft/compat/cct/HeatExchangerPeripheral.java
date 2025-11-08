package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerComputerPortEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.multiblock.hx.HeatExchangerTubeNetwork;
import com.nred.nuclearcraft.util.CCHelper;
import com.nred.nuclearcraft.util.StreamHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public final class HeatExchangerPeripheral extends MultiblockPeripheral<HeatExchanger> implements IPeripheral {
    public HeatExchangerPeripheral(HeatExchangerComputerPortEntity computerPort) {
        super(computerPort);
    }

    @LuaFunction(mainThread = true)
    public boolean isExchangerOn() {
        if (!test()) return false;
        return getMultiblock().isExchangerOn;
    }

    @LuaFunction(mainThread = true)
    public int getActiveNetworkCount() {
        if (!test()) return 0;
        return getMultiblock().activeNetworkCount;
    }

    @LuaFunction(mainThread = true)
    public int getActiveTubeCount() {
        if (!test()) return 0;
        return getMultiblock().activeTubeCount;
    }

    @LuaFunction(mainThread = true)
    public double getShellSpeedMultiplier() {
        if (!test()) return 0;
        return getMultiblock().shellSpeedMultiplier;
    }

    @LuaFunction(mainThread = true)
    public double getShellInputRate() {
        if (!test()) return 0;
        return getMultiblock().shellInputRate;
    }

    @LuaFunction(mainThread = true)
    public double getHeatTransferRate() {
        if (!test()) return 0;
        return getMultiblock().heatTransferRate;
    }

    @LuaFunction(mainThread = true)
    public Double[] getMeanTempDiff() {
        if (!test()) return new Double[]{0.0};
        HeatExchanger hx = getMultiblock();
        return new Double[]{hx.activeContactCount == 0 ? 0D : hx.totalTempDiff / hx.activeContactCount};
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfNetworks() {
        if (!test()) return 0;
        return getMultiblock().networks.size();
    }

    @LuaFunction(mainThread = true)
    public Object[] getNetworkStats() {
        if (!test()) return new Object[]{};
        List<Object[]> stats = new ArrayList<>();
        for (HeatExchangerTubeNetwork network : getMultiblock().networks) {
            stats.add(new Object[]{network.usefulTubeCount, CCHelper.vec3Info(network.tubeFlow), CCHelper.vec3Info(network.shellFlow), network.flowCosine, network.baseHeatingMultiplier, network.baseCoolingMultiplier, CCHelper.tankInfoArray(network.getTanks())});
        }
        return new Object[]{stats.toArray()};
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfTubes() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(HeatExchangerTubeEntity.class);
    }

    @LuaFunction(mainThread = true)
    public Object[] getTubeStats() {
        if (test()) {
            List<Object[]> stats = new ArrayList<>();
            for (HeatExchangerTubeEntity tube : getMultiblock().getParts(HeatExchangerTubeEntity.class)) {
                BlockPos pos = tube.getBlockPos();
                stats.add(new Object[]{CCHelper.posInfo(pos), tube.tubeType.getHeatTransferCoefficient(), tube.tubeType.getHeatRetentionMultiplier(), StreamHelper.map(tube.settings, Object::toString, Object[]::new), CCHelper.vec3Info(tube.tubeFlow), CCHelper.vec3Info(tube.shellFlow)});
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
            getMultiblock().getLogic().setIsExchangerOn();
        }
    }

    @LuaFunction(mainThread = true)
    public void deactivate() {
        if (test()) {
            getMultiblock().computerActivated = false;
            getMultiblock().getLogic().setIsExchangerOn();
        }
    }

    @Override
    public String getType() {
        return ncLoc("heat_exchanger").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof HeatExchangerPeripheral && ((HeatExchangerPeripheral) other).computerPort.equals(computerPort);
    }
}