package com.nred.nuclearcraft.multiblock.machine;

import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.FluidConnection;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankSorption;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemSorption;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.machine.IMachineController;
import com.nred.nuclearcraft.block_entity.machine.MachineProcessPortEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineRedstonePortEntity;
import com.nred.nuclearcraft.block_entity.machine.MachineReservoirPortEntity;
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
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

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

        InventoryStackList prevReservoirInventoryStacks = multiblock.reservoirInventoryStacks;

        multiblock.reservoirInventoryStacks = InventoryStackList.withSize(reservoirInventorySize());

        for (int i = 0, len = Math.min(prevReservoirInventoryStacks.size(), multiblock.reservoirInventoryStacks.size()); i < len; ++i) {
            multiblock.reservoirInventoryStacks.set(i, prevReservoirInventoryStacks.get(i));
        }

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

        List<Set<ResourceLocation>> validFluids = multiblock.recipeHandler == null ? null : multiblock.recipeHandler.getValidFluids(multiblock.getWorld().getRecipeManager());
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

    public int reservoirInventorySize() {
        return 0;
    }

    public int reservoirTankCount() {
        return 0;
    }

    public boolean usesReservoirPorts() {
        return reservoirInventorySize() > 0 || reservoirTankCount() > 0;
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

        if (!usesReservoirPorts()) {
            Map<Long, MachineReservoirPortEntity> reservoirPortMap = getPartMap(MachineReservoirPortEntity.class);
            if (!reservoirPortMap.isEmpty()) {
                multiblock.setLastError(MODID + ".multiblock_validation.machine.invalid_reservoir_port", reservoirPortMap.keySet());
                return false;
            }
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
                mergeInventoryStacks(multiblock.reservoirInventoryStacks, assimilated.reservoirInventoryStacks);
                mergeTanks(multiblock.reservoirTanks, assimilated.reservoirTanks);
                mergeInventoryStacks(multiblock.inventoryStacks, assimilated.inventoryStacks);
                mergeTanks(multiblock.tanks, assimilated.tanks);
            }
        }
    }


    protected static void mergeInventoryStacks(NonNullList<ItemStack> stacks, NonNullList<ItemStack> assimilatedStacks) {
        for (ItemStack assimilatedStack : assimilatedStacks) {
            if (assimilatedStack.isEmpty()) {
                continue;
            }

            ItemStack remaining = assimilatedStack.copy();
            for (ItemStack stack : stacks) {
                if (!remaining.isEmpty() && ItemStack.isSameItemSameComponents(stack, remaining)) {
                    int transfer = Math.min(remaining.getCount(), stack.getMaxStackSize() - stack.getCount());
                    if (transfer > 0) {
                        stack.grow(transfer);
                        remaining.shrink(transfer);
                    }
                }
            }

            for (int i = 0; i < stacks.size() && !remaining.isEmpty(); ++i) {
                if (stacks.get(i).isEmpty()) {
                    int transfer = Math.min(remaining.getCount(), remaining.getMaxStackSize());
                    ItemStack stack = remaining.copy();
                    stack.setCount(transfer);
                    stacks.set(i, stack);
                    remaining.shrink(transfer);
                }
            }
        }
    }

    protected static void mergeTanks(List<Tank> tanks, List<Tank> assimilatedTanks) {
        for (int i = 0, len = Math.min(tanks.size(), assimilatedTanks.size()); i < len; ++i) {
            tanks.get(i).mergeTank(assimilatedTanks.get(i));
        }
    }

    @Override
    public void onAssimilated(IMultiblockController<Machine> assimilator) {
    }

    // Server

    @Override
    public boolean onUpdateServer() {
        if (multiblock.machineActivityCooldown > 0) {
            --multiblock.machineActivityCooldown;
        }

        boolean shouldUpdate = multiblock.processor.onTick(getWorld());
        if (multiblock.machineActivityCooldown <= 0 && multiblock.machineActivityDirty) {
            shouldUpdate |= setIsMachineOnInternal(multiblock.isMachineOnQueued);
        }

        if (multiblock.controller != null) {
            multiblock.sendRenderPacketToAll();
        }

        return shouldUpdate;
    }

    public void setActivity(boolean isMachineOn) {
        multiblock.controller.setActivity(isMachineOn);
    }

    public void setIsMachineOn(boolean isMachineOn) {
        if (multiblock.machineActivityCooldown > 0) {
            multiblock.isMachineOnQueued = isMachineOn;
            multiblock.machineActivityDirty = isMachineOn != multiblock.isMachineOn;
            return;
        }

        setIsMachineOnInternal(isMachineOn);
    }

    protected boolean setIsMachineOnInternal(boolean isMachineOn) {
        boolean oldIsMachineOn = multiblock.isMachineOn;
        multiblock.isMachineOn = isMachineOn;
        multiblock.machineActivityDirty = false;
        if (multiblock.isMachineOn != oldIsMachineOn) {
            multiblock.machineActivityCooldown = NCConfig.machine_update_rate;
            if (multiblock.controller != null) {
                setActivity(multiblock.isMachineOn);
                multiblock.sendMultiblockUpdatePacketToAll();
            }
        }
        return multiblock.isMachineOn != oldIsMachineOn;
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
    public @Nonnull NonNullList<ItemStack> getReservoirPortInventoryStacks(MachineReservoirPortEntity port) {
        return multiblock.reservoirInventoryStacks;
    }

    public @Nonnull InventoryConnection[] getReservoirPortInventoryConnections(MachineReservoirPortEntity port) {
        return port.backupInventoryConnections;
    }

    public @Nonnull ItemStack getReservoirPortStackInSlot(MachineReservoirPortEntity port, int slot) {
        NonNullList<ItemStack> stacks = port.getInventoryStacks();
        return slot >= 0 && slot < stacks.size() ? stacks.get(slot) : ItemStack.EMPTY;
    }

    public @Nonnull ItemStack decrReservoirPortStackSize(MachineReservoirPortEntity port, int slot, int amount) {
        NonNullList<ItemStack> stacks = port.getInventoryStacks();
        if (slot < 0 || slot >= stacks.size() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = ContainerHelper.removeItem(stacks, slot, amount);
        if (!stack.isEmpty()) {
            onReservoirPortInventoryChanged(port, slot);
        }
        return stack;
    }

    public @Nonnull ItemStack removeReservoirPortStackFromSlot(MachineReservoirPortEntity port, int slot) {
        NonNullList<ItemStack> stacks = port.getInventoryStacks();
        if (slot < 0 || slot >= stacks.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = ContainerHelper.takeItem(stacks, slot);
        if (!stack.isEmpty()) {
            onReservoirPortInventoryChanged(port, slot);
        }
        return stack;
    }

    public void setReservoirPortInventorySlotContents(MachineReservoirPortEntity port, int slot, ItemStack stack) {
        NonNullList<ItemStack> stacks = port.getInventoryStacks();
        if (slot < 0 || slot >= stacks.size()) {
            return;
        }

        ItemStack stackInSlot = stacks.get(slot);
        boolean sameStack = !stack.isEmpty() && ItemStack.isSameItemSameComponents(stack, stackInSlot);

        if (stack.getCount() > port.getMaxStackSize()) {
            stack.setCount(port.getMaxStackSize());
        }

        stacks.set(slot, stack);

        if (!sameStack) {
            port.markTileDirty();
        }
        onReservoirPortInventoryChanged(port, slot);
    }

    public void clearReservoirPortInventory(MachineReservoirPortEntity port) {
        Collections.fill(port.getInventoryStacks(), ItemStack.EMPTY);
        onReservoirPortInventoryCleared(port);
    }

    public boolean hasReservoirPortItemCapability(MachineReservoirPortEntity port, @Nullable Direction side) {
        return !port.getInventoryStacks().isEmpty() && port.hasInventorySideCapability(side);
    }

    public @Nonnull IItemHandler getReservoirPortItemHandler(MachineReservoirPortEntity port, @Nullable Direction side) {
        return port.getItemHandler(side);
    }

    public void pushReservoirPortItemToSide(MachineReservoirPortEntity port, @Nonnull Direction side) {
        NonNullList<ItemStack> stacks = port.getInventoryStacks();
        if (!stacks.isEmpty()) {
            port.pushStacksToSide(side);
        }
    }

    public boolean isReservoirPortItemValid(MachineReservoirPortEntity port, int slot, ItemStack stack) {
        NonNullList<ItemStack> stacks = port.getInventoryStacks();
        return slot >= 0 && slot < stacks.size() && !stack.isEmpty();
    }

    public void onReservoirPortInventoryChanged(MachineReservoirPortEntity port, int slot) {
        refreshRecipe(getWorld());
        refreshActivity();
    }

    public void onReservoirPortInventoryCleared(MachineReservoirPortEntity port) {
        refreshRecipe(getWorld());
        refreshActivity();
    }

    public @Nonnull List<Tank> getReservoirPortTanks(MachineReservoirPortEntity port) {
        return multiblock.reservoirTanks;
    }

    public @Nonnull FluidConnection[] getReservoirPortFluidConnections(MachineReservoirPortEntity port) {
        return port.backupFluidConnections;
    }

    public boolean hasReservoirPortFluidCapability(MachineReservoirPortEntity port, @Nullable Direction side) {
        return !port.getTanks().isEmpty() && port.hasFluidSideCapability(side);
    }

    public @Nonnull IFluidHandler getReservoirPortFluidHandler(MachineReservoirPortEntity port, @Nonnull Direction side) {
        return port.getFluidSide(side);
    }

    public void pushReservoirPortFluidToSide(MachineReservoirPortEntity port, @Nonnull Direction side) {
        List<Tank> tanks = port.getTanks();
        if (!tanks.isEmpty() && !tanks.get(0).isEmpty() && port.getTankSorption(side, 0).canDrain()) {
            port.pushFluidToSide(side);
        }
    }

    public boolean isReservoirPortFluidValid(MachineReservoirPortEntity port, int tankNumber, FluidStack stack) {
        List<Tank> tanks = port.getTanks();
        return tankNumber >= 0 && tankNumber < tanks.size() && tanks.get(tankNumber).isFluidValid(stack);
    }

    public void onReservoirPortTanksCleared(MachineReservoirPortEntity port) {
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

        logicTag.putDouble("radiationLevel", multiblock.radiation.getRadiationLevel());

        multiblock.energyStorage.writeToNBT(logicTag, registries, "energyStorage");
        CompoundTag reservoirInventoryTag = new CompoundTag();
        writeStacks(multiblock.reservoirInventoryStacks, reservoirInventoryTag, registries);
        logicTag.put("reservoirInventoryStacks", reservoirInventoryTag);
        writeTanks(multiblock.reservoirTanks, logicTag, registries, "reservoirTanks");

        writeStacks(multiblock.inventoryStacks, logicTag, registries);
        writeTanks(multiblock.tanks, logicTag, registries, "tanks");

        multiblock.processor.writeToNBT(logicTag, registries, "processor");
        logicTag.putInt("productionCount", multiblock.productionCount);

        logicTag.putDouble("baseSpeedMultiplier", multiblock.baseSpeedMultiplier);
        logicTag.putDouble("basePowerMultiplier", multiblock.basePowerMultiplier);

        multiblock.recipeUnitInfo.writeToNBT(logicTag, registries, "recipeUnitInfo");

        logicTag.putBoolean("readyToProcess", multiblock.readyToProcess);
    }

    @Override
    public void readFromLogicTag(CompoundTag logicTag, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
        multiblock.isMachineOn = logicTag.getBoolean("isMachineOn");
        multiblock.fullHalt = logicTag.getBoolean("fullHalt");

        multiblock.radiation.setRadiationLevel(logicTag.getDouble("radiationLevel"));

        multiblock.energyStorage.readFromNBT(logicTag, registries, "energyStorage");
        readStacks(multiblock.reservoirInventoryStacks, logicTag.getCompound("reservoirInventoryStacks"), registries);
        readTanks(multiblock.reservoirTanks, logicTag, registries, "reservoirTanks");

        readStacks(multiblock.inventoryStacks, logicTag, registries);
        readTanks(multiblock.tanks, logicTag, registries, "tanks");

        multiblock.processor.readFromNBT(logicTag, registries, "processor");
        multiblock.productionCount = logicTag.getInt("productionCount");

        multiblock.baseSpeedMultiplier = logicTag.getDouble("baseSpeedMultiplier");
        multiblock.basePowerMultiplier = logicTag.getDouble("basePowerMultiplier");

        multiblock.recipeUnitInfo = RecipeUnitInfo.readFromNBT(logicTag, registries, "recipeUnitInfo");

        multiblock.readyToProcess = logicTag.getBoolean("readyToProcess");
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