package com.nred.nuclearcraft.block_entity.fission;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCoolerPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block_entity.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl.BasicProcessorMenuInfo;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.BlockEntityInfoHandler;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.PebbleFissionCoolerMenu;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.pebble.FissionCoolerType;
import com.nred.nuclearcraft.payload.multiblock.PebbleFissionCoolerUpdatePacket;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.PebbleFissionCoolerRecipe;
import com.nred.nuclearcraft.util.CCHelper;
import com.nred.nuclearcraft.util.InventoryStackList;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.fission_cooler_coolant_heat_per_mb;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class PebbleFissionCoolerEntity extends AbstractFissionEntity implements IBasicProcessor<PebbleFissionCoolerEntity, PebbleFissionCoolerUpdatePacket, PebbleFissionCoolerRecipe>, ITileFilteredFluid, IFissionCoolingComponent, IFissionPortTarget<FissionCoolerPortEntity, PebbleFissionCoolerEntity> {
    public FissionCoolerType coolerType;

    protected final BasicProcessorMenuInfo<PebbleFissionCoolerEntity, PebbleFissionCoolerUpdatePacket, PebbleFissionCoolerRecipe> info;

    protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Collections.emptyList());

    protected final @Nonnull List<Tank> tanks;

    protected final @Nonnull List<Tank> filterTanks;

    protected @Nonnull FluidConnection[] fluidConnections;

    protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
    protected @Nonnull ChemicalTileWrapper[] chemicalSides = ITileFluid.getDefaultChemicalSides(this);

    protected int baseProcessCooling;
    protected PlacementRule<FissionReactor, AbstractFissionEntity> placementRule = FissionPlacement.RULE_MAP.get("");
    public double heatingSpeedMultiplier; // Based on the cluster efficiency, but with heat/cooling taken into account

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs;
    public boolean isRunningSimulated;

    protected RecipeInfo<PebbleFissionCoolerRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    protected FissionCluster cluster = null;
    protected long heat = 0L;
    protected boolean isInValidPosition = false;

    public long clusterHeatStored, clusterHeatCapacity;

    protected BlockPos masterPortPos = DEFAULT_NON;
    protected FissionCoolerPortEntity masterPort = null;

    public PebbleFissionCoolerEntity(BlockPos pos, BlockState blockState, FissionCoolerType coolerType) {
        super(FISSION_ENTITY_TYPE.get("cooler").get(), pos, blockState);
        info = BlockEntityInfoHandler.getProcessorMenuInfo("pebble_fission_cooler");

        this.coolerType = coolerType;
        Set<ResourceLocation> validFluids = coolerType.validFluids();
        tanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, validFluids), new Tank(INGOT_BLOCK_VOLUME, new ObjectOpenHashSet<>()));
        filterTanks = Lists.newArrayList(new Tank(1000, validFluids), new Tank(1000, new ObjectOpenHashSet<>()));

        fluidConnections = ITileFluid.fluidConnectionAll(info.nonTankSorptions());
    }

    @Override
    public boolean isGoodForPosition(PartPosition position, IMultiblockValidator validatorCallback) {
        return position == PartPosition.Interior;
    }

    // MenuProvider

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new PebbleFissionCoolerMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(MODID + ".menu.title.fission_cooler");
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
        if (!isProcessing(false, simulate) || componentFailCache.containsKey(worldPosition.asLong())) {
            return isInValidPosition = false;
        } else if (placementRule.requiresRecheck()) {
            isInValidPosition = placementRule.satisfied(this, simulate);
            if (isInValidPosition) {
                assumedValidCache.put(worldPosition.asLong(), this);
            }
            return isInValidPosition;
        } else if (isInValidPosition) {
            return true;
        }
        return isInValidPosition = placementRule.satisfied(this, simulate);
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isRunning(simulate);
    }

    @Override
    public void resetStats() {
        isInValidPosition = false;
        heatingSpeedMultiplier = 0D;

        refreshAll();
    }

    @Override
    public boolean isClusterRoot() {
        return false;
    }

    @Override
    public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        refreshDirty();

        IFissionCoolingComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);

        refreshIsProcessing(true, simulate);
    }

    public void refreshIsProcessing(boolean checkValid, boolean simulate) {
        if (simulate) {
            isProcessing = false;
            isRunningSimulated = isProcessing(checkValid, simulate);
        } else {
            isProcessing = isProcessing(checkValid, simulate);
            isRunningSimulated = false;
        }
    }

    @Override
    public long getHeatStored() {
        return heat;
    }

    @Override
    public void setHeatStored(long heat) {
        this.heat = heat;
    }

    @Override
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
    }

    @Override
    public boolean isNullifyingSources(Direction side, boolean simulate) {
        return false;
    }

    @Override
    public long getCooling(boolean simulate) {
        return baseProcessCooling;
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
        masterPort = multiblock.map(fissionReactor -> fissionReactor.getPartMap(FissionCoolerPortEntity.class).get(masterPortPos.asLong())).orElse(null);
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
        if (!level.isClientSide()) {
            refreshMasterPort();
            refreshAll();
        }
    }

    @Override
    public void update() {
        if (!level.isClientSide()) {
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
    public void refreshRecipe() {
        recipeInfo = NCRecipes.gas_cooler.getRecipeInfoFromCoolerInputs(level, coolerType, getFluidInputs(false));
    }

    @Override
    public void refreshActivity() {
        canProcessInputs = canProcessInputs();
    }

    public boolean isRunning(boolean simulate) {
        return simulate ? isRunningSimulated : isProcessing;
    }

    // IProcessor

    @Override
    public BasicProcessorMenuInfo<PebbleFissionCoolerEntity, PebbleFissionCoolerUpdatePacket, PebbleFissionCoolerRecipe> getContainerInfo() {
        return info;
    }

    @Override
    public BasicRecipeHandler<PebbleFissionCoolerRecipe> getRecipeHandler() {
        return NCRecipes.gas_cooler;
    }

    @Override
    public RecipeInfo<PebbleFissionCoolerRecipe> getRecipeInfo() {
        return recipeInfo;
    }

    @Override
    public void setRecipeInfo(RecipeInfo<PebbleFissionCoolerRecipe> recipeInfo) {
        this.recipeInfo = recipeInfo;
    }

    @Override
    public void setRecipeStats(@Nullable PebbleFissionCoolerRecipe recipe) {
        if (recipe == null) {
            baseProcessCooling = 0;
            placementRule = FissionPlacement.RULE_MAP.get("");
        } else {
            baseProcessCooling = recipe.getFissionCoolingRate();
            placementRule = FissionPlacement.RULE_MAP.get(recipe.getFissionCoolingPlacementRule());
        }
    }

    @Override
    public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
        return getInventoryStacks();
    }

    @Override
    public @Nonnull List<Tank> getConsumedTanks() {
        return getTanks();
    }

    @Override
    public double getBaseProcessTime() {
        return 1D;
    }

    @Override
    public void setBaseProcessTime(double baseProcessTime) {
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
        return false;
    }

    @Override
    public void setHasConsumed(boolean hasConsumed) {
    }

    protected int getFluidIngredientStackSize() {
        return recipeInfo == null ? 0 : recipeInfo.recipe.getFluidIngredients().get(0).amount();
    }

    @Override
    public double getSpeedMultiplier() {
        int inputSize = getFluidIngredientStackSize();
        return inputSize <= 0 ? 0D : (heatingSpeedMultiplier * baseProcessCooling) / (fission_cooler_coolant_heat_per_mb * inputSize);
    }

    @Override
    public double getPowerMultiplier() {
        return 0D;
    }

    @Override
    public boolean isProcessing() {
        return !isSimulation() && isProcessing(true, false);
    }

    public boolean isProcessing(boolean checkValid, boolean simulate) {
        return readyToProcess(checkValid);
    }

    @Override
    public boolean readyToProcess() {
        return readyToProcess(true);
    }

    public boolean readyToProcess(boolean checkValid) {
        return canProcessInputs && isMachineAssembled() && (!checkValid || cluster != null);
    }

    @Override
    public void finishProcess() {
        int oldProcessCooling = baseProcessCooling;
        produceProducts();
        refreshRecipe();
        time = Math.max(0D, time - 1D);
        refreshActivityOnProduction();
        if (!canProcessInputs) {
            time = 0;
        }

        Optional<FissionReactor> multiblock = getMultiblockController();
        if (multiblock.isPresent()) {
            if (canProcessInputs) {
                if (oldProcessCooling != baseProcessCooling) {
                    multiblock.get().addClusterToRefresh(cluster);
                }
            } else {
                multiblock.get().refreshFlag = true;
            }
        }
    }

    // ITileInventory

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return InventoryStackList.EMPTY_LIST;
    }

    @Override
    public void setChanged() {
        refreshDirty();
        super.setChanged();
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

    // ITileFluid

    @Override
    public @Nonnull List<Tank> getTanks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
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
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFluidSides() : fluidSides;
    }

    @Override
    public @Nonnull ChemicalTileWrapper[] getChemicalSides() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getChemicalSides() : chemicalSides;
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

    @Override
    public void clearAllTanks() {
        for (Tank tank : tanks) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
        refreshAll();
    }

    // ITileFilteredFluid

    @Override
    public @Nonnull List<Tank> getTanksInternal() {
        return tanks;
    }

    @Override
    public @Nonnull List<Tank> getFilterTanks() {
        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
    }

    @Override
    public boolean canModifyFilter(int tank) {
        return !isMachineAssembled();
    }

    @Override
    public void onFilterChanged(int slot) {
        setChanged();
    }

    @Override
    public Object getFilterKey() {
        return coolerType.getName();
    }

    // ITileGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public PebbleFissionCoolerUpdatePacket getTileUpdatePacket() {
        return new PebbleFissionCoolerUpdatePacket(worldPosition, isProcessing, time, 1D, getTanks(), masterPortPos, getFilterTanks(), cluster);
    }

    @Override
    public void onTileUpdatePacket(PebbleFissionCoolerUpdatePacket message) {
        IBasicProcessor.super.onTileUpdatePacket(message);
        if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
            refreshMasterPort();
        }
        Tank.TankInfo.readInfoList(message.filterTankInfos, getFilterTanks());
        clusterHeatStored = message.clusterHeatStored;
        clusterHeatCapacity = message.clusterHeatCapacity;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeTanks(nbt, registries);
        writeProcessorNBT(nbt, registries);

        nbt.putInt("baseProcessCooling", baseProcessCooling);
        nbt.putDouble("heatingSpeedMultiplier", heatingSpeedMultiplier);

        nbt.putLong("clusterHeat", heat);
        nbt.putBoolean("isInValidPosition", isInValidPosition);

        nbt.putBoolean("isRunningSimulated", isRunningSimulated);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);

        readTanks(nbt, registries);
        readProcessorNBT(nbt, registries);

        baseProcessCooling = nbt.getInt("baseProcessCooling");
        heatingSpeedMultiplier = nbt.getDouble("heatingSpeedMultiplier");

        heat = nbt.getLong("clusterHeat");
        isInValidPosition = nbt.getBoolean("isInValidPosition");

        isRunningSimulated = nbt.getBoolean("isRunningSimulated");
    }

    @Override
    public CompoundTag writeTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).writeToNBT(nbt, registries, "tanks" + i);
        }
        for (int i = 0; i < filterTanks.size(); ++i) {
            filterTanks.get(i).writeToNBT(nbt, registries, "filterTanks" + i);
        }
        return nbt;
    }

    @Override
    public void readTanks(CompoundTag nbt, HolderLookup.Provider registries) {
        for (int i = 0; i < tanks.size(); ++i) {
            tanks.get(i).readFromNBT(nbt, registries, "tanks" + i);
        }
        for (int i = 0; i < filterTanks.size(); ++i) {
            filterTanks.get(i).readFromNBT(nbt, registries, "filterTanks" + i);
        }
    }

    // ComputerCraft

    @Override
    public String getCCKey() {
        return "cooler";
    }

    @Override
    public Object getCCInfo() {
        Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
        List<Tank> tanks = getTanks();
        entry.put("coolant", CCHelper.tankInfo(tanks.get(0)));
        entry.put("hot_coolant", CCHelper.tankInfo(tanks.get(1)));
        entry.put("cooling", getCooling(false));
        entry.put("speed_multiplier", getSpeedMultiplier());
        entry.put("is_processing", getIsProcessing());
        entry.put("current_time", getCurrentTime());
        entry.put("base_process_time", getBaseProcessTime());
        return entry;
    }
}