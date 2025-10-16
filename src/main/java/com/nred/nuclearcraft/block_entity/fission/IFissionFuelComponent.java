package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.fission.FissionModeratorRecipe;
import com.nred.nuclearcraft.recipe.fission.FissionReflectorRecipe;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.config.Config2.fission_neutron_reach;


public interface IFissionFuelComponent extends IFissionFluxSink, IFissionHeatingComponent {

    void tryPriming(FissionReactor sourceReactor, boolean fromSource, boolean simulate);

    boolean isPrimed(boolean simulate);

    void addToPrimedCache(final ObjectSet<IFissionFuelComponent> primedCache);

    void unprime(boolean simulate);

    @Override
    default boolean isNullifyingSources(Direction side, boolean simulate) {
        return false;
    }

    default boolean isProducingFlux(boolean simulate) {
        return isPrimed(simulate) || isFunctional(simulate);
    }

    boolean isFluxSearched();

    void setFluxSearched(boolean fluxSearched);

    void addToFluxSearchCache(final ObjectSet<IFissionFuelComponent> fluxSearchCache);

    void incrementHeatMultiplier();

    double getSourceEfficiency();

    void setSourceEfficiency(double sourceEfficiency, boolean maximize);

    long[] getModeratorLineFluxes();

    Double[] getModeratorLineEfficiencies();

    IFissionFluxSink[] getAdjacentFluxSinks();

    LongSet[] getPassiveModeratorCaches();

    LongSet[] getActiveModeratorCaches();

    ModeratorLine[] getModeratorLineCaches();

    LongSet[] getPassiveReflectorModeratorCaches();

    LongSet[] getActiveReflectorModeratorCaches();

    LongSet getActiveReflectorCache();

    long getBaseProcessHeat();

    double getBaseProcessEfficiency();

    long getHeatMultiplier(boolean simulate);

    default double getModeratorEfficiencyFactor() {
        int count = 0;
        double efficiency = 0D;
        for (Direction dir : Direction.values()) {
            int index = dir.ordinal();
            Double lineEfficiency = getModeratorLineEfficiencies()[index];
            if (lineEfficiency != null) {
                ++count;
                efficiency += getModeratorLineFluxes()[index] == 0 ? 0D : lineEfficiency;
            }
        }
        return count == 0 ? 0D : efficiency / count;
    }

    double getFluxEfficiencyFactor();

    @Override
    default double moderatorLineEfficiencyFactor() {
        return 1D;
    }

    @Override
    default boolean canSupportActiveModerator(boolean activeModeratorPos, boolean simulate) {
        return activeModeratorPos;
    }

    double getEfficiency(boolean simulate);

    double getEfficiencyIgnoreCoolingPenalty(boolean simulate);

    void setUndercoolingLifetimeFactor(double undercoolingLifetimeFactor);

    long getCriticality();

    double getFloatingPointCriticality();

    boolean isRunning(boolean simulate);

    int getDecayHeating();

    double getFloatingPointDecayHeating();

    boolean isInitiallyPrimed(boolean simulate);

    default void fluxSearch(final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (!isFluxSearched() && isProducingFlux(simulate)) {
            setFluxSearched(true);
        } else {
            return;
        }

        getLogic().distributeFluxFromFuelComponent(this, fluxSearchCache, componentFailCache, assumedValidCache, simulate);
    }

    default void defaultDistributeFlux(final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        dirLoop:
        for (Direction dir : Direction.values()) {
            BlockPos offPos = getWorldPosition().relative(dir);
            ModeratorBlockInfo activeInfo = componentFailCache.containsKey(offPos.asLong()) ? null : getModeratorBlockInfo(offPos, dir, canSupportActiveModerator(true, simulate));

            if (activeInfo != null && !activeInfo.blockingFlux) {
                int index = dir.ordinal(), oppositeIndex = dir.getOpposite().ordinal();

                final ModeratorLine line = new ModeratorLine(new ArrayList<>(), this);
                line.info.add(activeInfo);

                line.flux = activeInfo.fluxFactor;
                double moderatorEfficiency = activeInfo.efficiency;

                for (int i = 2; i <= fission_neutron_reach + 1; ++i) {
                    offPos = getWorldPosition().relative(dir, i);
                    ModeratorBlockInfo info = componentFailCache.containsKey(offPos.asLong()) ? null : getModeratorBlockInfo(offPos, dir, canSupportActiveModerator(false, simulate));
                    if (info != null) {
                        if (info.blockingFlux) {
                            continue dirLoop;
                        }
                        line.info.add(info);

                        line.flux += info.fluxFactor;
                        moderatorEfficiency += info.efficiency;
                    } else {
                        IFissionFuelComponent fuelComponent = getLogic().getNextFuelComponent(this, offPos);
                        if (fuelComponent != null) {
                            line.fluxSink = fuelComponent;
                            fuelComponent.addFlux(line.flux);
                            fuelComponent.getModeratorLineFluxes()[oppositeIndex] = line.flux;
                            fuelComponent.getModeratorLineEfficiencies()[oppositeIndex] = moderatorEfficiency / (i - 1);
                            fuelComponent.incrementHeatMultiplier();

                            updateModeratorLine(fuelComponent, dir, line, componentFailCache, assumedValidCache, simulate);

                            fuelComponent.addToFluxSearchCache(fluxSearchCache);
                        } else {
                            IFissionComponent component = getMultiblockController().get().getPartMap(IFissionComponent.class).get(offPos.asLong());
                            if (component instanceof IFissionFluxSink fluxSink) {
                                if (fluxSink.isAcceptingFlux(dir.getOpposite(), simulate)) {
                                    line.fluxSink = fluxSink;
                                    fluxSink.addFlux(line.flux);
                                    getModeratorLineFluxes()[index] = line.flux;
                                    getModeratorLineEfficiencies()[index] = fluxSink.moderatorLineEfficiencyFactor() * moderatorEfficiency / (i - 1);
                                    incrementHeatMultiplier();

                                    updateModeratorLine(fluxSink, dir, line, componentFailCache, assumedValidCache, simulate);
                                }
                            } else if (i - 1 <= fission_neutron_reach / 2) {
                                FissionReflectorRecipe recipe = (FissionReflectorRecipe) RecipeHelper.blockRecipe(NCRecipes.fission_reflector, getTileWorld(), offPos);
                                if (recipe != null) {
                                    line.reflectorRecipe = recipe;
                                    line.flux = (long) Math.floor(2D * line.flux * recipe.getFissionReflectorReflectivity());
                                    addFlux(line.flux);
                                    getModeratorLineFluxes()[index] = line.flux;
                                    getModeratorLineEfficiencies()[index] = recipe.getFissionReflectorEfficiency() * moderatorEfficiency / (i - 1);
                                    incrementHeatMultiplier();

                                    if (isFunctional(simulate)) {
                                        onModeratorLineComplete(line, dir);

                                        addToModeratorCache(line, getMultiblockController().get().activeModeratorCache, getMultiblockController().get().passiveModeratorCache, componentFailCache, assumedValidCache);
                                        getMultiblockController().get().activeReflectorCache.add(offPos.asLong());
                                    } else {
                                        getModeratorLineCaches()[index] = line;
                                        addToModeratorCache(line, dir, getActiveReflectorModeratorCaches(), getPassiveReflectorModeratorCaches(), componentFailCache, assumedValidCache);
                                        getActiveReflectorCache().add(offPos.asLong());
                                    }
                                }
                            }
                        }
                        continue dirLoop;
                    }
                }
            }
        }
    }

    class ModeratorLine {
        public final List<ModeratorBlockInfo> info;
        public final IFissionFuelComponent fuelComponent;
        public IFissionFluxSink fluxSink = null;
        public FissionReflectorRecipe reflectorRecipe = null;
        public long flux = 0;

        public ModeratorLine(List<ModeratorBlockInfo> info, IFissionFuelComponent fuelComponent) {
            this.info = new ArrayList<>(info);
            this.fuelComponent = fuelComponent;
        }

        public boolean hasValidEndpoint(boolean simulate) {
            return fluxSink != null && fluxSink.isFunctional(simulate) || reflectorRecipe != null;
        }
    }

    default ModeratorBlockInfo getModeratorBlockInfo(BlockPos pos, Direction dir, boolean validActiveModeratorPos) {
        IFissionComponent component = getMultiblockController().get().getPartMap(IFissionComponent.class).get(pos.asLong());
        if (component != null) {
            return component.getModeratorBlockInfo(dir, validActiveModeratorPos);
        }

        FissionModeratorRecipe recipe = (FissionModeratorRecipe) RecipeHelper.blockRecipe(NCRecipes.fission_moderator, getTileWorld(), pos);
        if (recipe != null) {
            return new ModeratorBlockInfo(pos, null, false, validActiveModeratorPos, recipe.getFissionModeratorFluxFactor(), recipe.getFissionModeratorEfficiency());
        }

        return null;
    }

    class ModeratorBlockInfo {
        public final long posLong;
        public final IFissionComponent component;
        public final boolean blockingFlux;
        public boolean validActiveModeratorPos;
        public final long fluxFactor;
        public final double efficiency;

        public ModeratorBlockInfo(BlockPos pos, IFissionComponent component, boolean blockingFlux, boolean validActiveModeratorPos, long fluxFactor, double efficiency) {
            posLong = pos.asLong();
            this.component = component;
            this.blockingFlux = blockingFlux;
            this.validActiveModeratorPos = validActiveModeratorPos;
            this.fluxFactor = fluxFactor;
            this.efficiency = efficiency;
        }

        public void updateModeratorPosValidity(boolean validActiveModeratorPosIn) {
            validActiveModeratorPos |= validActiveModeratorPosIn;
        }
    }

    /**
     * Adds to the full reactor cache, not the local cache!
     */
    static void addToModeratorCache(ModeratorLine line, LongSet activeCache, LongSet passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
        for (ModeratorBlockInfo info : line.info) {
            addToModeratorCache(info, activeCache, passiveCache, componentFailCache, assumedValidCache);
        }
    }

    /**
     * Adds to the full reactor cache, not the local cache!
     */
    static void addToModeratorCache(ModeratorBlockInfo info, LongSet activeCache, LongSet passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
        if (info.component != null && !componentFailCache.containsKey(info.posLong)) {
            assumedValidCache.put(info.posLong, info.component);
            info.component.onAddedToModeratorCache(info);
        } else {
            (info.validActiveModeratorPos ? activeCache : passiveCache).add(info.posLong);
        }
    }

    /**
     * Adds to local cache, not the reactor cache!
     */
    static void addToModeratorCache(ModeratorLine line, Direction dir, LongSet[] activeCache, LongSet[] passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
        for (ModeratorBlockInfo info : line.info) {
            addToModeratorCache(info, dir, activeCache, passiveCache, componentFailCache, assumedValidCache);
        }
    }

    /**
     * Adds to local cache, not the reactor cache!
     */
    static void addToModeratorCache(ModeratorBlockInfo info, Direction dir, LongSet[] activeCache, LongSet[] passiveCache, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache) {
        if (info.component != null && !componentFailCache.containsKey(info.posLong)) {
            assumedValidCache.put(info.posLong, info.component);
            info.component.onAddedToModeratorCache(info);
        } else {
            if (info.validActiveModeratorPos) {
                activeCache[dir.ordinal()].add(info.posLong);
            } else {
                passiveCache[dir.ordinal()].add(info.posLong);
            }
        }
    }

    static void onModeratorLineComplete(ModeratorLine line, Direction dir) {
        for (ModeratorBlockInfo info : line.info) {
            if (info.component != null) {
                info.component.onModeratorLineComplete(line, info, dir);
            }
        }
    }

    default void updateModeratorLine(IFissionFluxSink fluxSink, Direction dir, ModeratorLine line, final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        fluxSink.onEndModeratorLine(simulate);
        line.info.get(line.info.size() - 1).updateModeratorPosValidity(fluxSink.canSupportActiveModerator(true, simulate));

        int index = dir.ordinal();
        if (isFunctional(simulate) && fluxSink.isFunctional(simulate)) {
            onModeratorLineComplete(line, dir);
            addToModeratorCache(line, getMultiblockController().get().activeModeratorCache, getMultiblockController().get().passiveModeratorCache, componentFailCache, assumedValidCache);
        } else {
            getModeratorLineCaches()[index] = line;
            addToModeratorCache(line, dir, getActiveModeratorCaches(), getPassiveModeratorCaches(), componentFailCache, assumedValidCache);
        }
        getAdjacentFluxSinks()[index] = fluxSink;
    }

    default void defaultRefreshLocal(boolean simulate) {
        if (isFunctional(simulate)) {
            for (Direction dir : Direction.values()) {
                int index = dir.ordinal();
                ModeratorLine line = getModeratorLineCaches()[index];
                if (line != null && line.hasValidEndpoint(simulate)) {
                    onModeratorLineComplete(line, dir);
                    getMultiblockController().get().passiveModeratorCache.addAll(getPassiveModeratorCaches()[index]);
                    getMultiblockController().get().activeModeratorCache.addAll(getActiveModeratorCaches()[index]);
                }
                getMultiblockController().get().passiveModeratorCache.addAll(getPassiveReflectorModeratorCaches()[index]);
                getMultiblockController().get().activeModeratorCache.addAll(getActiveReflectorModeratorCaches()[index]);
                getMultiblockController().get().activeReflectorCache.addAll(getActiveReflectorCache());
            }
        }
    }

    /**
     * Fix to force adjacent moderators to be active
     */
    default void defaultRefreshModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        if (isFunctional(simulate)) {
            defaultRefreshAdjacentActiveModerators(componentFailCache, assumedValidCache, simulate);
        }
    }

    default void defaultRefreshAdjacentActiveModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        FissionReactor multiblock = getMultiblockController().get();
        for (Direction dir : Direction.values()) {
            IFissionFluxSink fluxSink = getAdjacentFluxSinks()[dir.ordinal()];
            if (fluxSink != null && fluxSink.isFunctional(simulate)) {
                BlockPos adjPos = getWorldPosition().relative(dir);
                if (getMultiblockController().get().passiveModeratorCache.contains(adjPos.asLong())) {
                    addToModeratorCache(getModeratorBlockInfo(adjPos, dir, canSupportActiveModerator(true, simulate)), multiblock.activeModeratorCache, multiblock.passiveModeratorCache, componentFailCache, assumedValidCache);
                }
            }
        }
    }

    default void defaultForceAdjacentActiveModerators(final Long2ObjectMap<IFissionComponent> componentFailCache, final Long2ObjectMap<IFissionComponent> assumedValidCache, boolean simulate) {
        FissionReactor multiblock = getMultiblockController().get();
        for (Direction dir : Direction.values()) {
            ModeratorBlockInfo info = getModeratorBlockInfo(getWorldPosition().relative(dir), dir, true);
            if (info != null) {
                addToModeratorCache(info, multiblock.activeModeratorCache, multiblock.passiveModeratorCache, componentFailCache, assumedValidCache);
            }
        }
    }
}