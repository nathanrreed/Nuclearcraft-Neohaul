package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.block.fission.FissionShieldEntity;
import com.nred.nuclearcraft.block.fission.IFissionComponent;
import com.nred.nuclearcraft.block.fission.IFissionFuelComponent;
import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.helpers.Tank;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity.SyncReason;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public abstract class MultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>> implements IMultiblockLogic<MULTIBLOCK, LOGIC> {
    public final MULTIBLOCK multiblock;
    public final Random rand = new Random();

    public MultiblockLogic(MULTIBLOCK multiblock) {
        this.multiblock = multiblock;
    }

    public abstract void onMachineAssembled();

    public abstract void onMachineRestored();

    public abstract void onMachinePaused();

    public abstract void onMachineDisassembled();

    public abstract void onAssimilate(MULTIBLOCK assimilated);

    public abstract void onAssimilated(MULTIBLOCK assimilator);

    public abstract boolean isMachineWhole();

    public abstract boolean onUpdateServer();

    public abstract void onUpdateClient();

    public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {
    }

    public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
        IFissionComponent component = multiblock.getPartMap(IFissionComponent.class).get(pos.asLong());
        return component instanceof IFissionFuelComponent ? (IFissionFuelComponent) component : null;
    }

    public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent, boolean simulate) {
    }

    public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {
    }

    public boolean isShieldActiveModerator(FissionShieldEntity shield, boolean activeModeratorPos) {
        return false;
    }

    public ModeratorBlockInfo getShieldModeratorBlockInfo(FissionShieldEntity shield, boolean validActiveModerator) {
        return new ModeratorBlockInfo(shield.getBlockPos(), shield, shield.isShielding, validActiveModerator, 0, shield.efficiency);
    }

    public @Nonnull EnergyStorage getPowerPortEnergyStorage(EnergyStorage backupStorage) {
        return backupStorage;
    }

    public int getPowerPortEUSinkTier() {
        return 10;
    }

    public int getPowerPortEUSourceTier() {
        return 1;
    }

    public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
        return backupTanks;
    }

    public abstract List<Pair<Class<? extends IMultiblockPart<MULTIBLOCK>>, String>> getPartBlacklist();

    public boolean containsBlacklistedPart() {
        for (Pair<Class<? extends IMultiblockPart<MULTIBLOCK>>, String> pair : getPartBlacklist()) {
            for (long posLong : multiblock.getPartMap(pair.getLeft()).keySet()) {
                multiblock.setLastError(pair.getRight(), BlockPos.of(posLong));
                return true;
            }
        }
        return false;
    }

    // NBT
    public abstract void writeToLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason);

    public abstract void readFromLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason);

    // Utility Methods

//    @SuppressWarnings("unchecked") TODO
//    public <PORT extends ITilePort<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, PRT extends T, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, T, PORT, TARGET> & ITileFiltered, TRGT extends T> void refreshFilteredPorts(Class<PORT> portClass, Class<TARGET> targetClass) {
//        Map<Long, PORT> portMap = multiblock.getPartMap(portClass.asSubclass(multiblock.getClass()));
//        Map<Long, TARGET> targetMap = multiblock.getPartMap(targetClass.asSubclass(multiblock.getClass()));
//
//        for (TARGET target : targetMap.values()) {
//            target.clearMasterPort();
//        }
//
//        Object2ObjectMap<Object, PORT> masterPortMap = new Object2ObjectOpenHashMap<>();
//        Object2IntMap<Object> targetCountMap = new Object2IntOpenHashMap<>();
//        for (PORT port : portMap.values()) {
//            Object filter = port.getFilterKey();
//            if (PosHelper.DEFAULT_NON.equals(port.getMasterPortPos()) && !masterPortMap.containsKey(filter)) {
//                masterPortMap.put(filter, port);
//                targetCountMap.put(filter, 0);
//            }
//            port.clearMasterPort();
//            port.getTargets().clear();
//        }
//
//        if (!multiblock.isAssembled() || portMap.isEmpty()) {
//            return;
//        }
//
//        for (PORT port : portMap.values()) {
//            Object filter = port.getFilterKey();
//            if (!masterPortMap.containsKey(filter)) {
//                masterPortMap.put(filter, port);
//                targetCountMap.put(filter, 0);
//            }
//        }
//
//        for (PORT port : portMap.values()) {
//            Object filter = port.getFilterKey();
//            PORT master = masterPortMap.get(filter);
//            if (port != master) {
//                port.setMasterPortPos(master.getTilePos());
//                port.refreshMasterPort();
//                port.setInventoryStackLimit(64);
//                port.setTankCapacity(port.getTankBaseCapacity());
//            }
//        }
//
//        for (TARGET target : targetMap.values()) {
//            Object filter = target.getFilterKey();
//            if (masterPortMap.containsKey(filter)) {
//                PORT master = masterPortMap.get(filter);
//                if (master != null) {
//                    master.getTargets().add(target);
//                    target.setMasterPortPos(master.getTilePos());
//                    target.refreshMasterPort();
//                    targetCountMap.put(filter, targetCountMap.get(filter) + 1);
//                }
//            }
//        }
//
//        for (Object2ObjectMap.Entry<Object, PORT> entry : masterPortMap.object2ObjectEntrySet()) {
//            entry.getValue().setInventoryStackLimit(Math.max(64, entry.getValue().getInventoryStackLimitPerConnection() * targetCountMap.get(entry.getKey())));
//            entry.getValue().setTankCapacity(Math.max(entry.getValue().getTankBaseCapacity(), entry.getValue().getTankCapacityPerConnection() * targetCountMap.get(entry.getKey())));
//        }
//    }
//
//    public <MANAGER extends ITileManager<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, T, MANAGER, LISTENER>> void refreshManagers(Class<MANAGER> managerClass) {
//        for (MANAGER manager : ((Long2ObjectMap<MANAGER>) getPartMap(managerClass.asSubclass(multiblock.tClass))).values()) {
//            manager.refreshManager();
//        }
//    }
}