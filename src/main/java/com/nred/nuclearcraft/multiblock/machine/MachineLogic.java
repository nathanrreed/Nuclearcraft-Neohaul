package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyStorage;
import com.nred.nuclearcraft.block_entity.internal.fluid.FluidConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.machine.IMachineController;
import com.nred.nuclearcraft.block_entity.machine.MachineProcessPortEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineRedstonePortEntity;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.SoundHandler;
import com.nred.nuclearcraft.multiblock.IPacketMultiblockLogic;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import com.nred.nuclearcraft.payload.multiblock.MachineRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeUnitInfo;
import com.nred.nuclearcraft.recipe.machine.MultiblockMachine;
import com.nred.nuclearcraft.util.InventoryStackList;
import com.nred.nuclearcraft.util.StreamHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MachineLogic extends MultiblockLogic<Machine, MachineLogic> implements IPacketMultiblockLogic<Machine, MachineLogic, MachineUpdatePacket> {
    public MachineLogic(Machine machine) {
        super(machine);
        constructorInit();
    }

    public MachineLogic(MachineLogic oldLogic) {
        super(oldLogic.multiblock);

        String id = getID();
        if (!id.isEmpty() && !id.equals(oldLogic.getID())) {
            constructorInit();
        }

        if (multiblock.processor.isProcessing) {
            multiblock.refreshSounds = true;
        }
    }

    @Override
    public String getID() {
        return "";
    }

    protected void constructorInit() {
        int volume = multiblock.getExteriorVolume();
        long energyCapacity = volume * energyDensity();
        int tankCapacity = volume * tankDensity();

        multiblock.energyStorage.setStorageCapacity(energyCapacity);
        multiblock.energyStorage.setMaxTransfer(energyCapacity);

        int nextReservoirTankCount = reservoirTankCount(), prevReservoirTankCount = multiblock.reservoirTanks.size();

        List<Set<ResourceLocation>> reservoirValidFluids = getReservoirValidFluids();
        IntFunction<Set<ResourceLocation>> getReservoirValidFluids = x -> reservoirValidFluids != null && x < reservoirValidFluids.size() ? reservoirValidFluids.get(x) : null;

        multiblock.reservoirTanks = IntStream.range(0, nextReservoirTankCount).mapToObj(x -> {
            if (x < prevReservoirTankCount) {
                Tank tank = multiblock.reservoirTanks.get(x);
                tank.setTankCapacity(tankCapacity);
                tank.setAllowedFluids(getReservoirValidFluids.apply(x));
                return tank;
            } else {
                return new Tank(tankCapacity, getReservoirValidFluids.apply(x));
            }
        }).collect(Collectors.toList());

        multiblock.recipeHandler = getRecipeHandler();

        if (multiblock.recipeHandler == null) {
            multiblock.itemInputSize = multiblock.itemOutputSize = multiblock.fluidInputSize = multiblock.fluidOutputSize = 0;
        } else {
            multiblock.itemInputSize = multiblock.recipeHandler.itemInputSize;
            multiblock.itemOutputSize = multiblock.recipeHandler.itemOutputSize;
            multiblock.fluidInputSize = multiblock.recipeHandler.fluidInputSize;
            multiblock.fluidOutputSize = multiblock.recipeHandler.fluidOutputSize;
        }

        InventoryStackList prevInventoryStacks = multiblock.inventoryStacks;

        multiblock.inventoryStacks = InventoryStackList.withSize(inventorySize());

        for (int i = 0, len = Math.min(prevInventoryStacks.size(), multiblock.inventoryStacks.size()); i < len; ++i) {
            multiblock.inventoryStacks.set(i, prevInventoryStacks.get(i));
        }

        int nextTankCount = tankCount(), prevTankCount = multiblock.tanks.size();

        List<Set<ResourceLocation>> validFluids = multiblock.recipeHandler == null ? null : multiblock.recipeHandler.validFluids;
        IntFunction<Set<ResourceLocation>> getValidFluids = x -> validFluids != null && x < validFluids.size() ? validFluids.get(x) : null;

        multiblock.tanks = IntStream.range(0, nextTankCount).mapToObj(x -> {
            if (x < prevTankCount) {
                Tank tank = multiblock.tanks.get(x);
                tank.setTankCapacity(tankCapacity);
                tank.setAllowedFluids(getValidFluids.apply(x));
                return tank;
            } else {
                return new Tank(tankCapacity, getValidFluids.apply(x));
            }
        }).collect(Collectors.toList());

        List<ItemSorption> itemSorptions = new ArrayList<>();
        for (int i = 0; i < multiblock.itemInputSize; ++i) {
            itemSorptions.add(ItemSorption.IN);
        }
        for (int i = 0; i < multiblock.itemOutputSize; ++i) {
            itemSorptions.add(ItemSorption.OUT);
        }

        multiblock.inventoryConnections = StreamHelper.map(itemSorptions, x -> ITileInventory.inventoryConnectionAll(Collections.singletonList(x)));

        List<TankSorption> tankSorptions = new ArrayList<>();
        for (int i = 0; i < multiblock.fluidInputSize; ++i) {
            tankSorptions.add(TankSorption.IN);
        }
        for (int i = 0; i < multiblock.fluidOutputSize; ++i) {
            tankSorptions.add(TankSorption.OUT);
        }

        multiblock.fluidConnections = StreamHelper.map(tankSorptions, x -> ITileFluid.fluidConnectionAll(Collections.singletonList(x)));

        multiblock.processor.baseProcessTime = defaultProcessTime();

        multiblock.baseProcessPower = defaultProcessPower();

        boolean consumesInputs = getConsumesInputs();
        int consumedTankCapacity = multiblock.processor.getInputTankCapacity();

        NonNullList<ItemStack> prevConsumedStacks = multiblock.processor.consumedStacks;

        multiblock.processor.consumedStacks = NonNullList.withSize(consumesInputs ? multiblock.itemInputSize : 0, ItemStack.EMPTY);

        for (int i = 0, len = Math.min(prevConsumedStacks == null ? 0 : prevConsumedStacks.size(), multiblock.processor.consumedStacks.size()); i < len; ++i) {
            multiblock.processor.consumedStacks.set(i, prevConsumedStacks.get(i));
        }

        int prevConsumedTankCount = multiblock.processor.consumedTanks == null ? 0 : multiblock.processor.consumedTanks.size();

        multiblock.processor.consumedTanks = IntStream.range(0, consumesInputs ? multiblock.fluidInputSize : 0).mapToObj(x -> {
            if (x < prevConsumedTankCount) {
                return multiblock.processor.consumedTanks.get(x);
            } else {
                return new Tank(consumedTankCapacity, new ObjectOpenHashSet<>());
            }
        }).collect(Collectors.toList());
    }

    public int reservoirTankCount() {
        return 0;
    }

    public List<Set<ResourceLocation>> getReservoirValidFluids() {
        return null;
    }

    public BasicRecipeHandler<?> getRecipeHandler() {
        return null;
    }

    public double defaultProcessTime() {
        return 1D;
    }

    public double defaultProcessPower() {
        return 0D;
    }

    public long energyDensity() {
        return (long) (Math.ceil(defaultProcessTime()) * Math.ceil(defaultProcessPower()));
    }

    public int inventorySize() {
        return multiblock.itemInputSize + multiblock.itemOutputSize;
    }

    public int combinedInventorySize() {
        return 36 + inventorySize();
    }

    public int tankCount() {
        return multiblock.fluidInputSize + multiblock.fluidOutputSize;
    }

    public int tankDensity() {
        return 1000;
    }

    public boolean getConsumesInputs() {
        return false;
    }

    public boolean getLosesProgress() {
        return false;
    }

    public boolean isGenerator() {
        return false;
    }

    // Multiblock Size Limits

    @Override
    public int getMinimumInteriorLength() {
        return NCConfig.machine_min_size;
    }

    @Override
    public int getMaximumInteriorLength() {
        return NCConfig.machine_max_size;
    }

    // Multiblock Methods

    @Override
    public void onMachineAssembled() {
        onMachineFormed();
    }

    @Override
    public void onMachineRestored() {
        onMachineFormed();
    }

    protected void onMachineFormed() {
        for (IMachineController<?> contr : getParts(IMachineController.class)) {
            multiblock.controller = contr;
            break;
        }

        if (!getWorld().isClientSide()) {
            setupMachine();
            multiblock.processor.refreshAll(getWorld());
            setIsMachineOn(multiblock.processor.isProcessing);

            for (MachineProcessPortEntity port : getParts(MachineProcessPortEntity.class)) {
                port.setItemFluidData();
            }
        }
    }

    protected void setupMachine() {
        int volume = multiblock.getExteriorVolume();
        long energyCapacity = volume * energyDensity();
        int tankCapacity = volume * tankDensity();

        multiblock.energyStorage.setStorageCapacity(energyCapacity);
        multiblock.energyStorage.setMaxTransfer(energyCapacity);

        Consumer<Tank> setupTank = x -> {
            x.setTankCapacity(tankCapacity);
            x.clampTankAmount();
        };
        multiblock.reservoirTanks.forEach(setupTank);
        multiblock.tanks.forEach(setupTank);
        multiblock.processor.consumedTanks.forEach(setupTank);
    }

    @Override
    public void onMachinePaused() {
        onMachineBroken();
    }

    @Override
    public void onMachineDisassembled() {
        onMachineBroken();
    }

    public void onMachineBroken() {
        setIsMachineOn(false);
    }

    @Override
    public boolean isMachineWhole() {
        if (containsBlacklistedPart()) {
            return false;
        }

        for (IMachineController<?> controller : getParts(IMachineController.class)) {
            controller.setIsRenderer(false);
        }
        for (IMachineController<?> controller : getParts(IMachineController.class)) {
            controller.setIsRenderer(true);
            break;
        }

        return true;
    }

    @Override
    public List<Pair<Class<? extends IMultiblockPart<Machine>>, String>> getPartBlacklist() {
        return Collections.emptyList();
    }

    @Override
    public void onAssimilate(IMultiblockController<Machine> machineIMultiblockController) {
        Machine assimilated = (Machine) machineIMultiblockController;
        MachineLogic other = assimilated == null ? null : assimilated.getLogic();
        if (other != null) {
            multiblock.energyStorage.mergeEnergyStorage(assimilated.energyStorage);

            if (getID().equals(other.getID())) {
                for (int i = 0, len = Math.min(multiblock.reservoirTanks.size(), assimilated.reservoirTanks.size()); i < len; ++i) {
                    multiblock.reservoirTanks.get(i).mergeTank(assimilated.reservoirTanks.get(i));
                }
                for (int i = 0, len = Math.min(multiblock.tanks.size(), assimilated.tanks.size()); i < len; ++i) {
                    multiblock.tanks.get(i).mergeTank(assimilated.tanks.get(i));
                }
            }
        }
    }

    @Override
    public void onAssimilated(IMultiblockController<Machine> assimilator) {
    }

    // Server

    @Override
    public boolean onUpdateServer() {
        boolean shouldUpdate = multiblock.processor.onTick(getWorld());

        if (multiblock.controller != null) {
            multiblock.sendRenderPacketToAll();
        }

        return shouldUpdate;
    }

    public void setActivity(boolean isMachineOn) {
        multiblock.controller.setActivity(isMachineOn);
    }

    public void setIsMachineOn(boolean isMachineOn) {
        boolean oldIsMachineOn = multiblock.isMachineOn;
        multiblock.isMachineOn = isMachineOn;
        if (multiblock.isMachineOn != oldIsMachineOn) {
            if (multiblock.controller != null) {
                setActivity(multiblock.isMachineOn);
                multiblock.sendMultiblockUpdatePacketToAll();
            }
        }
    }

    protected void setRecipeStats(@Nullable BasicRecipe recipe) {
        if (recipe == null) {
            multiblock.processor.baseProcessTime = defaultProcessTime();
            multiblock.baseProcessPower = defaultProcessPower();
        } else {
            multiblock.processor.baseProcessTime = ((MultiblockMachine) recipe).getBaseProcessTime(defaultProcessTime());
            multiblock.baseProcessPower = ((MultiblockMachine) recipe).getBaseProcessPower(defaultProcessPower());
        }
    }

    protected long getEnergyCapacity() {
        return multiblock.getExteriorVolume() * energyDensity();
    }

    protected double getSpeedMultiplier() {
        return multiblock.baseSpeedMultiplier;
    }

    protected double getPowerMultiplier() {
        return multiblock.basePowerMultiplier;
    }

    public double getProcessTimeFP() {
        return multiblock.processor.baseProcessTime / getSpeedMultiplier();
    }

    public long getProcessTime() {
        double processTimeFP = getProcessTimeFP();
        return processTimeFP >= Long.MAX_VALUE ? Long.MAX_VALUE : Math.max(1L, (long) Math.ceil(processTimeFP));
    }

    public double getProcessPowerFP() {
        return multiblock.baseProcessPower * getPowerMultiplier();
    }

    public long getProcessPower() {
        double processPowerFP = getProcessPowerFP();
        return processPowerFP >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) Math.ceil(processPowerFP);
    }

    public double getProcessEnergyFP() {
        return getProcessTimeFP() * getProcessPowerFP();
    }

    public long getProcessEnergy() {
        double processEnergyFP = getProcessEnergyFP();
        return processEnergyFP >= Long.MAX_VALUE ? Long.MAX_VALUE : (long) Math.ceil(processEnergyFP);
    }

    public boolean isHalted() {
        return multiblock.fullHalt || Stream.concat(Stream.of(multiblock.controller), getParts(MachineRedstonePortEntity.class).stream()).anyMatch(x -> x != null && x.getIsRedstonePowered());
    }

    protected boolean hasSufficientEnergy() {
        if (multiblock.processor.time <= multiblock.processor.resetTime) {
            long processEnergy = getProcessEnergy(), maxEnergy = multiblock.energyStorage.getMaxEnergyStoredLong();
            if (processEnergy >= maxEnergy) {
                return multiblock.energyStorage.getEnergyStoredLong() >= maxEnergy;
            } else {
                return multiblock.energyStorage.getEnergyStoredLong() >= processEnergy;
            }
        } else {
            return multiblock.energyStorage.getEnergyStoredLong() >= getProcessPower();
        }
    }

    protected boolean readyToProcess() {
        return multiblock.processor.readyToProcessBase() && (isGenerator() || hasSufficientEnergy());
    }

    public void refreshRecipe(Level level) {
        multiblock.processor.refreshRecipeBase(level);
    }

    public void refreshActivity() {
        multiblock.processor.refreshActivityBase();
    }

    // Component Logic

    public @Nonnull EnergyStorage getPowerPortEnergyStorage(EnergyStorage backupStorage) {
        return multiblock.energyStorage;
    }

    public @Nonnull List<Tank> getReservoirPortTanks(List<Tank> backupTanks) {
        return multiblock.reservoirTanks;
    }

    public @Nonnull NonNullList<ItemStack> getProcessPortInventoryStacks(NonNullList<ItemStack> backupStacks, int slot) {
        return multiblock.isAssembled() && slot >= 0 ? multiblock.inventoryStacks.subList(slot, slot + 1) : backupStacks;
    }

    public @Nonnull InventoryConnection[] getProcessPortInventoryConnections(InventoryConnection[] backupInventoryConnections, int slot) {
        return multiblock.isAssembled() && slot >= 0 ? multiblock.inventoryConnections.get(slot) : backupInventoryConnections;
    }

    public @Nonnull List<Tank> getProcessPortTanks(List<Tank> backupTanks, int tankIndex) {
        return multiblock.isAssembled() && tankIndex >= 0 ? multiblock.tanks.subList(tankIndex, tankIndex + 1) : backupTanks;
    }

    public @Nonnull FluidConnection[] getProcessPortFluidConnections(FluidConnection[] backupFluidConnections, int tankIndex) {
        return multiblock.isAssembled() && tankIndex >= 0 ? multiblock.fluidConnections.get(tankIndex) : backupFluidConnections;
    }

    // Client

    @Override
    public void onUpdateClient() {
    }

    protected Object2ObjectMap<BlockPos, SoundInstance> getSoundMap() {
        if (multiblock.soundMap == null) {
            multiblock.soundMap = new Object2ObjectOpenHashMap<>();
        }
        return multiblock.soundMap;
    }

    @OnlyIn(Dist.CLIENT)
    protected void clearSounds() {
        getSoundMap().forEach((k, v) -> SoundHandler.stopBlockSound(k));
        getSoundMap().clear();
    }

    // NBT

    @Override
    public void writeToLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        logicTag.putBoolean("isMachineOn", multiblock.isMachineOn);
        logicTag.putBoolean("fullHalt", multiblock.fullHalt);

//        logicTag.putDouble("radiationLevel", multiblock.radiation.getRadiationLevel()); TODO

        multiblock.energyStorage.writeToNBT(logicTag, registries, "energyStorage");
        writeTanks(multiblock.reservoirTanks, logicTag, registries, "reservoirTanks");

        writeStacks(multiblock.inventoryStacks, logicTag, registries);
        writeTanks(multiblock.tanks, logicTag, registries, "tanks");

        multiblock.processor.writeToNBT(logicTag, registries, "processor");
        logicTag.putInt("productionCount", multiblock.productionCount);

        logicTag.putDouble("baseSpeedMultiplier", multiblock.baseSpeedMultiplier);
        logicTag.putDouble("basePowerMultiplier", multiblock.basePowerMultiplier);

        multiblock.recipeUnitInfo.writeToNBT(logicTag, registries, "recipeUnitInfo");
    }

    @Override
    public void readFromLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        multiblock.isMachineOn = logicTag.getBoolean("isMachineOn");
        multiblock.fullHalt = logicTag.getBoolean("fullHalt");

//        multiblock.radiation.setRadiationLevel(logicTag.getDouble("radiationLevel")); TODO

        multiblock.energyStorage.readFromNBT(logicTag, registries, "energyStorage");
        readTanks(multiblock.reservoirTanks, logicTag, registries, "reservoirTanks");

        readStacks(multiblock.inventoryStacks, logicTag, registries);
        readTanks(multiblock.tanks, logicTag, registries, "tanks");

        multiblock.processor.readFromNBT(logicTag, registries, "processor");
        multiblock.productionCount = logicTag.getInt("productionCount");

        multiblock.baseSpeedMultiplier = logicTag.getDouble("baseSpeedMultiplier");
        multiblock.basePowerMultiplier = logicTag.getDouble("basePowerMultiplier");

        multiblock.recipeUnitInfo = RecipeUnitInfo.readFromNBT(logicTag, registries, "recipeUnitInfo");
    }

    // Packets

    @Override
    public MachineUpdatePacket getMultiblockUpdatePacket() {
        return null;
    }

    @Override
    public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
        multiblock.isMachineOn = message.isMachineOn;
        multiblock.processor.isProcessing = message.isProcessing;
        multiblock.processor.time = message.time;
        multiblock.processor.baseProcessTime = message.baseProcessTime;
        multiblock.baseProcessPower = message.baseProcessPower;
        Tank.TankInfo.readInfoList(message.tankInfos, multiblock.tanks);
        multiblock.baseSpeedMultiplier = message.baseSpeedMultiplier;
        multiblock.basePowerMultiplier = message.basePowerMultiplier;
        multiblock.recipeUnitInfo = message.recipeUnitInfo;
    }

    public MachineRenderPacket getRenderPacket() {
        return null;
    }

    public void onRenderPacket(MachineRenderPacket message) {
        multiblock.isMachineOn = message.isMachineOn;
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
    }
}