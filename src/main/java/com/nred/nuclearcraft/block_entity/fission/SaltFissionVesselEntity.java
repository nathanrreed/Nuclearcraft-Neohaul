package com.nred.nuclearcraft.block_entity.fission;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.IFissionPortTarget;
import com.nred.nuclearcraft.block_entity.fission.port.ITileFilteredFluid;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.internal.fluid.*;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.internal.inventory.InventoryConnection;
import com.nred.nuclearcraft.block_entity.internal.inventory.ItemOutputSetting;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;
import com.nred.nuclearcraft.block_entity.processor.IBasicProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl.SaltFissionVesselMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionFuelBunch;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.payload.multiblock.SaltFissionVesselUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.SaltFissionRecipe;
import com.nred.nuclearcraft.util.CCHelper;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.*;
import it.zerono.mods.zerocore.lib.multiblock.cuboid.PartPosition;
import it.zerono.mods.zerocore.lib.multiblock.validation.IMultiblockValidator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.FISSION_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.FluidRegistration.CUSTOM_FLUID_MAP;
import static com.nred.nuclearcraft.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static com.nred.nuclearcraft.util.PosHelper.DEFAULT_NON;

public class SaltFissionVesselEntity extends AbstractFissionEntity implements IBasicProcessor<SaltFissionVesselEntity, SaltFissionVesselUpdatePacket>, ITileFilteredFluid, IFissionFuelBunchComponent, IFissionPortTarget<FissionVesselPortEntity, SaltFissionVesselEntity> {
    protected final ProcessorMenuInfoImpl.BasicProcessorMenuInfo<SaltFissionVesselEntity, SaltFissionVesselUpdatePacket> info;

    protected final @Nonnull NonNullList<ItemStack> inventoryStacks;
    protected final @Nonnull NonNullList<ItemStack> consumedStacks;

    protected @Nonnull InventoryConnection[] inventoryConnections = ITileInventory.inventoryConnectionAll(Collections.emptyList());

    protected final @Nonnull List<Tank> tanks;
    protected final @Nonnull List<Tank> consumedTanks;

    protected final @Nonnull List<Tank> filterTanks;

    protected @Nonnull FluidConnection[] fluidConnections;

    protected @Nonnull FluidTileWrapper[] fluidSides = ITileFluid.getDefaultFluidSides(this);
    protected @Nonnull ChemicalTileWrapper[] chemicalSides = ITileFluid.getDefaultChemicalSides(this);

    public double baseProcessTime = 1D, baseProcessEfficiency = 0D, baseProcessDecayFactor = 0D, baseProcessRadiation = 0D;
    public int baseProcessHeat = 0, baseProcessCriticality = 1;
    protected boolean selfPriming = false;

    public double time, resetTime;
    public boolean isProcessing, canProcessInputs, hasConsumed;
    public boolean isRunningSimulated;

    public double decayProcessHeat = 0D, decayHeatFraction = 0D, iodineFraction = 0D, poisonFraction = 0D;

    protected RecipeInfo<SaltFissionRecipe> recipeInfo = null;

    protected final Set<Player> updatePacketListeners = new ObjectOpenHashSet<>();

    public long cachedFlux = 0L;

    protected FissionCluster cluster = null;
    protected long heat = 0L;

    public long clusterHeatStored, clusterHeatCapacity;

    protected boolean fluxSearched = false;

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
    protected FissionVesselPortEntity masterPort = null;

    protected FissionFuelBunch fuelBunch = null;

    public SaltFissionVesselEntity(final BlockPos position, final BlockState blockState) {
        super(FISSION_ENTITY_TYPE.get("vessel").get(), position, blockState);

        info = TileInfoHandler.getProcessorContainerInfo("salt_fission_vessel");

        inventoryStacks = NonNullList.withSize(0, ItemStack.EMPTY);
        consumedStacks = info.getConsumedStacks();

        Set<ResourceLocation> validFluids = (Set<ResourceLocation>) NCRecipes.salt_fission.validFluids.get(0);
        tanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, validFluids), new Tank(INGOT_BLOCK_VOLUME, new ObjectOpenHashSet<>()));
        consumedTanks = Lists.newArrayList(new Tank(INGOT_BLOCK_VOLUME, new ObjectOpenHashSet<>()));

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
        return new SaltFissionVesselMenu(containerId, playerInventory, this);
    }

    @Override
    public Component getDisplayName() {
        return getTileBlockDisplayName();
    }

    @Override
    public boolean canOpenGui(Level world, BlockPos position, BlockState state) {
        return true;
    }

    // IFissionFuelComponent

    @Override
    public @javax.annotation.Nullable FissionFuelBunch getFuelBunch() {
        return fuelBunch;
    }

    @Override
    public void setFuelBunch(@javax.annotation.Nullable FissionFuelBunch fuelBunch) {
        this.fuelBunch = fuelBunch;
        if (fuelBunch != null) {
            fuelBunch.fuelComponentMap.put(worldPosition.asLong(), this);
        }
    }

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
        fuelBunch.sources = fuelBunch.flux = cachedFlux = 0L;
        fluxSearched = false;
        heatMult = 0L;
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

        IFissionFuelBunchComponent.super.clusterSearch(id, clusterSearchCache, componentFailCache, assumedValidCache, simulate);
    }

    @Override
    public boolean isProducingFlux(boolean simulate) {
        return isPrimed(simulate) || isRunning(simulate);
    }

    @Override
    public void tryPriming(FissionReactor sourceReactor, boolean fromSource, boolean simulate) {
        if (getMultiblockController().orElse(null) != sourceReactor) {
            return;
        }

        if (canProcessInputs) {
            fuelBunch.tryPriming(fromSource, simulate);
        }
    }

    @Override
    public boolean isPrimed(boolean simulate) {
        return fuelBunch.primed;
    }

    @Override
    public void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache) {
        primedCache.addAll(fuelBunch.fuelComponentMap.values());
    }

    @Override
    public void unprime(boolean simulate) {
        fuelBunch.primed = false;
    }

    @Override
    public boolean isAcceptingFlux(Direction side, boolean simulate) {
        return canProcessInputs;
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
    public boolean isFluxSearched() {
        return fluxSearched;
    }

    @Override
    public void setFluxSearched(boolean fluxSearched) {
        this.fluxSearched = fluxSearched;
    }

    @Override
    public void addToFluxSearchCache(final ObjectSet<IFissionFuelComponent> fluxSearchCache) {
        fluxSearchCache.addAll(fuelBunch.fuelComponentMap.values());
    }

    @Override
    public void onEndModeratorLine(boolean simulate) {
        for (IFissionFuelBunchComponent fuelComponent : fuelBunch.fuelComponentMap.values()) {
            fuelComponent.refreshIsProcessing(false, simulate);
        }
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
        return fuelBunch.flux;
    }

    @Override
    public void addFlux(long addedFlux) {
        cachedFlux += addedFlux;
        fuelBunch.flux += addedFlux;
    }

    @Override
    public long getIndividualFlux() {
        return cachedFlux;
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

    /**
     * DON'T USE IN REACTOR LOGIC!
     */
    @Override
    public long getRawHeating(boolean simulate) {
        return isRunning(simulate) ? baseProcessHeat * fuelBunch.getHeatMultiplier(simulate) / getFuelBunchSize() : 0L;
    }

    /**
     * DON'T USE IN REACTOR LOGIC!
     */
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

    /**
     * DON'T USE IN REACTOR LOGIC!
     */
    @Override
    public long getHeatMultiplier(boolean simulate) {
        return fuelBunch.getHeatMultiplier(simulate) / getFuelBunchSize();
    }

    @Override
    public long getIndividualHeatMultiplier(boolean simulate) {
        return heatMult;
    }

    @Override
    public double getFluxEfficiencyFactor() {
        return fuelBunch.getFluxEfficiencyFactor(getFloatingPointCriticality());
    }

    @Override
    public double getEfficiency(boolean simulate) {
        return isRunning(simulate) ? fuelBunch.getHeatMultiplier(simulate) * baseProcessEfficiency * getSourceEfficiency() * getModeratorEfficiencyFactor() * getFluxEfficiencyFactor() / getFuelBunchSize() : 0D;
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
//        IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos)); TODO ADD
//        if (chunkSource != null) {
//            RadiationHelper.addToSourceRadiation(chunkSource, 8D * baseProcessRadiation * getSpeedMultiplier() * fission_meltdown_radiation_multiplier);
//        }
//
        componentIterator.remove();
        level.removeBlockEntity(worldPosition);

        BlockState corium = CUSTOM_FLUID_MAP.get("corium").block.get().defaultBlockState();
        level.setBlockAndUpdate(worldPosition, corium);

        Optional<FissionReactor> multiblock = getMultiblockController();
        if (multiblock.isPresent()) {
            for (Direction dir : Direction.values()) {
                BlockPos offPos = worldPosition.relative(dir);
                if (multiblock.get().rand.nextDouble() < 0.75D) {
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
        masterPort = multiblock.map(fissionReactor -> fissionReactor.getPartMap(FissionVesselPortEntity.class).get(masterPortPos.asLong())).orElse(null);
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

            updateDecayFractions();

            if (shouldRefresh) {
                reactor.get().refreshFlag = true;
            }
        }
    }

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
    public ProcessorMenuInfoImpl.BasicProcessorMenuInfo<SaltFissionVesselEntity, SaltFissionVesselUpdatePacket> getContainerInfo() {
        return info;
    }

    @Override
    public BasicRecipeHandler<SaltFissionRecipe> getRecipeHandler() {
        return NCRecipes.salt_fission;
    }

    @Override
    public RecipeInfo<SaltFissionRecipe> getRecipeInfo() {
        return recipeInfo;
    }

    @Override
    public void setRecipeInfo(RecipeInfo<? extends BasicRecipe> recipeInfo) {
        this.recipeInfo = (RecipeInfo<SaltFissionRecipe>) recipeInfo;
    }

    @Override
    public void setRecipeStats(@javax.annotation.Nullable BasicRecipe basic) {
        if (basic instanceof SaltFissionRecipe recipe) {
            decayProcessHeat = baseProcessHeat;
            baseProcessDecayFactor = recipe.getFissionFuelDecayFactor();
            baseProcessTime = recipe.getSaltFissionFuelTime();
            baseProcessHeat = recipe.getFissionFuelHeat();
            baseProcessEfficiency = recipe.getFissionFuelEfficiency();
            baseProcessCriticality = recipe.getFissionFuelCriticality();
            selfPriming = recipe.getFissionFuelSelfPriming();
            baseProcessRadiation = recipe.getFissionFuelRadiation();
        } else {
            baseProcessTime = 1D;
            baseProcessHeat = 0;
            baseProcessEfficiency = 0D;
            baseProcessCriticality = 1;
            selfPriming = false;
            baseProcessRadiation = 0D;
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
        return 1D / undercoolingLifetimeFactor;
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
        return readyToProcess(checkCluster) && hasEnoughFlux();
    }

    @Override
    public boolean readyToProcess() {
        return readyToProcess(true);
    }

    public boolean readyToProcess(boolean checkCluster) {
        return canProcessInputs && hasConsumed && isMachineAssembled() && (!checkCluster || cluster != null);
    }

    public boolean hasEnoughFlux() {
        return fuelBunch != null && fuelBunch.flux >= fuelBunch.getCriticalityFactor(getCriticality());
    }

    @Override
    public void process() {
//        getRadiationSource().setRadiationLevel(baseProcessRadiation * getSpeedMultiplier()); TODO
        IBasicProcessor.super.process();
    }

    @Override
    public void finishProcess() {
        double oldProcessTime = baseProcessTime, oldProcessEfficiency = baseProcessEfficiency, oldProcessDecayFactor = baseProcessDecayFactor;
        int oldProcessHeat = baseProcessHeat;
        long oldCriticality = getCriticality();
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
                if (oldProcessHeat != baseProcessHeat || oldProcessEfficiency != baseProcessEfficiency || oldProcessDecayFactor != baseProcessDecayFactor || oldCriticality != getCriticality()) {
                    if (!hasEnoughFlux()) {
                        multiblock.get().refreshFlag = true;
                    } else {
                        multiblock.get().addClusterToRefresh(cluster);
                    }
                }
            } else {
                sourceEfficiency = null;
                multiblock.get().refreshFlag = true;
            }
        }
    }

    // ITileInventory

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
        for (Tank tank : consumedTanks) {
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
    public SaltFissionVesselUpdatePacket getTileUpdatePacket() {
        return new SaltFissionVesselUpdatePacket(worldPosition, isProcessing, time, baseProcessTime, getTanks(), masterPortPos, getFilterTanks(), cluster);
    }

    @Override
    public void onTileUpdatePacket(SaltFissionVesselUpdatePacket message) {
        IBasicProcessor.super.onTileUpdatePacket(message);
        if (DEFAULT_NON.equals(masterPortPos = message.masterPortPos) ^ masterPort == null) {
            refreshMasterPort();
        }
        TankInfo.readInfoList(message.filterTankInfos, getFilterTanks());
        clusterHeatStored = message.clusterHeatStored;
        clusterHeatCapacity = message.clusterHeatCapacity;
    }

    // NBT

    @Override
    public CompoundTag writeAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.writeAll(nbt, registries);
        writeTanks(nbt, registries);

        writeProcessorNBT(nbt, registries);

        nbt.putDouble("baseProcessTime", baseProcessTime);
        nbt.putInt("baseProcessHeat", baseProcessHeat);
        nbt.putDouble("baseProcessEfficiency", baseProcessEfficiency);
        nbt.putInt("baseProcessCriticality", baseProcessCriticality);
        nbt.putDouble("baseProcessDecayFactor", baseProcessDecayFactor);
        nbt.putBoolean("selfPriming", selfPriming);

        nbt.putDouble("decayProcessHeat", decayProcessHeat);
        nbt.putDouble("decayHeatFraction", decayHeatFraction);
        nbt.putDouble("iodineFraction", iodineFraction);
        nbt.putDouble("poisonFraction", poisonFraction);

        nbt.putLong("cachedFlux", cachedFlux);
        nbt.putLong("clusterHeat", heat);

        nbt.putBoolean("isRunningSimulated", isRunningSimulated);
        return nbt;
    }

    @Override
    public void readAll(CompoundTag nbt, HolderLookup.Provider registries) {
        super.readAll(nbt, registries);
        readTanks(nbt, registries);

        readProcessorNBT(nbt, registries);

        baseProcessTime = nbt.getDouble("baseProcessTime");
        baseProcessHeat = nbt.getInt("baseProcessHeat");
        baseProcessEfficiency = nbt.getDouble("baseProcessEfficiency");
        baseProcessCriticality = nbt.getInt("baseProcessCriticality");
        baseProcessDecayFactor = nbt.getDouble("baseProcessDecayFactor");
        selfPriming = nbt.getBoolean("selfPriming");

        decayProcessHeat = nbt.getDouble("decayProcessHeat");
        decayHeatFraction = nbt.getDouble("decayHeatFraction");
        iodineFraction = nbt.getDouble("iodineFraction");
        poisonFraction = nbt.getDouble("poisonFraction");

        cachedFlux = nbt.getLong("cachedFlux");
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
        for (int i = 0; i < consumedTanks.size(); ++i) {
            consumedTanks.get(i).writeToNBT(nbt, registries, "consumedTanks" + i);
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
        for (int i = 0; i < consumedTanks.size(); ++i) {
            consumedTanks.get(i).readFromNBT(nbt, registries, "consumedTanks" + i);
        }
    }

    // ComputerCraft

    @Override
    public String getCCKey() {
        return "vessel";
    }

    @Override
    public Object getCCInfo() {
        Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
        List<Tank> tanks = getTanks();
        entry.put("fuel", CCHelper.tankInfo(tanks.get(0)));
        entry.put("depleted_fuel", CCHelper.tankInfo(tanks.get(1)));
        entry.put("effective_heating", getEffectiveHeating(false));
        entry.put("heat_multiplier", getHeatMultiplier(false));
        entry.put("is_processing", getIsProcessing());
        entry.put("current_time", getCurrentTime());
        entry.put("base_process_time", getBaseProcessTime());
        entry.put("base_process_criticality", baseProcessCriticality);
        entry.put("base_process_efficiency", baseProcessEfficiency);
        entry.put("is_primed", isPrimed(false));
        entry.put("efficiency", getEfficiency(false));
        entry.put("flux", getFlux());
        return entry;
    }
}