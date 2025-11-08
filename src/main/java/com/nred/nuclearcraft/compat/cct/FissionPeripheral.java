package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.turbine.TurbineDynamoEntityPart;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.util.CCHelper;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.jspecify.annotations.Nullable;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public final class FissionPeripheral extends MultiblockPeripheral<FissionReactor> implements IPeripheral {
    public FissionPeripheral(FissionComputerPortEntity turbineComputerPort) {
        super(turbineComputerPort);
    }

    @LuaFunction(mainThread = true)
    public boolean isReactorOn() {
        if (!test()) return false;
        return getMultiblock().isReactorOn;
    }

    @LuaFunction(mainThread = true)
    public long getHeatStored() {
        if (!test()) return 0;
        return getMultiblock().getLogic().heatBuffer.getHeatStored();
    }

    @LuaFunction(mainThread = true)
    public long getHeatCapacity() {
        if (!test()) return 0;
        return getMultiblock().getLogic().heatBuffer.getHeatCapacity();
    }

    @LuaFunction(mainThread = true)
    public double getTemperature() {
        if (!test()) return 0;
        return getMultiblock().getLogic().getTemperature();
    }

    @LuaFunction(mainThread = true)
    public double getCoolingRate() {
        if (!test()) return 0;
        return getMultiblock().cooling;
    }

    @LuaFunction(mainThread = true)
    public double getRawHeatingRate() {
        if (!test()) return 0;
        return getMultiblock().rawHeating;
    }

    @LuaFunction(mainThread = true)
    public double getMeanEfficiency() {
        if (!test()) return 0;
        return getMultiblock().meanEfficiency;
    }

    @LuaFunction(mainThread = true)
    public double getMeanHeatMultiplier() {
        if (!test()) return 0;
        return getMultiblock().meanHeatMult;
    }

    @LuaFunction(mainThread = true)
    public double getNumberOfDynamoParts() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(TurbineDynamoEntityPart.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfIrradiators() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(FissionIrradiatorEntity.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfCells() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(SolidFissionCellEntity.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfSinks() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(SolidFissionHeatSinkEntity.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfVessels() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(SaltFissionVesselEntity.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfHeaters() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(SaltFissionHeaterEntity.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfShields() {
        if (!test()) return 0;
        return getMultiblock().getPartCount(FissionShieldEntity.class);
    }

    private <T extends IFissionComponent> Object[] getPartStats(Class<T> type) {
        if (!test()) return new Object[]{};
        return getMultiblock().getParts(type).stream().map(IFissionComponent::getCCInfo).toArray(Object[]::new);
    }

    @LuaFunction(mainThread = true)
    public Object[] getIrradiatorStats() {
        return getPartStats(FissionIrradiatorEntity.class);
    }

    @LuaFunction(mainThread = true)
    public Object[] getCellStats() {
        return getPartStats(SolidFissionCellEntity.class);
    }

    @LuaFunction(mainThread = true)
    public Object[] getSinkStats() {
        return getPartStats(SolidFissionHeatSinkEntity.class);
    }

    @LuaFunction(mainThread = true)
    public Object[] getVesselStats() {
        return getPartStats(SaltFissionVesselEntity.class);
    }

    @LuaFunction(mainThread = true)
    public Object[] getHeaterStats() {
        return getPartStats(SaltFissionHeaterEntity.class);
    }

    @LuaFunction(mainThread = true)
    public Object[] getShieldStats() {
        return getPartStats(FissionShieldEntity.class);
    }

    @LuaFunction(mainThread = true)
    public int getNumberOfClusters() {
        if (!test()) return 0;
        return getMultiblock().clusterCount;
    }

    @LuaFunction(mainThread = true)
    public Object[] getClusterInfo(int clusterID) {
        FissionCluster cluster;
        if (!test() || clusterID >= getMultiblock().clusterCount || (cluster = getMultiblock().getClusterMap().get(clusterID)) == null) {
            return new Object[]{new Object[]{}};
        }

        Object2ObjectMap<String, Object> infoMap = new Object2ObjectLinkedOpenHashMap<>();
        infoMap.put("heat_capacity", cluster.heatBuffer.getHeatCapacity());
        infoMap.put("heat_stored", cluster.heatBuffer.getHeatStored());
        infoMap.put("cooling", cluster.cooling);
        infoMap.put("raw_heating", cluster.rawHeating);

        Object2ObjectMap<String, Object2ObjectMap<Object, Object>> componentsMap = new Object2ObjectLinkedOpenHashMap<>();
        for (Long2ObjectMap.Entry<IFissionComponent> entry : cluster.getComponentMap().long2ObjectEntrySet()) {
            IFissionComponent component = entry.getValue();
            String key = component.getCCKey();
            Object2ObjectMap<Object, Object> entryMap = componentsMap.get(key);
            if (entryMap == null) {
                componentsMap.put(key, entryMap = new Object2ObjectLinkedOpenHashMap<>());
            }
            entryMap.put(CCHelper.posInfo(entry.getLongKey()), component.getCCInfo());
        }
        infoMap.put("components", componentsMap);

        return new Object[]{infoMap};
    }

    @Override
    public String getType() {
        return ncLoc("multiblock").toString();
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof FissionPeripheral && ((FissionPeripheral) other).computerPort.equals(computerPort);
    }
}