package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.block_entity.*;
import com.nred.nuclearcraft.block_entity.fission.FissionShieldEntity;
import com.nred.nuclearcraft.block_entity.fission.IFissionComponent;
import com.nred.nuclearcraft.block_entity.fission.IFissionFuelComponent;
import com.nred.nuclearcraft.block_entity.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.util.PosHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity.SyncReason;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class MultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>> implements IMultiblockLogic<MULTIBLOCK, LOGIC> {
    public final MULTIBLOCK multiblock;
    public final Random rand = new Random();

    public MultiblockLogic(MULTIBLOCK multiblock) {
        this.multiblock = multiblock;
    }

    @Override
    public Level getWorld() {
        return multiblock.getWorld();
    }

    // Multiblock Size Limits

    public abstract int getMinimumInteriorLength();

    public abstract int getMaximumInteriorLength();

    // Multiblock Methods
    public void onBlockAdded(IMultiblockPart<MULTIBLOCK> newPart) {
    }

    public void onBlockRemoved(IMultiblockPart<MULTIBLOCK> oldPart) {
    }

    public abstract void onMachineAssembled();

    public abstract void onMachineRestored();

    public abstract void onMachinePaused();

    public abstract void onMachineDisassembled();

    public abstract void onAssimilate(IMultiblockController<MULTIBLOCK> assimilated);

    public abstract void onAssimilated(IMultiblockController<MULTIBLOCK> assimilator);

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
                multiblock.setLastError(BlockPos.of(posLong), pair.getRight());
                return true;
            }
        }
        return false;
    }

    // Multiblock Part Helpers
    public <TYPE> Map<Long, TYPE> getPartMap(Class<TYPE> type) {
        return multiblock.getPartMap(type);
    }

    public <TYPE> int getPartCount(Class<TYPE> type) {
        return getPartMap(type).size();
    }

    public <TYPE> Collection<TYPE> getParts(Class<TYPE> type) {
        return getPartMap(type).values();
    }

    public <TYPE> Iterator<TYPE> getPartIterator(Class<TYPE> type) {
        return getParts(type).iterator();
    }

    // Utility Methods

    public <PORT extends ITilePort<MULTIBLOCK, LOGIC, PORT, TARGET> & ITileFiltered, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, PORT, TARGET> & ITileFiltered> void refreshFilteredPorts(Class<PORT> portClass, Class<TARGET> targetClass) {
        Map<Long, PORT> portMap = multiblock.getPartMap(portClass);
        Map<Long, TARGET> targetMap = multiblock.getPartMap(targetClass);

        for (TARGET target : targetMap.values()) {
            target.clearMasterPort();
        }

        Object2ObjectMap<Object, PORT> masterPortMap = new Object2ObjectOpenHashMap<>();
        Object2IntMap<Object> targetCountMap = new Object2IntOpenHashMap<>();
        for (PORT port : portMap.values()) {
            Object filter = port.getFilterKey();
            if (PosHelper.DEFAULT_NON.equals(port.getMasterPortPos()) && !masterPortMap.containsKey(filter)) {
                masterPortMap.put(filter, port);
                targetCountMap.put(filter, 0);
            }
            port.clearMasterPort();
            port.getTargets().clear();
        }

        if (!multiblock.isAssembled() || portMap.isEmpty()) {
            return;
        }

        for (PORT port : portMap.values()) {
            Object filter = port.getFilterKey();
            if (!masterPortMap.containsKey(filter)) {
                masterPortMap.put(filter, port);
                targetCountMap.put(filter, 0);
            }
        }

        for (PORT port : portMap.values()) {
            Object filter = port.getFilterKey();
            PORT master = masterPortMap.get(filter);
            if (port != master) {
                port.setMasterPortPos(master.getTilePos());
                port.refreshMasterPort();
                port.setInventoryStackLimit(64);
                port.setTankCapacity(port.getTankBaseCapacity());
            }
        }

        for (TARGET target : targetMap.values()) {
            Object filter = target.getFilterKey();
            if (masterPortMap.containsKey(filter)) {
                PORT master = masterPortMap.get(filter);
                if (master != null) {
                    master.getTargets().add(target);
                    target.setMasterPortPos(master.getTilePos());
                    target.refreshMasterPort();
                    targetCountMap.put(filter, targetCountMap.get(filter) + 1);
                }
            }
        }

        for (Object2ObjectMap.Entry<Object, PORT> entry : masterPortMap.object2ObjectEntrySet()) {
            entry.getValue().setInventoryStackLimit(Math.max(64, entry.getValue().getInventoryStackLimitPerConnection() * targetCountMap.get(entry.getKey())));
            entry.getValue().setTankCapacity(Math.max(entry.getValue().getTankBaseCapacity(), entry.getValue().getTankCapacityPerConnection() * targetCountMap.get(entry.getKey())));
        }
    }

    public <MANAGER extends ITileManager<MULTIBLOCK, LOGIC, MANAGER, LISTENER>, LISTENER extends ITileManagerListener<MULTIBLOCK, LOGIC, MANAGER, LISTENER>> void refreshManagers(Class<MANAGER> managerClass) {
        for (MANAGER manager : getPartMap(managerClass).values()) {
            manager.refreshManager();
        }
    }

    // NBT
    public abstract void writeToLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason);

    public abstract void readFromLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason);

    public CompoundTag writeStacks(NonNullList<ItemStack> stacks, CompoundTag data, HolderLookup.Provider registries) {
        ContainerHelper.saveAllItems(data, stacks, registries);
        return data;
    }

    public void readStacks(NonNullList<ItemStack> stacks, CompoundTag data, HolderLookup.Provider registries) {
        ContainerHelper.loadAllItems(data, stacks, registries);
    }

    public CompoundTag writeTanks(List<Tank> tanks, CompoundTag data, HolderLookup.Provider registries, String name) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).writeToNBT(data, registries, name + i);
        }
        return data;
    }

    public void readTanks(List<Tank> tanks, CompoundTag data, HolderLookup.Provider registries, String name) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).readFromNBT(data, registries, name + i);
        }
    }

    public CompoundTag writeEnergy(com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage storage, CompoundTag data, HolderLookup.Provider registries, String string) {
        storage.writeToNBT(data, registries, string);
        return data;
    }

    public void readEnergy(com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage storage, CompoundTag data, HolderLookup.Provider registries, String string) {
        storage.readFromNBT(data, registries, string);
    }

    // Multiblock Validators

    public boolean isBlockGoodForInterior(Level level, int x, int y, int z) {
        return true;
    }

    // Clear Material

    public abstract void clearAllMaterial();
}