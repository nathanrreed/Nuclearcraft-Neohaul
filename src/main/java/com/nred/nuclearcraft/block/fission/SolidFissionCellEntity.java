package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import static com.nred.nuclearcraft.config.Config2.*;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class SolidFissionCellEntity extends AbstractFissionEntity implements IFissionFuelComponent {
//    protected final ProcessorContainerInfoImpl.BasicProcessorContainerInfo<SolidFissionCellEntity, SolidFissionCellUpdatePacket> info;

//    protected final @Nonnull String inventoryName;
//
//    protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
//    protected final @Nonnull NonNullList<ItemStack> consumedStacks;
//
//    protected final @Nonnull NonNullList<ItemStack> filterStacks;
//
//    protected @Nonnull InventoryConnection[] inventoryConnections;
//
//    protected final @Nonnull List<Tank> tanks;
//    protected final @Nonnull List<Tank> consumedTanks;

//    protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Collections.emptyList());
//
//    protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
//    protected @Nonnull GasTileWrapper gasWrapper = new GasTileWrapper(this);

    public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessDecayFactor = 0D, baseProcessRadiation = 0D;
    public int baseProcessHeat = 0, baseProcessCriticality = 1;
    protected boolean selfPriming = false;

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs, hasConsumed;
    public boolean isRunningSimulated;

    public double decayProcessHeat = 0D, decayHeatFraction = 0D, iodineFraction = 0D, poisonFraction = 0D;

//    protected RecipeInfo<BasicRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    protected long flux = 0;

    protected FissionCluster cluster = null;
    protected long heat = 0L;

    public long clusterHeatStored, clusterHeatCapacity;

    protected boolean primed = false, fluxSearched = false;

    public long heatMult = 0L;
    protected double undercoolingLifetimeFactor = 1D;
    protected Double sourceEfficiency = null;
    protected long[] moderatorLineFluxes = new long[]{0L, 0L, 0L, 0L, 0L, 0L};
    protected Double[] moderatorLineEfficiencies = new Double[]{null, null, null, null, null, null};
    protected IFissionFluxSink[] adjacentFluxSinks = new IFissionFluxSink[]{null, null, null, null, null, null};
    protected final LongSet[] passiveModeratorCaches = new LongSet[]{new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
    protected final LongSet[] activeModeratorCaches = new LongSet[]{new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
    protected final ModeratorLine[] moderatorLineCaches = new ModeratorLine[]{null, null, null, null, null, null};
    protected final LongSet[] passiveReflectorModeratorCaches = new LongSet[]{new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
    protected final LongSet[] activeReflectorModeratorCaches = new LongSet[]{new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet(), new LongOpenHashSet()};
    protected final LongSet activeReflectorCache = new LongOpenHashSet();

    protected BlockPos masterPortPos = DEFAULT_NON;
    protected SolidFissionCellEntity masterPort = null;

    public SolidFissionCellEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("cell").get(), position, blockState);
    }

    // IFissionFuelComponent

    @Override
    public @javax.annotation.Nullable FissionCluster getCluster() {
        return cluster;
    }

    @Override
    public void setClusterInternal(@javax.annotation.Nullable FissionCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public boolean isValidHeatConductor(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        return isRunning(simulate) || getDecayHeating() > 0;
    }

    @Override
    public boolean isFunctional(boolean simulate) {
        return isRunning(simulate) || getDecayHeating() > 0;
    }

    @Override
    public void resetStats() {
        // primed = false;
        fluxSearched = false;
        flux = heatMult = 0L;
        undercoolingLifetimeFactor = 1D;
        // sourceEfficiency = null;
        for (int i = 0; i < 6; ++i) {
            moderatorLineFluxes[i] = 0;
            moderatorLineEfficiencies[i] = null;
            adjacentFluxSinks[i] = null;
            passiveModeratorCaches[i].clear();
            activeModeratorCaches[i].clear();
            moderatorLineCaches[i] = null;
            passiveReflectorModeratorCaches[i].clear();
            activeReflectorModeratorCaches[i].clear();
        }
        activeReflectorCache.clear();

//        refreshAll();
    }

    @Override
    public boolean isClusterRoot() {
        return true;
    }

    @Override
    public void clusterSearch(Integer id, final Object2IntMap<IFissionComponent> clusterSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
//        refreshDirty(); TODO
        refreshIsProcessing(false, simulate);

        IFissionFuelComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
    }

    @Override
    public boolean isProducingFlux(boolean simulate) {
        return isPrimed(simulate) || isRunning(simulate);
    }

    @Override
    public void tryPriming(FissionReactor sourceReactor, boolean fromSource, boolean simulate) {
        if (getMultiblockController().isEmpty() && getMultiblockController().get() != sourceReactor) {
            return;
        }

        if (canProcessInputs) {
            primed = true;
        }
    }

    @Override
    public boolean isPrimed(boolean simulate) {
        return primed;
    }

    @Override
    public void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache) {
        primedCache.add(this);
    }

    @Override
    public void unprime(boolean simulate) {
        primed = false;
    }

    @Override
    public boolean isAcceptingFlux(Direction side, boolean simulate) {
        return canProcessInputs;
    }

    @Override
    public void refreshIsProcessing(boolean checkCluster, boolean simulate) {
        if (simulate) {
            isProcessing = false;
//            isRunningSimulated = isProcessing(checkCluster, simulate); TODO
        } else {
//            isProcessing = isProcessing(checkCluster, simulate);
            isRunningSimulated = false;
        }
//        hasConsumed = hasConsumed();
    }

    @Override
    public boolean isFluxSearched() {
        return fluxSearched;
    }

    @Override
    public void setFluxSearched(boolean fluxSearched) {
        this.fluxSearched = fluxSearched;
    }

    @Override
    public void addToFluxSearchCache(final ObjectSet<IFissionFuelComponent> fluxSearchCache) {
        fluxSearchCache.add(this);
    }

    @Override
    public void incrementHeatMultiplier() {
        ++heatMult;
    }

    @Override
    public double getSourceEfficiency() {
        return sourceEfficiency == null ? 1D : sourceEfficiency;
    }

    @Override
    public void setSourceEfficiency(double sourceEfficiency, boolean maximize) {
        this.sourceEfficiency = this.sourceEfficiency != null && maximize ? Math.max(this.sourceEfficiency, sourceEfficiency) : sourceEfficiency;
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
    public long[] getModeratorLineFluxes() {
        return moderatorLineFluxes;
    }

    @Override
    public Double[] getModeratorLineEfficiencies() {
        return moderatorLineEfficiencies;
    }

    @Override
    public IFissionFluxSink[] getAdjacentFluxSinks() {
        return adjacentFluxSinks;
    }

    @Override
    public LongSet[] getPassiveModeratorCaches() {
        return passiveModeratorCaches;
    }

    @Override
    public LongSet[] getActiveModeratorCaches() {
        return activeModeratorCaches;
    }

    @Override
    public ModeratorLine[] getModeratorLineCaches() {
        return moderatorLineCaches;
    }

    @Override
    public LongSet[] getPassiveReflectorModeratorCaches() {
        return passiveReflectorModeratorCaches;
    }

    @Override
    public LongSet[] getActiveReflectorModeratorCaches() {
        return activeReflectorModeratorCaches;
    }

    @Override
    public LongSet getActiveReflectorCache() {
        return activeReflectorCache;
    }

    @Override
    public long getBaseProcessHeat() {
        return baseProcessHeat;
    }

    @Override
    public double getBaseProcessEfficiency() {
        return baseProcessEfficiency;
    }

    @Override
    public long getRawHeating(boolean simulate) {
        return isRunning(simulate) ? (long) baseProcessHeat * heatMult : 0L;
    }

    @Override
    public long getRawHeatingIgnoreCoolingPenalty(boolean simulate) {
        return isRunning(simulate) ? 0L : getDecayHeating();
    }

    @Override
    public double getEffectiveHeating(boolean simulate) {
        return isRunning(simulate) ? baseProcessHeat * getEfficiency(simulate) : 0D;
    }

    @Override
    public double getEffectiveHeatingIgnoreCoolingPenalty(boolean simulate) {
        return isRunning(simulate) ? 0D : getFloatingPointDecayHeating();
    }

    @Override
    public long getHeatMultiplier(boolean simulate) {
        return heatMult;
    }

    @Override
    public double getFluxEfficiencyFactor() {
        return (1D + Math.exp(-2D * getFloatingPointCriticality())) / (1D + Math.exp(2D * (flux - 2D * getFloatingPointCriticality())));
    }

    @Override
    public double getEfficiency(boolean simulate) {
        return isRunning(simulate) ? heatMult * baseProcessEfficiency * getSourceEfficiency() * getModeratorEfficiencyFactor() * getFluxEfficiencyFactor() : 0D;
    }

    @Override
    public double getEfficiencyIgnoreCoolingPenalty(boolean simulate) {
        return isRunning(simulate) ? 0D : 1D;
    }

    @Override
    public void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor) {
        this.undercoolingLifetimeFactor = undercoolingLifetimeFactor;
    }

    @Override
    public long getCriticality() {
        return fission_decay_mechanics ? NCMath.toInt(getFloatingPointCriticality()) : baseProcessCriticality;
    }

    @Override
    public double getFloatingPointCriticality() {
        return fission_decay_mechanics ? baseProcessCriticality * (1D - baseProcessDecayFactor + poisonFraction) : baseProcessCriticality;
    }

    @Override
    public boolean isInitiallyPrimed(boolean simulate) {
        return selfPriming || isFunctional(simulate);
    }

    /**
     * Fix to force adjacent moderators to be active
     */
    @Override
    public void defaultRefreshModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (isRunning(simulate)) {
            defaultRefreshAdjacentActiveModerators(componentFailCache, assumedValidCache, simulate);
        } else if (getDecayHeating() > 0) {
            defaultForceAdjacentActiveModerators(componentFailCache, assumedValidCache, simulate);
        }
    }

    @Override
    public void onClusterMeltdown(Iterator<IFissionComponent> componentIterator) {
//        IRadiationSource chunkSource = RadiationHelper.getRadiationSource(level.getChunk(pos)); TODO
//        if (chunkSource != null) {
//            RadiationHelper.addToSourceRadiation(chunkSource, 8D * baseProcessRadiation * getSpeedMultiplier() * fission_meltdown_radiation_multiplier);
//        }

        componentIterator.remove();
        level.removeBlockEntity(worldPosition);

        BlockState corium = CUSTOM_FLUID_MAP.get("corium").block.get().defaultBlockState();
        level.setBlockAndUpdate(worldPosition, corium);

        Optional<FissionReactor> multiblock = getMultiblockController();
        if (multiblock.isPresent()) {
            for (Direction dir : Direction.values()) {
                BlockPos offPos = worldPosition.relative(dir);
                if (level.random.nextDouble() < 0.75D) {
                    level.removeBlockEntity(offPos);
                    level.setBlockAndUpdate(offPos, corium);
                }
            }
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

//    // IFissionPortTarget
//
//    @Override
//    public BlockPos getMasterPortPos() {
//        return masterPortPos;
//    }
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
//        masterPort = multiblock == null ? null : multiblock.getPartMap(TileFissionCellPort.class).get(masterPortPos.toLong());
//        if (masterPort == null) {
//            masterPortPos = DEFAULT_NON;
//        }
//    }
//
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
//        if (!level.isClientSide) {
//            FissionReactor reactor = getMultiblock();
//            boolean shouldRefresh = reactor != null && reactor.isReactorOn && cluster == null && isProcessing(false, false);
//
//            if (onTick()) {
//                markDirty();
//            }
//
//            updateDecayFractions();
//
//            if (shouldRefresh) {
//                getMultiblock().refreshFlag = true;
//            }
//        }
//    }

    public void updateDecayFractions() {
        if (!fission_decay_mechanics) {
            decayHeatFraction = iodineFraction = poisonFraction = 0D;
            return;
        }

        long oldCriticality = getCriticality();
        boolean oldHasEnoughFlux = hasEnoughFlux();
        int oldDecayHeating = getDecayHeating();

        boolean decayHeatReduce = true;
        boolean iodineReduce = true;
        boolean poisonReduce = true;

        double decayHeatEquilibrium = fission_decay_equilibrium_factors[0] * baseProcessDecayFactor;
        double iodineEquilibrium = fission_decay_equilibrium_factors[1] * baseProcessDecayFactor;
        double poisonEquilibrium = fission_decay_equilibrium_factors[2] * baseProcessDecayFactor;

        if (isProcessing) {
            if (decayHeatFraction <= decayHeatEquilibrium) {
                decayHeatFraction = Mth.clamp(decayHeatFraction + (fission_decay_term_multipliers[0] * (decayHeatEquilibrium - decayHeatFraction) + fission_decay_term_multipliers[1] * decayHeatEquilibrium) / fission_decay_build_up_times[0], 0D, decayHeatEquilibrium);
                decayHeatReduce = false;
            }

            if (iodineFraction <= iodineEquilibrium) {
                iodineFraction = Mth.clamp(iodineFraction + (fission_decay_term_multipliers[0] * (iodineEquilibrium - iodineFraction) + fission_decay_term_multipliers[1] * iodineEquilibrium) / fission_decay_build_up_times[1], 0D, iodineEquilibrium);
                iodineReduce = false;
            }

            if (poisonFraction <= poisonEquilibrium) {
                poisonFraction = Mth.clamp(poisonFraction + (fission_decay_term_multipliers[0] * (poisonEquilibrium - poisonFraction) + fission_decay_term_multipliers[1] * poisonEquilibrium) / fission_decay_build_up_times[2], 0D, poisonEquilibrium);
                poisonReduce = false;
            }
        }

        double decayHeatFractionReduction = 0D;
        if (decayHeatReduce) {
            decayHeatFractionReduction = Math.min(decayHeatFraction, (fission_decay_term_multipliers[0] * decayHeatFraction + fission_decay_term_multipliers[1] * decayHeatEquilibrium) / fission_decay_lifetimes[0]);
            decayHeatFraction = Math.max(0D, decayHeatFraction - decayHeatFractionReduction);
        }

        double poisonParentFractionReduction = 0D;
        if (iodineReduce) {
            poisonParentFractionReduction = Math.min(iodineFraction, (fission_decay_term_multipliers[0] * iodineFraction + fission_decay_term_multipliers[1] * iodineEquilibrium) / fission_decay_lifetimes[1]);
            iodineFraction = Math.max(0D, iodineFraction - poisonParentFractionReduction + fission_decay_daughter_multipliers[0] * decayHeatFractionReduction);
        }

        double poisonFractionReduction = 0D;
        if (poisonReduce) {
            poisonFractionReduction = Math.min(poisonFraction, (fission_decay_term_multipliers[0] * poisonFraction + fission_decay_term_multipliers[1] * poisonEquilibrium) / fission_decay_lifetimes[2]);
            poisonFraction = Math.max(0D, poisonFraction - poisonFractionReduction + fission_decay_daughter_multipliers[1] * poisonParentFractionReduction);
        }

        boolean refreshReactor = false, refreshCluster = false;

        if (oldCriticality != getCriticality()) {
            if (isProcessing) {
                if (oldHasEnoughFlux && !hasEnoughFlux()) {
                    refreshReactor = true;
                } else {
                    refreshCluster = true;
                }
            } else if (oldCriticality > baseProcessCriticality && getCriticality() <= baseProcessCriticality) {
                refreshReactor = true;
            }
        }

        if (!isProcessing && oldDecayHeating != getDecayHeating()) {
            if (getDecayHeating() == 0) {
                refreshReactor = true;
            } else {
                refreshCluster = true;
            }
        }

        if (refreshReactor) {
            getMultiblockController().get().refreshFlag = true;
        } else if (refreshCluster) {
            getMultiblockController().get().addClusterToRefresh(cluster);
        }
    }

    @Override
    public int getDecayHeating() {
        return fission_decay_mechanics ? NCMath.toInt(getFloatingPointDecayHeating()) : 0;
    }

    @Override
    public double getFloatingPointDecayHeating() {
        return fission_decay_mechanics ? decayProcessHeat * decayHeatFraction : 0D;
    }

//    @Override
//    public void refreshAll() {
//        refreshDirty();
//        refreshIsProcessing(true, false);
//    }
//
//    @Override
//    public void refreshActivity() {
//        boolean wasReady = readyToProcess(false);
//        canProcessInputs = canProcessInputs();
//        Optional<FissionReactor> multiblock = getMultiblockController();
//        if (multiblock.isPresent() && !wasReady && readyToProcess(false)) {
//            multiblock.refreshFlag = true;
//        }
//    }

    @Override
    public boolean isRunning(boolean simulate) {
        return simulate ? isRunningSimulated : isProcessing;
    }

//    // IProcessor
//
//    @Override
//    public ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TileSolidFissionCell, SolidFissionCellUpdatePacket> getContainerInfo() {
//        return info;
//    }
//
//    @Override
//    public BasicRecipeHandler getRecipeHandler() {
//        return NCRecipes.solid_fission;
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
//    public void setRecipeStats(@javax.annotation.Nullable BasicRecipe recipe) {
//        baseProcessTime = recipe == null ? 1D : recipe.getFissionFuelTime();
//        baseProcessHeat = recipe == null ? 0 : recipe.getFissionFuelHeat();
//        baseProcessEfficiency = recipe == null ? 0D : recipe.getFissionFuelEfficiency();
//        baseProcessCriticality = recipe == null ? 1 : recipe.getFissionFuelCriticality();
//        selfPriming = recipe != null && recipe.getFissionFuelSelfPriming();
//        baseProcessRadiation = recipe == null ? 0D : recipe.getFissionFuelRadiation();
//
//        if (recipe != null) {
//            decayProcessHeat = baseProcessHeat;
//            baseProcessDecayFactor = recipe.getFissionFuelDecayFactor();
//        }
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
//        return baseProcessTime;
//    }
//
//    @Override
//    public void setBaseProcessTime(double baseProcessTime) {
//        this.baseProcessTime = baseProcessTime;
//    }
//
//    @Override
//    public double getBaseProcessPower() {
//        return 0D;
//    }
//
//    @Override
//    public void setBaseProcessPower(double baseProcessPower) {
//    }
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
//        return 1D / undercoolingLifetimeFactor;
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
//
//    public boolean isProcessing(boolean checkCluster, boolean simulate) {
//        return readyToProcess(checkCluster) && hasEnoughFlux();
//    }
//
//    @Override
//    public boolean readyToProcess() {
//        return readyToProcess(true);
//    }

    public boolean readyToProcess(boolean checkCluster) {
        return canProcessInputs && hasConsumed && isMachineAssembled() && (!checkCluster || cluster != null);
    }

    public boolean hasEnoughFlux() {
        return flux >= getCriticality();
    }

//    @Override
//    public void process() {
//        getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier());
//        IBasicProcessor.super.process();
//    }
//
//    @Override
//    public void finishProcess() {
//        double oldProcessTime = baseProcessTime, oldProcessEfficiency = baseProcessEfficiency, oldProcessDecayFactor = baseProcessDecayFactor;
//        int oldProcessHeat = baseProcessHeat;
//        long oldCriticality = getCriticality();
//        produceProducts();
//        refreshRecipe();
//        time = Math.max(0D, time - oldProcessTime);
//        refreshActivityOnProduction();
//        if (!canProcessInputs) {
//            time = 0;
//        }
//
//        FissionReactor multiblock = getMultiblock();
//        if (multiblock != null) {
//            if (canProcessInputs) {
//                if (oldProcessHeat != baseProcessHeat || oldProcessEfficiency != baseProcessEfficiency || oldProcessDecayFactor != baseProcessDecayFactor || oldCriticality != getCriticality()) {
//                    if (!hasEnoughFlux()) {
//                        multiblock.refreshFlag = true;
//                    } else {
//                        multiblock.addClusterToRefresh(cluster);
//                    }
//                }
//            } else {
//                sourceEfficiency = null;
//                multiblock.refreshFlag = true;
//            }
//        }
//    }
//
//    @Override
//    public int getItemProductCapacity(int slot, ItemStack stack) {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStackLimit() : IBasicProcessor.super.getItemProductCapacity(slot, stack);
//    }

//    // ITileInventory TODO
//
//    @Override
//    public String getName() {
//        return inventoryName;
//    }
//
//    @Override
//    public @Nonnull NonNullList<ItemStack> getInventoryStacks() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStacks() : inventoryStacks;
//    }
//
//    @Override
//    public @Nonnull NonNullList<ItemStack> getInventoryStacksInternal() {
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
//    public boolean isItemValidForSlot(int slot, ItemStack stack) {
//        if (stack.isEmpty() || (slot >= info.itemInputSize && slot < info.itemInputSize + info.itemOutputSize)) {
//            return false;
//        }
//        ItemStack filter = getFilterStacks().get(slot);
//        if (!filter.isEmpty() && !stack.isItemEqual(filter)) {
//            return false;
//        }
//        return isItemValidForSlotInternal(slot, stack);
//    }
//
//    @Override
//    public boolean isItemValidForSlotInternal(int slot, ItemStack stack) {
//        return IBasicProcessor.super.isItemValidForSlot(slot, stack);
//    }
//
//    @Override
//    public int getInventoryStackLimit() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getInventoryStackLimit() : IBasicProcessor.super.getInventoryStackLimit();
//    }
//
//    @Override
//    public void clearAllSlots() {
//        Collections.fill(inventoryStacks, ItemStack.EMPTY);
//        Collections.fill(consumedStacks, ItemStack.EMPTY);
//        refreshAll();
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
//    // ITileFilteredInventory
//
//    @Override
//    public @Nonnull NonNullList<ItemStack> getFilterStacks() {
//        return !DEFAULT_NON.equals(masterPortPos) ? masterPort.getFilterStacks() : filterStacks;
//    }
//
//    @Override
//    public boolean canModifyFilter(int slot) {
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
//        return getFilterStacks().get(0).isEmpty() ? 0 : RecipeItemHelper.pack(getFilterStacks().get(0));
//    }
//
//    // ITileFluid
//
//    @Override
//    public @Nonnull List<Tank> getTanks() {
//        return tanks;
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
//        return fluidSides;
//    }
//
//    @Override
//    public @Nonnull GasTileWrapper getGasWrapper() {
//        return gasWrapper;
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
//    // ITileGui
//
//    @Override
//    public Set<EntityPlayer> getTileUpdatePacketListeners() {
//        return updatePacketListeners;
//    }
//
//    @Override
//    public SolidFissionCellUpdatePacket getTileUpdatePacket() {
//        return new SolidFissionCellUpdatePacket(pos, isProcessing, time, baseProcessTime, getTanks(), masterPortPos, getFilterStacks(), cluster);
//    }
//
//    @Override
//    public void onTileUpdatePacket(SolidFissionCellUpdatePacket message) {
//        IBasicProcessor.super.onTileUpdatePacket(message);
//        if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
//            refreshMasterPort();
//        }
//        IntStream.range(0, filterStacks.size()).forEach(x -> filterStacks.set(x, message.filterStacks.get(x)));
//        clusterHeatStored = message.clusterHeatStored;
//        clusterHeatCapacity = message.clusterHeatCapacity;
//    }
//
//    // NBT
//
//    @Override
//    public NBTTagCompound writeAll(NBTTagCompound nbt) {
//        super.writeAll(nbt);
//        writeInventory(nbt);
//        writeInventoryConnections(nbt);
//
//        writeProcessorNBT(nbt);
//
//        nbt.setDouble("baseProcessTime", baseProcessTime);
//        nbt.setInteger("baseProcessHeat", baseProcessHeat);
//        nbt.setDouble("baseProcessEfficiency", baseProcessEfficiency);
//        nbt.setInteger("baseProcessCriticality", baseProcessCriticality);
//        nbt.setDouble("baseProcessDecayFactor", baseProcessDecayFactor);
//        nbt.setBoolean("selfPriming", selfPriming);
//
//        nbt.setDouble("decayProcessHeat", decayProcessHeat);
//        nbt.setDouble("decayHeatFraction", decayHeatFraction);
//        nbt.setDouble("iodineFraction", iodineFraction);
//        nbt.setDouble("poisonFraction", poisonFraction);
//
//        nbt.setLong("flux", flux);
//        nbt.setLong("clusterHeat", heat);
//
//        nbt.setBoolean("isRunningSimulated", isRunningSimulated);
//        return nbt;
//    }
//
//    @Override
//    public void readAll(NBTTagCompound nbt) {
//        super.readAll(nbt);
//        readInventory(nbt);
//        readInventoryConnections(nbt);
//
//        readProcessorNBT(nbt);
//
//        baseProcessTime = nbt.getDouble("baseProcessTime");
//        baseProcessHeat = nbt.getInteger("baseProcessHeat");
//        baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
//        baseProcessCriticality = nbt.getInteger("baseProcessCriticality");
//        baseProcessDecayFactor = nbt.getDouble("baseProcessDecayFactor");
//        selfPriming = nbt.getBoolean("selfPriming");
//
//        decayProcessHeat = nbt.getDouble("decayProcessHeat");
//        decayHeatFraction = nbt.getDouble("decayHeatFraction");
//        iodineFraction = nbt.getDouble("iodineFraction");
//        poisonFraction = nbt.getDouble("poisonFraction");
//
//        flux = nbt.getLong("flux");
//        heat = nbt.getLong("clusterHeat");
//
//        isRunningSimulated = nbt.getBoolean("isRunningSimulated");
//    }
//
//    @Override
//    public NBTTagCompound writeInventory(NBTTagCompound nbt) {
//        NBTHelper.writeAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
//        return nbt;
//    }
//
//    @Override
//    public void readInventory(NBTTagCompound nbt) {
//        NBTHelper.readAllItems(nbt, inventoryStacks, filterStacks, consumedStacks);
//    }
//
//    // Capability
//
//    @Override
//    public boolean hasCapability(Capability<?> capability, @javax.annotation.Nullable Direction side) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return !getInventoryStacks().isEmpty() && hasInventorySideCapability(side);
//        }
//        return super.hasCapability(capability, side);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, @javax.annotation.Nullable Direction side) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            if (!getInventoryStacks().isEmpty() && hasInventorySideCapability(side)) {
//                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandler(side));
//            }
//            return null;
//        }
//        return super.getCapability(capability, side);
//    }
//
//    @Override
//    public IItemHandler getItemHandler(@Nullable Direction side) {
//        // ITileInventory tile = !DEFAULT_NON.equals(masterPortPos) ? masterPort : this;
//        // return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new PortItemHandler(tile, side));
//        return ITileFilteredInventory.super.getItemHandler(side);
//    }
}