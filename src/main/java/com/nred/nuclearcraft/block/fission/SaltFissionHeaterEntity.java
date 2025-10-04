package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.block.fission.IFissionFuelComponent.ModeratorBlockInfo;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionPlacement;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.FissionCoolantHeaterType;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Set;

import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class SaltFissionHeaterEntity extends AbstractFissionEntity implements IFissionCoolingComponent { // , IFissionPortTarget<TileFissionHeaterPort, TileSaltFissionHeater> TODO
    public FissionCoolantHeaterType heaterType;
    public PlacementRule<FissionReactor, AbstractFissionEntity> placementRule;
    public Double coolingRate;

    protected int baseProcessCooling;
    public double heatingSpeedMultiplier; // Based on the cluster efficiency, but with heat/cooling taken into account

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs, hasConsumed;
    public boolean isRunningSimulated;

//    protected RecipeInfo<BasicRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

//    public String heaterType, coolantName;

    protected FissionCluster cluster = null;
    protected long heat = 0L;
    protected boolean isInValidPosition = false;

    public long clusterHeatStored, clusterHeatCapacity;

    protected BlockPos masterPortPos = DEFAULT_NON;
//    protected FissionHeaterPortEntity masterPort = null;


    public SaltFissionHeaterEntity(final BlockPos position, final BlockState blockState, FissionCoolantHeaterType heaterType) {
        super(FISSION_ENTITY_TYPE.get("coolant_heater").get(), position, blockState);
        this.heaterType = heaterType;
        this.placementRule = FissionPlacement.RULE_MAP.get(heaterType.getName() + "_coil");
        this.coolingRate = heaterType.getCoolingRate();
    }

    public static class Variant extends SaltFissionHeaterEntity {
        protected Variant(final BlockPos position, final BlockState blockState, FissionCoolantHeaterType dynamoCoilType) {
            super(position, blockState, dynamoCoilType);
        }
    }

    public static class Standard extends SaltFissionHeaterEntity.Variant {
        public Standard(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.STANDARD);
        }
    }

    public static class Iron extends SaltFissionHeaterEntity.Variant {
        public Iron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.IRON);
        }
    }

    public static class Redstone extends SaltFissionHeaterEntity.Variant {
        public Redstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.REDSTONE);
        }
    }

    public static class Quartz extends SaltFissionHeaterEntity.Variant {
        public Quartz(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.QUARTZ);
        }
    }

    public static class Obsidian extends SaltFissionHeaterEntity.Variant {
        public Obsidian(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.OBSIDIAN);
        }
    }

    public static class NetherBrick extends SaltFissionHeaterEntity.Variant {
        public NetherBrick(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.NETHER_BRICK);
        }
    }

    public static class Glowstone extends SaltFissionHeaterEntity.Variant {
        public Glowstone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.GLOWSTONE);
        }
    }

    public static class Lapis extends SaltFissionHeaterEntity.Variant {
        public Lapis(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LAPIS);
        }
    }

    public static class Gold extends SaltFissionHeaterEntity.Variant {
        public Gold(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.GOLD);
        }
    }

    public static class Prismarine extends SaltFissionHeaterEntity.Variant {
        public Prismarine(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.PRISMARINE);
        }
    }

    public static class Slime extends SaltFissionHeaterEntity.Variant {
        public Slime(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.SLIME);
        }
    }

    public static class EndStone extends SaltFissionHeaterEntity.Variant {
        public EndStone(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.END_STONE);
        }
    }

    public static class Purpur extends SaltFissionHeaterEntity.Variant {
        public Purpur(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.PURPUR);
        }
    }

    public static class Diamond extends SaltFissionHeaterEntity.Variant {
        public Diamond(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.DIAMOND);
        }
    }

    public static class Emerald extends SaltFissionHeaterEntity.Variant {
        public Emerald(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.EMERALD);
        }
    }

    public static class Copper extends SaltFissionHeaterEntity.Variant {
        public Copper(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.COPPER);
        }
    }

    public static class Tin extends SaltFissionHeaterEntity.Variant {
        public Tin(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.TIN);
        }
    }

    public static class Lead extends SaltFissionHeaterEntity.Variant {
        public Lead(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LEAD);
        }
    }

    public static class Boron extends SaltFissionHeaterEntity.Variant {
        public Boron(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.BORON);
        }
    }

    public static class Lithium extends SaltFissionHeaterEntity.Variant {
        public Lithium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LITHIUM);
        }
    }

    public static class Magnesium extends SaltFissionHeaterEntity.Variant {
        public Magnesium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.MAGNESIUM);
        }
    }

    public static class Manganese extends SaltFissionHeaterEntity.Variant {
        public Manganese(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.MANGANESE);
        }
    }

    public static class Aluminum extends SaltFissionHeaterEntity.Variant {
        public Aluminum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ALUMINUM);
        }
    }

    public static class Silver extends SaltFissionHeaterEntity.Variant {
        public Silver(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.SILVER);
        }
    }

    public static class Fluorite extends SaltFissionHeaterEntity.Variant {
        public Fluorite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.FLUORITE);
        }
    }

    public static class Villiaumite extends SaltFissionHeaterEntity.Variant {
        public Villiaumite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.VILLIAUMITE);
        }
    }

    public static class Carobbiite extends SaltFissionHeaterEntity.Variant {
        public Carobbiite(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.CAROBBIITE);
        }
    }

    public static class Arsenic extends SaltFissionHeaterEntity.Variant {
        public Arsenic(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ARSENIC);
        }
    }

    public static class LiquidNitrogen extends SaltFissionHeaterEntity.Variant {
        public LiquidNitrogen(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LIQUID_NITROGEN);
        }
    }

    public static class LiquidHelium extends SaltFissionHeaterEntity.Variant {
        public LiquidHelium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.LIQUID_HELIUM);
        }
    }

    public static class Enderium extends SaltFissionHeaterEntity.Variant {
        public Enderium(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.ENDERIUM);
        }
    }

    public static class Cryotheum extends SaltFissionHeaterEntity.Variant {
        public Cryotheum(final BlockPos position, final BlockState blockState) {
            super(position, blockState, FissionCoolantHeaterType.CRYOTHEUM);
        }
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
        heatingSpeedMultiplier = 0;

//        refreshAll();
    }

    @Override
    public boolean isClusterRoot() {
        return false;
    }

    @Override
    public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
//        refreshDirty();

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
//        hasConsumed = hasConsumed();
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

//    // IFissionPortTarget
//
//    @Override
//    public BlockPos getMasterPortPos() {
//        return masterPortPos;
//    }
//
//
//    @Override
//    public void setMasterPortPos(BlockPos pos) {
//        masterPortPos = pos;
//    }
//
//    @Override
//    public void clearMasterPort() {
//        masterPort = null;
//        masterPortPos = DEFAULT_NON;
//    }
//
//    @Override
//    public void refreshMasterPort() {
//        FissionReactor multiblock = getMultiblock();
//        masterPort = multiblock == null ? null : multiblock.getPartMap(TileFissionHeaterPort.class).get(masterPortPos.toLong());
//        if (masterPort == null) {
//            masterPortPos = DEFAULT_NON;
//        }
//    }

//    @Override
//    public void onPortRefresh() {
//        refreshAll();
//    }

    // Ticking

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide) {
//            refreshMasterPort(); TODO
//            refreshAll();
        }
    }

//    @Override
//    public void update() {
//        if (!world.isRemote) {
//            FissionReactor reactor = getMultiblock();
//            boolean shouldRefresh = reactor != null && reactor.isReactorOn && (cluster == null || !isInValidPosition) && isProcessing(false, false);
//
//            if (onTick()) {
//                markDirty();
//            }
//
//            if (shouldRefresh) {
//                getMultiblock().refreshFlag = true;
//            }
//        }
//    }
//
//    @Override
//    public void refreshAll() {
//        refreshDirty();
//        refreshIsProcessing(true, false);
//    }
//
//    @Override
//    public void refreshRecipe() {
//        boolean hasConsumed = getHasConsumed();
//        recipeInfo = NCRecipes.coolant_heater.getRecipeInfoFromHeaterInputs(heaterType, getFluidInputs(hasConsumed));
//        if (info.consumesInputs) {
//            consumeInputs();
//        }
//    }

//    @Override
//    public void refreshActivity() {
//        canProcessInputs = canProcessInputs();
//    }

    public boolean isRunning(boolean simulate) {
        return simulate ? isRunningSimulated : isProcessing;
    }

//    // IProcessor
//
//    @Override
//    public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TileSaltFissionHeater, SaltFissionHeaterUpdatePacket> getContainerInfo() {
//        return info;
//    }
//
//    @Override
//    public BasicRecipeHandler getRecipeHandler() {
//        return NCRecipes.coolant_heater;
//    }
//
//    @Override
//    public RecipeInfo<BasicRecipe> getRecipeInfo() {
//        return recipeInfo;
//    }
//
//    @Override
//    public void setRecipeInfo(RecipeInfo<BasicRecipe> recipeInfo) {
//        this.recipeInfo = recipeInfo;
//    }
//
//    @Override
//    public void setRecipeStats(@Nullable BasicRecipe recipe) {
//        baseProcessCooling = recipe == null ? 0 : recipe.getCoolantHeaterCoolingRate();
//        placementRule = FissionPlacement.RULE_MAP.get(recipe == null ? "" : recipe.getCoolantHeaterPlacementRule());
//    }
//
//    @Override
//    public @Nonnull NonNullList<ItemStack> getConsumedStacks() {
//        return consumedStacks;
//    }
//
//    @Override
//    public @Nonnull List<Tank> getConsumedTanks() {
//        return consumedTanks;
//    }
//
//    @Override
//    public double getBaseProcessTime() {
//        return 1D;
//    }
//
//    @Override
//    public void setBaseProcessTime(double baseProcessTime) {}
//
//    @Override
//    public double getBaseProcessPower() {
//        return 0D;
//    }
//
//    @Override
//    public void setBaseProcessPower(double baseProcessPower) {}
//
//    @Override
//    public double getCurrentTime() {
//        return time;
//    }
//
//    @Override
//    public void setCurrentTime(double time) {
//        this.time = time;
//    }
//
//    @Override
//    public double getResetTime() {
//        return resetTime;
//    }
//
//    @Override
//    public void setResetTime(double resetTime) {
//        this.resetTime = resetTime;
//    }
//
//    @Override
//    public boolean getIsProcessing() {
//        return isProcessing;
//    }
//
//    @Override
//    public void setIsProcessing(boolean isProcessing) {
//        this.isProcessing = isProcessing;
//    }
//
//    @Override
//    public boolean getCanProcessInputs() {
//        return canProcessInputs;
//    }
//
//    @Override
//    public void setCanProcessInputs(boolean canProcessInputs) {
//        this.canProcessInputs = canProcessInputs;
//    }
//
//    @Override
//    public boolean getHasConsumed() {
//        return hasConsumed;
//    }
//
//    @Override
//    public void setHasConsumed(boolean hasConsumed) {
//        this.hasConsumed = hasConsumed;
//    }
//
//    @Override
//    public double getSpeedMultiplier() {
//        return heatingSpeedMultiplier;
//    }
//
//    @Override
//    public double getPowerMultiplier() {
//        return 0D;
//    }
//
//    @Override
//    public boolean isProcessing() {
//        return !isSimulation() && isProcessing(true, false);
//    }

    public boolean isProcessing(boolean checkValid, boolean simulate) {
        return readyToProcess(checkValid);
    }

//    @Override
//    public boolean readyToProcess() {
//        return readyToProcess(true);
//    }

    public boolean readyToProcess(boolean checkValid) {
        return canProcessInputs && hasConsumed && isMachineAssembled() && (!checkValid || (cluster != null && isInValidPosition));
    }

//    @Override
//    public void finishProcess() {
//        int oldProcessCooling = baseProcessCooling;
//        produceProducts();
//        refreshRecipe();
//        time = Math.max(0D, time - 1D);
//        refreshActivityOnProduction();
//        if (!canProcessInputs) {
//            time = 0;
//        }
//
//        FissionReactor multiblock = getMultiblock();
//        if (multiblock != null) {
//            if (canProcessInputs) {
//                if (oldProcessCooling != baseProcessCooling) {
//                    multiblock.addClusterToRefresh(cluster);
//                }
//            }
//            else {
//                multiblock.refreshFlag = true;
//            }
//        }
//    }

//    // ITileInventory
//
//    @Override
//    public String getName() {
//        return inventoryName;
//    }
//
//    @Override
//    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
//        return inventoryStacks;
//    }
//
//    @Override
//    public void markDirty() {
//        refreshDirty();
//        super.markDirty();
//    }
//
//    @Override
//    public @Nonnull InventoryConnection[] getInventoryConnections() {
//        return inventoryConnections;
//    }
//
//    @Override
//    public void setInventoryConnections(@Nonnull InventoryConnection[] connections) {
//        inventoryConnections = connections;
//    }
//
//    @Override
//    public ItemOutputSetting getItemOutputSetting(int slot) {
//        return ItemOutputSetting.DEFAULT;
//    }
//
//    @Override
//    public void setItemOutputSetting(int slot, ItemOutputSetting setting) {}
//
//    // ITileFluid
//
//    @Override
//    public @Nonnull List<Tank> getTanks() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getTanks() : tanks;
//    }
//
//    @Override
//    public @Nonnull FluidConnection[] getFluidConnections() {
//        return fluidConnections;
//    }
//
//    @Override
//    public void setFluidConnections(@Nonnull FluidConnection[] connections) {
//        fluidConnections = connections;
//    }
//
//    @Override
//    public @Nonnull FluidTileWrapper[] getFluidSides() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFluidSides() : fluidSides;
//    }
//
//    @Override
//    public @Nonnull GasTileWrapper getGasWrapper() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getGasWrapper() : gasWrapper;
//    }
//
//    @Override
//    public boolean getInputTanksSeparated() {
//        return false;
//    }
//
//    @Override
//    public void setInputTanksSeparated(boolean separated) {}
//
//    @Override
//    public boolean getVoidUnusableFluidInput(int tankNumber) {
//        return false;
//    }
//
//    @Override
//    public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}
//
//    @Override
//    public TankOutputSetting getTankOutputSetting(int tankNumber) {
//        return TankOutputSetting.DEFAULT;
//    }
//
//    @Override
//    public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
//
//    @Override
//    public boolean hasConfigurableFluidConnections() {
//        return false;
//    }
//
//    @Override
//    public void clearAllTanks() {
//        for (Tank tank : tanks) {
//            tank.setFluidStored(null);
//        }
//        for (Tank tank : consumedTanks) {
//            tank.setFluidStored(null);
//        }
//        refreshAll();
//    }
//
//    // ITileFilteredFluid
//
//    @Override
//    public @Nonnull List<Tank> getTanksInternal() {
//        return tanks;
//    }
//
//    @Override
//    public @Nonnull List<Tank> getFilterTanks() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterTanks() : filterTanks;
//    }
//
//    @Override
//    public boolean canModifyFilter(int tank) {
//        return !isMultiblockAssembled();
//    }
//
//    @Override
//    public void onFilterChanged(int slot) {
//        markDirty();
//    }
//
//    @Override
//    public Object getFilterKey() {
//        return heaterType;
//    }
//
//    // ITileGui
//
//    @Override
//    public Set<EntityPlayer> getTileUpdatePacketListeners() {
//        return updatePacketListeners;
//    }
//
//    @Override
//    public SaltFissionHeaterUpdatePacket getTileUpdatePacket() {
//        return new SaltFissionHeaterUpdatePacket(pos, isProcessing, time, 1D, getTanks(), masterPortPos, getFilterTanks(), cluster);
//    }
//
//    @Override
//    public void onTileUpdatePacket(SaltFissionHeaterUpdatePacket message) {
//        IBasicProcessor.super.onTileUpdatePacket(message);
//        if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
//            refreshMasterPort();
//        }
//        TankInfo.readInfoList(message.filterTankInfos, getFilterTanks());
//        clusterHeatStored = message.clusterHeatStored;
//        clusterHeatCapacity = message.clusterHeatCapacity;
//    }

    // NBT TODO
//
//    @Override
//    public NBTTagCompound writeAll(NBTTagCompound nbt) {
//        super.writeAll(nbt);
//        nbt.putString("heaterName", heaterType);
//        writeTanks(nbt);
//
//        writeProcessorNBT(nbt);
//
//        nbt.putInt("baseProcessCooling", baseProcessCooling);
//        nbt.putDouble("heatingSpeedMultiplier", heatingSpeedMultiplier);
//
//        nbt.putLong("clusterHeat", heat);
//        nbt.putBoolean("isInValidPosition", isInValidPosition);
//
//        nbt.putBoolean("isRunningSimulated", isRunningSimulated);
//        return nbt;
//    }
//
//    @Override
//    public void readAll(NBTTagCompound nbt) {
//        super.readAll(nbt);
//        if (nbt.hasKey("heaterName")) {
//            heaterType = nbt.getString("heaterName");
//        }
//
//        if (DYN_COOLANT_NAME_MAP.containsKey(heaterType)) {
//            coolantName = DYN_COOLANT_NAME_MAP.get(heaterType);
//            tanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
//            filterTanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
//        }
//
//        readTanks(nbt);
//
//        readProcessorNBT(nbt);
//
//        baseProcessCooling = nbt.getInt("baseProcessCooling");
//        heatingSpeedMultiplier = nbt.getDouble("heatingSpeedMultiplier");
//
//        heat = nbt.getLong("clusterHeat");
//        isInValidPosition = nbt.getBoolean("isInValidPosition");
//
//        isRunningSimulated = nbt.getBoolean("isRunningSimulated");
//    }
//
//    @Override
//    public NBTTagCompound writeTanks(NBTTagCompound nbt) {
//        for (int i = 0; i < tanks.size(); ++i) {
//            tanks.get(i).writeToNBT(nbt, "tanks" + i);
//        }
//        for (int i = 0; i < filterTanks.size(); ++i) {
//            filterTanks.get(i).writeToNBT(nbt, "filterTanks" + i);
//        }
//        for (int i = 0; i < consumedTanks.size(); ++i) {
//            consumedTanks.get(i).writeToNBT(nbt, "consumedTanks" + i);
//        }
//        return nbt;
//    }
//
//    @Override
//    public void readTanks(NBTTagCompound nbt) {
//        for (int i = 0; i < tanks.size(); ++i) {
//            tanks.get(i).readFromNBT(nbt, "tanks" + i);
//        }
//        for (int i = 0; i < filterTanks.size(); ++i) {
//            filterTanks.get(i).readFromNBT(nbt, "filterTanks" + i);
//        }
//        for (int i = 0; i < consumedTanks.size(); ++i) {
//            consumedTanks.get(i).readFromNBT(nbt, "consumedTanks" + i);
//        }
//    }

//    // Capability TODO
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY)) {
//            return !getTanks().isEmpty() && hasFluidSideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
//        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
//            if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
//                return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
//            }
//            return null;
//        }
//        else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
//            if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
//                return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
}
