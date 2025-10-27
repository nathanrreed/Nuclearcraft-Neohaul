package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.block_entity.fission.port.FissionIrradiatorPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.inventory.ITileFilteredInventory;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.FissionIrradiatorMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.payload.multiblock.FissionIrradiatorUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionIrradiatorRecipe;
import com.nred.nuclearcraft.util.CCHelper;
import com.nred.nuclearcraft.util.NBTHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class FissionIrradiatorEntity extends AbstractFissionEntity implements IBasicProcessor<FissionIrradiatorEntity, FissionIrradiatorUpdatePacket>, ITileFilteredInventory, IFissionHeatingComponent, IFissionFluxSink, IFissionPortTarget<FissionIrradiatorPortEntity, FissionIrradiatorEntity> {
    protected final ProcessorMenuInfoImpl.BasicProcessorMenuInfo<FissionIrradiatorEntity, FissionIrradiatorUpdatePacket> info;

    protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
    protected final @Nonnull NonNullList<ItemStack> consumedStacks;

    protected final @Nonnull NonNullList<ItemStack> filterStacks;

    protected @Nonnull InventoryConnection[] inventoryConnections;

    protected final @Nonnull List<Tank> tanks;
    protected final @Nonnull List<Tank> consumedTanks;

    protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Collections.emptyList());

    protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
    protected @Nonnull ChemicalTileWrapper[] chemicalSides = ITileFluid.getDefaultChemicalSides(this);

    public double baseProcessTime = 1D, baseProcessHeatPerFlux = 0D, baseProcessEfficiency = 0D, baseProcessRadiation = 0D;
    public long minFluxPerTick = 0, maxFluxPerTick = -1;

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs, hasConsumed;
    public boolean isRunningSimulated;

    protected RecipeInfo<FissionIrradiatorRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    protected FissionCluster cluster = null;
    protected long heat = 0L;

    public long clusterHeatStored, clusterHeatCapacity;

    protected long flux = 0;

    protected BlockPos masterPortPos = DEFAULT_NON;
    protected FissionIrradiatorPortEntity masterPort = null;

    public FissionIrradiatorEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("irradiator").get(), pos, blockState);
        info = TileInfoHandler.getProcessorContainerInfo("fission_irradiator");

        inventoryStacks = info.getInventoryStacks();
        consumedStacks = info.getConsumedStacks();

        filterStacks = info.getInventoryStacks();

        inventoryConnections = ITileInventory.inventoryConnectionAll(info.nonItemSorptions());

        tanks = Collections.emptyList();
        consumedTanks = info.getConsumedTanks();
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FissionIrradiatorMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }

    // IFissionComponent

    @Override
    public @Nullable FissionCluster getCluster() {
        return cluster;
    }

    @Override
    public void setClusterInternal(@Nullable FissionCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        return isRunning(simulate);
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isRunning(simulate);
    }

    @Override
    public void resetStats() {
        flux = 0;
        refreshAll();
    }

    @Override
    public boolean isClusterRoot() {
        return true;
    }

    @Override
    public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        refreshDirty();
        refreshIsProcessing(false, simulate);

        IFissionFluxSink.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
    }

    @Override
    public void refreshIsProcessing(boolean checkCluster, boolean simulate) {
        if (simulate) {
            isProcessing = false;
            isRunningSimulated = isProcessing(checkCluster, simulate);
        } else {
            isProcessing = isProcessing(checkCluster, simulate);
            isRunningSimulated = false;
        }
        hasConsumed = hasConsumed();
    }

    @Override
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
    }

    @Override
    public boolean isAcceptingFlux(Direction side, boolean simulate) {
        return canProcessInputs;
    }

    @Override
    public boolean isNullifyingSources(Direction side, boolean simulate) {
        return canProcessInputs;
    }

    @Override
    public double moderatorLineEfficiencyFactor() {
        return baseProcessEfficiency;
    }

    @Override
    public long getFlux() {
        return flux;
    }

    @Override
    public void addFlux(long addedFlux) {
        flux += addedFlux;
    }

    @Override
    public long getRawHeating(boolean simulate) {
        return (long) Math.min(Long.MAX_VALUE, Math.floor(flux * baseProcessHeatPerFlux));
    }

    @Override
    public long getRawHeatingIgnoreCoolingPenalty(boolean simulate) {
        return 0L;
    }

    @Override
    public double getEffectiveHeating(boolean simulate) {
        return flux * baseProcessHeatPerFlux * baseProcessEfficiency;
    }

    @Override
    public double getEffectiveHeatingIgnoreCoolingPenalty(boolean simulate) {
        return 0D;
    }

    @Override
    public long getHeatStored() {
        return heat;
    }

    @Override
    public void setHeatStored(long heat) {
        this.heat = heat;
    }

    // IFissionPortTarget

    @Override
    public BlockPos getMasterPortPos() {
        return masterPortPos;
    }

    @Override
    public void setMasterPortPos(BlockPos pos) {
        masterPortPos = pos;
    }

    @Override
    public void clearMasterPort() {
        masterPort = null;
        masterPortPos = DEFAULT_NON;
    }

    @Override
    public void refreshMasterPort() {
        Optional<FissionReactor> multiblock = getMultiblockController();
        masterPort = multiblock.map(fissionReactor -> fissionReactor.getPartMap(FissionIrradiatorPortEntity.class).get(masterPortPos.asLong())).orElse(null);
        if (masterPort == null) {
            masterPortPos = DEFAULT_NON;
        }
    }

    @Override
    public void onPortRefresh() {
        refreshAll();
    }

    // Ticking

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
            refreshMasterPort();
            refreshAll();
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide) {
            Optional<FissionReactor> reactor = getMultiblockController();
            boolean shouldRefresh = reactor.isPresent() && reactor.get().isReactorOn && cluster == null && isProcessing(false, false);

            if (onTick()) {
                setChanged();
            }

            if (shouldRefresh) {
                reactor.get().refreshFlag = true;
            }
        }
    }

    @Override
    public void refreshAll() {
        refreshDirty();
        refreshIsProcessing(true, false);
    }

    @Override
    public void refreshActivity() {
        boolean wasReady = readyToProcess(false);
        canProcessInputs = canProcessInputs();
        Optional<FissionReactor> multiblock = getMultiblockController();
        if (multiblock.isPresent() && !wasReady && readyToProcess(false)) {
            multiblock.get().refreshFlag = true;
        }
    }

    public boolean isRunning(boolean simulate) {
        return simulate ? isRunningSimulated : isProcessing;
    }

    // IProcessor

    @Override
    public ProcessorMenuInfoImpl.BasicProcessorMenuInfo<FissionIrradiatorEntity, FissionIrradiatorUpdatePacket> getContainerInfo() {
        return info;
    }

    @Override
    public BasicRecipeHandler<FissionIrradiatorRecipe> getRecipeHandler() {
        return NCRecipes.fission_irradiator;
    }

    @Override
    public RecipeInfo<FissionIrradiatorRecipe> getRecipeInfo() {
        return recipeInfo;
    }

    @Override
    public void setRecipeInfo(RecipeInfo<? extends BasicRecipe> recipeInfo) {
        this.recipeInfo = (RecipeInfo<FissionIrradiatorRecipe>) recipeInfo;
    }

    @Override
    public void setRecipeStats(@Nullable BasicRecipe basic) {
        if (basic instanceof FissionIrradiatorRecipe recipe) {
            baseProcessTime = recipe.getIrradiatorFluxRequired();
            baseProcessHeatPerFlux = recipe.getIrradiatorHeatPerFlux();
            baseProcessEfficiency = recipe.getIrradiatorProcessEfficiency();
            minFluxPerTick = recipe.getIrradiatorMinFluxPerTick();
            maxFluxPerTick = recipe.getIrradiatorMaxFluxPerTick();
            baseProcessRadiation = recipe.getIrradiatorBaseProcessRadiation();
        } else {
            baseProcessTime = 1;
            baseProcessHeatPerFlux = 0;
            baseProcessEfficiency = 0;
            minFluxPerTick = 0;
            maxFluxPerTick = -1;
            baseProcessRadiation = 0;
        }
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
        return consumedStacks;
    }

    @Override
    public @Nonnull List<Tank> getConsumedTanks() {
        return consumedTanks;
    }

    @Override
    public double getBaseProcessTime() {
        return baseProcessTime;
    }

    @Override
    public void setBaseProcessTime(double baseProcessTime) {
        this.baseProcessTime = baseProcessTime;
    }

    @Override
    public double getBaseProcessPower() {
        return 0D;
    }

    @Override
    public void setBaseProcessPower(double baseProcessPower) {
    }

    @Override
    public double getCurrentTime() {
        return time;
    }

    @Override
    public void setCurrentTime(double time) {
        this.time = time;
    }

    @Override
    public double getResetTime() {
        return resetTime;
    }

    @Override
    public void setResetTime(double resetTime) {
        this.resetTime = resetTime;
    }

    @Override
    public boolean getIsProcessing() {
        return isProcessing;
    }

    @Override
    public void setIsProcessing(boolean isProcessing) {
        this.isProcessing = isProcessing;
    }

    @Override
    public boolean getCanProcessInputs() {
        return canProcessInputs;
    }

    @Override
    public void setCanProcessInputs(boolean canProcessInputs) {
        this.canProcessInputs = canProcessInputs;
    }

    @Override
    public boolean getHasConsumed() {
        return hasConsumed;
    }

    @Override
    public void setHasConsumed(boolean hasConsumed) {
        this.hasConsumed = hasConsumed;
    }

    @Override
    public double getSpeedMultiplier() {
        if ((minFluxPerTick >= 0L && flux < minFluxPerTick) || (maxFluxPerTick >= 0L && flux > maxFluxPerTick)) {
            return 0;
        }
        return flux;
    }

    @Override
    public double getPowerMultiplier() {
        return 0D;
    }

    @Override
    public boolean isProcessing() {
        return !isSimulation() && isProcessing(true, false);
    }

    public boolean isProcessing(boolean checkCluster, boolean simulate) {
        return readyToProcess(checkCluster) && flux > 0;
    }

    @Override
    public boolean readyToProcess() {
        return readyToProcess(true);
    }

    public boolean readyToProcess(boolean checkCluster) {
        return canProcessInputs && hasConsumed && isMachineAssembled() && (!checkCluster || cluster != null);
    }

    @Override
    public void process() {
//        getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier() / RecipeStats.getFissionMaxModeratorLineFlux()); TODO
        IBasicProcessor.super.process();
    }

    @Override
    public void finishProcess() {
        double oldProcessTime = baseProcessTime, oldProcessHeat = baseProcessHeatPerFlux, oldProcessEfficiency = baseProcessEfficiency;
        produceProducts();
        refreshRecipe();
        time = Math.max(0D, time - oldProcessTime);
        refreshActivityOnProduction();
        if (!canProcessInputs) {
            time = 0;
        }

        Optional<FissionReactor> multiblock = getMultiblockController();
        if (multiblock.isPresent()) {
            if (canProcessInputs) {
                if (oldProcessHeat != baseProcessHeatPerFlux || oldProcessEfficiency != baseProcessEfficiency) {
                    multiblock.get().addClusterToRefresh(cluster);
                }
            } else {
                multiblock.get().refreshFlag = true;
            }
        }
    }

    @Override
    public int getItemProductCapacity(int slot, ItemStack stack) {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getMaxStackSize() : IBasicProcessor.super.getItemProductCapacity(slot, stack);
    }

    // ITileInventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStacks() : inventoryStacks;
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal() {
        return inventoryStacks;
    }

    @Override
    public void setChanged() {
        refreshDirty();
        super.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (stack.isEmpty() || (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize)) {
            return false;
        }
        ItemStack filter = getFilterStacks().get(slot);
        if (!filter.isEmpty() && !ItemStack.isSameItemSameComponents(stack, filter)) {
            return false;
        }
        return isItemValidForSlotInternal(slot, stack);
    }

    @Override
    public boolean isItemValidForSlotInternal(int slot, ItemStack stack) {
        return IBasicProcessor.super.canPlaceItem(slot, stack);
    }

    @Override
    public int getMaxStackSize() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getMaxStackSize() : IBasicProcessor.super.getMaxStackSize();
    }

    @Override
    public void clearAllSlots() {
        Collections.fill(inventoryStacks, ItemStack.EMPTY);
        Collections.fill(consumedStacks, ItemStack.EMPTY);
        refreshAll();
    }

    @Override
    public @Nonnull InventoryConnection[] getInventoryConnections() {
        return inventoryConnections;
    }

    @Override
    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
        inventoryConnections = connections;
    }

    @Override
    public ItemOutputSetting getItemOutputSetting(int slot) {
        return ItemOutputSetting.DEFAULT;
    }

    @Override
    public void setItemOutputSetting(int slot, ItemOutputSetting setting) {
    }

    // ITileFilteredInventory

    @Override
    public @Nonnull NonNullList<ItemStack> getFilterStacks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterStacks() : filterStacks;
    }

    @Override
    public boolean canModifyFilter(int slot) {
        return !isMachineAssembled();
    }

    @Override
    public void onFilterChanged(int slot) {
        setChanged();
    }

    @Override
    public Object getFilterKey() {
        return getFilterStacks().get(0).isEmpty() ? 0 : RecipeHelper.pack(getFilterStacks().get(0));
    }

    // ITileFluid

    @Override
    public @Nonnull List<Tank> getTanks() {
        return tanks;
    }

    @Override
    public @Nonnull FluidConnection[] getFluidConnections() {
        return fluidConnections;
    }

    @Override
    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
        fluidConnections = connections;
    }

    @Override
    public @Nonnull FluidTileWrapper[] getFluidSides() {
        return fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return chemicalSides;
    }

    @Override
    public boolean getInputTanksSeparated() {
        return false;
    }

    @Override
    public void setInputTanksSeparated(boolean separated) {
    }

    @Override
    public boolean getVoidUnusableFluidInput(int tankNumber) {
        return false;
    }

    @Override
    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {
    }

    @Override
    public TankOutputSetting getTankOutputSetting(int tankNumber) {
        return TankOutputSetting.DEFAULT;
    }

    @Override
    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {
    }

    @Override
    public boolean hasConfigurableFluidConnections() {
        return false;
    }

    // ITileGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public FissionIrradiatorUpdatePacket getTileUpdatePacket() {
        return new FissionIrradiatorUpdatePacket(worldPosition, isProcessing, time, baseProcessTime, getTanks(), masterPortPos, getFilterStacks(), cluster);
    }

    @Override
    public void onTileUpdatePacket(FissionIrradiatorUpdatePacket message) {
        IBasicProcessor.super.onTileUpdatePacket(message);
        if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
            refreshMasterPort();
        }
        IntStream.range(0, filterStacks.size()).forEach(x -> filterStacks.set(x, message.filterStacks.get(x)));
        clusterHeatStored = message.clusterHeatStored;
        clusterHeatCapacity = message.clusterHeatCapacity;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeInventory(nbt, registries);
        writeInventoryConnections(nbt, registries);

        writeProcessorNBT(nbt, registries);

        nbt.putDouble("baseProcessTime", baseProcessTime);
        nbt.putDouble("baseProcessHeatPerFlux", baseProcessHeatPerFlux);
        nbt.putDouble("baseProcessEfficiency", baseProcessEfficiency);

        nbt.putLong("minFluxPerTick", minFluxPerTick);
        nbt.putLong("maxFluxPerTick", maxFluxPerTick);

        nbt.putLong("flux", flux);
        nbt.putLong("clusterHeat", heat);

        nbt.putBoolean("isRunningSimulated", isRunningSimulated);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readInventory(nbt, registries);
        readInventoryConnections(nbt, registries);

        readProcessorNBT(nbt, registries);

        baseProcessTime = nbt.getDouble("baseProcessTime");
        baseProcessHeatPerFlux = nbt.getDouble("baseProcessHeatPerFlux");
        baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");

        minFluxPerTick = nbt.getLong("minFluxPerTick");
        maxFluxPerTick = nbt.getLong("maxFluxPerTick");

        flux = nbt.getLong("flux");
        heat = nbt.getLong("clusterHeat");

        isRunningSimulated = nbt.getBoolean("isRunningSimulated");
    }

    @Override
    public CompoundTag writeInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        NBTHelper.writeAllItems(nbt, registries, inventoryStacks, filterStacks, consumedStacks);
        return nbt;
    }

    @Override
    public void readInventory(CompoundTag nbt, HolderLookup.Provider registries) {
        NBTHelper.readAllItems(nbt, registries, inventoryStacks, filterStacks, consumedStacks);
    }

    // ComputerCraft

    @Override
    public String getCCKey() {
        return "irradiator";
    }

    @Override
    public Object getCCInfo() {
        Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
        List<ItemStack> stacks = getInventoryStacks();
        entry.put("input", CCHelper.stackInfo(stacks.get(0)));
        entry.put("output", CCHelper.stackInfo(stacks.get(1)));
        entry.put("effective_heating", getEffectiveHeating(false));
        entry.put("is_processing", getIsProcessing());
        entry.put("current_time", getCurrentTime());
        entry.put("base_process_time", getBaseProcessTime());
        entry.put("base_process_heat_per_flux", baseProcessHeatPerFlux);
        entry.put("base_process_efficiency", baseProcessEfficiency);
        entry.put("flux", getFlux());
        return entry;
    }
}