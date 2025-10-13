package com.nred.nuclearcraft.block.fission;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.block.fission.port.FissionCoolerPortEntity;
import com.nred.nuclearcraft.block.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.info.ProcessorContainerInfoImpl;
import com.nred.nuclearcraft.block.internal.fluid.*;
import com.nred.nuclearcraft.block.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block.inventory.ITileInventory;
import com.nred.nuclearcraft.block.processor.IBasicProcessor;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.payload.multiblock.FissionCoolerUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.util.CCHelper;
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class FissionCoolerEntity extends AbstractFissionEntity implements IBasicProcessor<FissionCoolerEntity, FissionCoolerUpdatePacket>, ITileFilteredFluid, IFissionCoolingComponent, IFissionPortTarget<FissionCoolerPortEntity, FissionCoolerEntity> {
    protected final ProcessorContainerInfoImpl.BasicProcessorContainerInfo<FissionCoolerEntity, FissionCoolerUpdatePacket> info;

    protected final @Nonnull String inventoryName; // TODO is this needed?

    protected final @Nonnull NonNullList<ItemStack> inventoryStacks;

    protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Collections.emptyList());

    protected final @Nonnull List<Tank> tanks;

    protected final @Nonnull List<Tank> filterTanks;

    protected @Nonnull FluidConnection[] fluidConnections;

    protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
    protected @Nonnull ChemicalTileWrapper[] chemicalSides = ITileFluid.getDefaultChemicalSides(this);

    protected int baseProcessCooling;

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs;
    public boolean isRunningSimulated;

    protected RecipeInfo<BasicRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>(); // TODO Merge into parent?

    protected FissionCluster cluster = null;
    protected long heat = 0L;

    public long clusterHeatStored, clusterHeatCapacity;

    protected BlockPos masterPortPos = DEFAULT_NON;
    protected FissionCoolerPortEntity masterPort = null;

    public FissionCoolerEntity(BlockPos pos, BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("cooler").get(), pos, blockState);
        info = TileInfoHandler.getProcessorContainerInfo("fission_cooler");

        inventoryName = MODID + ".container." + info.name;

        inventoryStacks = NonNullList.withSize(0, ItemStack.EMPTY);

        Set<ResourceKey<Fluid>> validFluids = (Set<ResourceKey<Fluid>>) NCRecipes.fission_emergency_cooling.validFluids.get(0);
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
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null; // TODO
    }

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
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
        return true;
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isRunning(simulate);
    }

    @Override
    public void resetStats() {
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

    public void refreshIsProcessing(boolean checkCluster, boolean simulate) {
        if (simulate) {
            isProcessing = false;
            isRunningSimulated = isProcessing(checkCluster, simulate);
        } else {
            isProcessing = isProcessing(checkCluster, simulate);
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

    // Moderator Line

    @Override
    public ModeratorBlockInfo getModeratorBlockInfo(Direction dir, boolean activeModeratorPos) {
        return new ModeratorBlockInfo(worldPosition, this, false, false, 0, 0D);
    }

    @Override
    public void onAddedToModeratorCache(ModeratorBlockInfo thisInfo) {
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
        canProcessInputs = canProcessInputs();
    }

    public boolean isRunning(boolean simulate) {
        return simulate ? isRunningSimulated : isProcessing;
    }

    // IProcessor

    @Override
    public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<FissionCoolerEntity, FissionCoolerUpdatePacket> getContainerInfo() {
        return info;
    }

    @Override
    public BasicRecipeHandler<?> getRecipeHandler() { //TODO
        return NCRecipes.fission_emergency_cooling;
    }

    @Override
    public RecipeInfo<BasicRecipe> getRecipeInfo() {
        return recipeInfo;
    }

    @Override
    public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
        this.recipeInfo = recipeInfo;
    }

    @Override
    public void setRecipeStats(@Nullable BasicRecipe recipe) {
        // TODO
        baseProcessCooling = recipe == null ? 0 : recipe.getCoolantHeaterCoolingRate();
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

    @Override
    public double getSpeedMultiplier() {
        return 1D;
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
        return readyToProcess(checkCluster);
    }

    @Override
    public boolean readyToProcess() {
        return readyToProcess(true);
    }

    public boolean readyToProcess(boolean checkCluster) {
        return canProcessInputs && isMachineAssembled() && (!checkCluster || cluster != null);
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

//    @Override TODO REMOVE
//    public Component getName() {
//        return Component.translatable(inventoryName);
//    }

    @Override
    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
        return inventoryStacks;
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
        return getFilterTanks().get(0).getFluidName();
    }

    // ITileGui

    @Override
    public Set<Player> getTileUpdatePacketListeners() {
        return updatePacketListeners;
    }

    @Override
    public FissionCoolerUpdatePacket getTileUpdatePacket() {
        return new FissionCoolerUpdatePacket(worldPosition, isProcessing, time, 1D, getTanks(), masterPortPos, getFilterTanks(), cluster);
    }

    @Override
    public void onTileUpdatePacket(FissionCoolerUpdatePacket message) {
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

        nbt.putLong("clusterHeat", heat);

        nbt.putBoolean("isRunningSimulated", isRunningSimulated);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);

        readTanks(nbt, registries);
        readProcessorNBT(nbt, registries);

        baseProcessCooling = nbt.getInt("baseProcessCooling");

        heat = nbt.getLong("clusterHeat");

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

//    // Capability TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable Direction side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
//            return !getTanks().isEmpty() && hasFluidSideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable Direction side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//            if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
//                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
//            }
//            return null;
//        } else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
//            if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
//                return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
//
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