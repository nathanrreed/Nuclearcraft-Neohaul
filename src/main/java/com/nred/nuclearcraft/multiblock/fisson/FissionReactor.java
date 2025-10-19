package com.nred.nuclearcraft.multiblock.fisson;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.fission.FissionMonitorEntity;
import com.nred.nuclearcraft.block_entity.fission.IFissionComponent;
import com.nred.nuclearcraft.block_entity.fission.IFissionController;
import com.nred.nuclearcraft.block_entity.internal.heat.HeatBuffer;
import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.SaltFissionLogic;
import com.nred.nuclearcraft.multiblock.fisson.solid.SolidFuelFissionLogic;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.UnaryOperator;

public class FissionReactor extends Multiblock<FissionReactor> implements ILogicMultiblock<FissionReactor, FissionReactorLogic>, IPacketMultiblock<FissionReactor, FissionUpdatePacket> {
    private static final Logger log = LoggerFactory.getLogger(FissionReactor.class);
    protected final Int2ObjectMap<FissionCluster> clusterMap = new Int2ObjectOpenHashMap<>();
    protected final ObjectSet<FissionCluster> clustersToRefresh = new ObjectOpenHashSet<>();
    public int clusterCount = 0;

    public @Nonnull FissionReactorLogic logic = new FissionReactorLogic(this);
    public final HeatBuffer heatBuffer = new HeatBuffer(FissionReactor.BASE_MAX_HEAT);

    public IFissionController<?> controller;

    public final LongSet passiveModeratorCache = new LongOpenHashSet();
    public final LongSet activeModeratorCache = new LongOpenHashSet();
    public final LongSet activeReflectorCache = new LongOpenHashSet();

    public static final long BASE_MAX_HEAT = 32000;
    public static final int BASE_TANK_CAPACITY = 4000;
    public static final double MAX_TEMP = 2400D;

    public boolean refreshFlag = true, isSimulation = false, isReactorOn = false;
    public double ambientTemp = 290D;
    public int fuelComponentCount = 0;
    public long cooling = 0L, rawHeating = 0L, totalHeatMult = 0L, usefulPartCount = 0L;
    public double meanHeatMult = 0D, totalEfficiency = 0D, meanEfficiency = 0D, sparsityEfficiencyMult = 0D;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public FissionReactor(Level world) {
        super(world);
    }

    @Override
    public FissionReactorLogic getLogic() {
        return logic;
    }

    @Override
    public void setLogic(String logicID) {
        if (logicID.equals(logic.getID())) {
            return;
        }

        UnaryOperator<FissionReactorLogic> constructor = switch (logicID) {
            case "solid_fuel" -> SolidFuelFissionLogic::new;
            case "molten_salt" -> SaltFissionLogic::new;
            default -> throw new IllegalStateException("Unexpected logicID: " + logicID);
        };

        logic = getNewLogic(constructor);
    }

    public Int2ObjectMap<FissionCluster> getClusterMap() {
        return clusterMap;
    }

    public void resetStats() {
        logic.onResetStats();
        fuelComponentCount = 0;
        cooling = rawHeating = totalHeatMult = usefulPartCount = 0L;
        meanHeatMult = totalEfficiency = meanEfficiency = sparsityEfficiencyMult = 0D;
    }

    // Multiblock Size Limits

    @Override
    public int getMinimumInteriorLength() {
        return logic.getMinimumInteriorLength();
    }

    @Override
    public int getMaximumInteriorLength() {
        return logic.getMaximumInteriorLength();
    }

    // Multiblock Methods

    @Override
    protected void onPartAdded(IMultiblockPart<FissionReactor> iMultiblockPart) {
        super.onPartAdded(iMultiblockPart);
        logic.onBlockAdded(iMultiblockPart);
    }

    @Override
    protected void onPartRemoved(IMultiblockPart<FissionReactor> iMultiblockPart) {
        super.onPartRemoved(iMultiblockPart);
        logic.onBlockRemoved(iMultiblockPart);
    }

    @Override
    protected void onMachineAssembled() {
        logic.onMachineAssembled();
    }

    @Override
    protected void onMachineRestored() {
        logic.onMachineRestored();
    }

    @Override
    protected void onMachinePaused() {
        logic.onMachinePaused();
    }

    @Override
    protected void onMachineDisassembled() {
        logic.onMachineDisassembled();
    }

    @Override
    protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
        return setLogic(this) && super.isMachineWhole(validatorCallback) && logic.isMachineWhole();
    }

    public boolean setLogic(FissionReactor multiblock) {
        if (getPartMap(IFissionController.class).isEmpty()) {
            multiblock.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.no_controller");
            return false;
        }
        if (getPartCount(IFissionController.class) > 1) {
            multiblock.setLastError(NuclearcraftNeohaul.MODID + ".multiblock_validation.too_many_controllers");
            return false;
        }

        for (IFissionController<?> contr : getParts(IFissionController.class)) {
            controller = contr;
            break;
        }

        setLogic(controller.getLogicID());

        return true;
    }

    @Override
    protected void onAssimilate(IMultiblockController<FissionReactor> assimilated) {
        logic.onAssimilate(assimilated);
    }

    @Override
    protected void onAssimilated(IMultiblockController<FissionReactor> assimilator) {
        super.onAssimilated(assimilator);
        logic.onAssimilated(assimilator);
    }

    // Cluster Management

    /**
     * Only use when the cluster geometry isn't changed and there is no effect on other clusters!
     */
    public void addClusterToRefresh(FissionCluster cluster) {
        if (cluster != null) {
            clustersToRefresh.add(cluster);
        }
    }

    protected void refreshCluster(FissionCluster cluster, boolean simulate) {
        if (cluster != null && clusterMap.containsKey(cluster.getId())) {
            logic.refreshClusterStats(cluster, simulate);
        }
    }

    protected void sortClusters(boolean simulate) {
        final ObjectSet<FissionCluster> uniqueClusterCache = new ObjectOpenHashSet<>();
        uniqueClusterCache.addAll(clusterMap.values());
        clusterMap.clear();
        int i = 0;
        for (FissionCluster cluster : uniqueClusterCache) {
            cluster.setId(i);
            clusterMap.put(i, cluster);
            ++i;
        }
        clusterCount = clusterMap.size();
    }

    public void mergeClusters(int assimilatorId, FissionCluster targetCluster) {
        if (assimilatorId == targetCluster.getId()) {
            return;
        }
        FissionCluster assimilatorCluster = clusterMap.get(assimilatorId);

        if (targetCluster.connectedToWall) {
            assimilatorCluster.connectedToWall = true;
        }

        for (IFissionComponent component : targetCluster.getComponentMap().values()) {
            component.setCluster(assimilatorCluster);
        }

        assimilatorCluster.heatBuffer.mergeHeatBuffers(targetCluster.heatBuffer);
        targetCluster.getComponentMap().clear();
        clusterMap.remove(targetCluster.getId());
    }

    // Server

    @Override
    protected boolean updateServer() {
        super.updateServer();
        boolean flag = refreshFlag;

        checkRefresh();

        if (logic.onUpdateServer()) {
            flag = true;
        }

        if (controller != null) {
            sendMultiblockUpdatePacketToListeners();
        }

        return flag;
    }

    public void checkRefresh() {
        boolean refreshSimulation = false;

        if (refreshFlag) {
            refreshSimulation = true;
            logic.refreshReactor(false);
            clustersToRefresh.clear();
        } else if (!clustersToRefresh.isEmpty()) {
            refreshSimulation = true;
            for (FissionCluster cluster : clustersToRefresh) {
                refreshCluster(cluster, false);
            }
            logic.refreshReactorStats(false);
            clustersToRefresh.clear();
        }

        if (refreshSimulation) {
            if (isAssembled() && !logic.isReactorActive(false)) {
                logic.refreshReactor(true);
                isSimulation = true;
                clustersToRefresh.clear();
            } else {
                isSimulation = false;
            }
        }

        updateActivity();
    }

    public void updateActivity() {
        boolean wasReactorOn = isReactorOn;
        isReactorOn = isAssembled() && logic.isReactorActive(true);
        if (isReactorOn != wasReactorOn) {
            if (controller != null) {
                controller.setActivity(isReactorOn);
                sendMultiblockUpdatePacketToAll();
            }
            for (FissionMonitorEntity monitor : getParts(FissionMonitorEntity.class)) {
                monitor.setActivity(isReactorOn);
            }
        }
    }

    // Client

    @Override
    protected void updateClient() {
        logic.onUpdateClient();
    }

    // NBT

    @Override
    public @NotNull CompoundTag syncDataTo(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        data.putBoolean("isSimulation", isSimulation);
        data.putBoolean("isReactorOn", isReactorOn);
        data.putInt("clusterCount", clusterCount);
        data.putLong("cooling", cooling);
        data.putLong("rawHeating", rawHeating);
        data.putLong("totalHeatMult", totalHeatMult);
        data.putDouble("meanHeatMult", meanHeatMult);
        data.putInt("fuelComponentCount", fuelComponentCount);
        data.putLong("usefulPartCount", usefulPartCount);
        data.putDouble("totalEfficiency", totalEfficiency);
        data.putDouble("meanEfficiency", meanEfficiency);
        data.putDouble("sparsityEfficiencyMult", sparsityEfficiencyMult);

        writeLogicNBT(data, registries, syncReason);

        return data;
    }

    @Override
    public void syncDataFrom(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        isSimulation = data.getBoolean("isSimulation");
        isReactorOn = data.getBoolean("isReactorOn");
        clusterCount = data.getInt("clusterCount");
        cooling = data.getLong("cooling");
        rawHeating = data.getLong("rawHeating");
        totalHeatMult = data.getLong("totalHeatMult");
        meanHeatMult = data.getDouble("meanHeatMult");
        fuelComponentCount = data.getInt("fuelComponentCount");
        usefulPartCount = data.getLong("usefulPartCount");
        totalEfficiency = data.getDouble("totalEfficiency");
        meanEfficiency = data.getDouble("meanEfficiency");
        sparsityEfficiencyMult = data.getDouble("sparsityEfficiencyMult");

        readLogicNBT(data, registries, syncReason);
    }

    // Packets

    @Override
    public Set<Player> getMultiblockUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public FissionUpdatePacket getMultiblockUpdatePacket() {
        return logic.getMultiblockUpdatePacket();
    }

    @Override
    public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
        isReactorOn = message.isReactorOn;
        clusterCount = message.clusterCount;
        cooling = message.cooling;
        rawHeating = message.rawHeating;
        totalHeatMult = message.totalHeatMult;
        meanHeatMult = message.meanHeatMult;
        fuelComponentCount = message.fuelComponentCount;
        usefulPartCount = message.usefulPartCount;
        totalEfficiency = message.totalEfficiency;
        meanEfficiency = message.meanEfficiency;
        sparsityEfficiencyMult = message.sparsityEfficiencyMult;

        logic.onMultiblockUpdatePacket(message);
    }

    @Override
    protected boolean isBlockGoodForInterior(Level level, int x, int y, int z, IMultiblockValidator iMultiblockValidator) {
        return logic.isBlockGoodForInterior(level, x, y, z);
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
        logic.clearAllMaterial();
        super.clearAllMaterial();

        if (!getWorld().isClientSide) {
            refreshFlag = true;
            checkRefresh();
        }
    }
}