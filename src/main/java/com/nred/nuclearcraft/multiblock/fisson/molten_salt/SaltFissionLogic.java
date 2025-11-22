package com.nred.nuclearcraft.multiblock.fisson.molten_salt;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.FissionHeaterPortEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionVesselPortEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionFuelBunch;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.SaltFissionUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionEmergencyCoolingRecipe;
import com.nred.nuclearcraft.util.NCMath;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity.SyncReason;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.fission_overheat;
import static com.nred.nuclearcraft.config.NCConfig.fission_sparsity_penalty_params;

public class SaltFissionLogic extends FissionReactorLogic {
    public final List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_emergency_cooling.validFluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));

    public RecipeInfo<FissionEmergencyCoolingRecipe> emergencyCoolingRecipeInfo;

    public int heaterCount = 0;
    public double meanHeatingSpeedMultiplier = 0D, totalHeatingSpeedMultiplier = 0D;

    public SaltFissionLogic(FissionReactorLogic oldLogic) {
        super(oldLogic.multiblock);
        if (oldLogic instanceof SaltFissionLogic oldMoltenSaltLogic) {
            heaterCount = oldMoltenSaltLogic.heaterCount;
            meanHeatingSpeedMultiplier = oldMoltenSaltLogic.meanHeatingSpeedMultiplier;
            totalHeatingSpeedMultiplier = oldMoltenSaltLogic.totalHeatingSpeedMultiplier;
        }
    }

    @Override
    public String getID() {
        return "molten_salt";
    }

    @Override
    public void onResetStats() {
        heaterCount = 0;
        meanHeatingSpeedMultiplier = totalHeatingSpeedMultiplier = 0D;
    }

    @Override
    public void onReactorFormed() {
        tanks.get(0).setCapacity(FissionReactor.BASE_TANK_CAPACITY * getCapacityMultiplier());
        tanks.get(1).setCapacity(FissionReactor.BASE_TANK_CAPACITY * getCapacityMultiplier());

        super.onReactorFormed();
    }

    @Override
    public boolean isMachineWhole() {
        return !containsBlacklistedPart();
    }

    public static final List<Pair<Class<? extends IMultiblockPart<FissionReactor>>, String>> MOLTEN_SALT_PART_BLACKLIST = Lists.newArrayList(Pair.of(SolidFissionCellEntity.class, MODID + ".multiblock_validation.fission_reactor.prohibit_cells"), Pair.of(SolidFissionHeatSinkEntity.class, MODID + ".multiblock_validation.fission_reactor.prohibit_sinks"));

    @Override
    public List<Pair<Class<? extends IMultiblockPart<FissionReactor>>, String>> getPartBlacklist() {
        return MOLTEN_SALT_PART_BLACKLIST;
    }

    @Override
    public void refreshConnections() {
        super.refreshConnections();
        refreshFilteredPorts(FissionVesselPortEntity.class, SaltFissionVesselEntity.class);
        refreshFilteredPorts(FissionHeaterPortEntity.class, SaltFissionHeaterEntity.class);
        formFuelBunches(SaltFissionVesselEntity.class, (x, y) -> x.getFilterKey().equals(y.getFilterKey()));
    }

    @Override
    public void refreshAllFuelComponentModerators(boolean simulate) {
        for (SaltFissionVesselEntity vessel : multiblock.getParts(SaltFissionVesselEntity.class)) {
            refreshFuelComponentModerators(vessel, componentFailCache, assumedValidCache, simulate);
        }
    }

    @Override
    public void incrementClusterStatsFromComponents(FissionCluster cluster, boolean simulate) {
        for (FissionFuelBunch bunch : fuelBunches) {
            bunch.statsRetrieved = false;
        }

        for (IFissionComponent component : cluster.getComponentMap().values()) {
            if (component.isFunctional(simulate)) {
                ++cluster.componentCount;
                if (component instanceof IFissionHeatingComponent heatingComponent) {
                    if (component instanceof SaltFissionVesselEntity vessel) {
                        FissionFuelBunch fuelBunch = vessel.getFuelBunch();
                        ++cluster.fuelComponentCount;
                        if (fuelBunch != null && !fuelBunch.statsRetrieved) {
                            fuelBunch.statsRetrieved = true;
                            cluster.rawHeating += fuelBunch.getRawHeating(simulate);
                            cluster.rawHeatingIgnoreCoolingPenalty += fuelBunch.getRawHeatingIgnoreCoolingPenalty(simulate);
                            cluster.effectiveHeating += fuelBunch.getEffectiveHeating(simulate);
                            cluster.effectiveHeatingIgnoreCoolingPenalty += fuelBunch.getEffectiveHeatingIgnoreCoolingPenalty(simulate);
                            cluster.totalHeatMult += fuelBunch.getHeatMultiplier(simulate);
                            cluster.totalEfficiency += fuelBunch.getEfficiency(simulate);
                            cluster.totalEfficiencyIgnoreCoolingPenalty += fuelBunch.getEfficiencyIgnoreCoolingPenalty(simulate);
                        }
                    } else {
                        cluster.rawHeating += heatingComponent.getRawHeating(simulate);
                        cluster.rawHeatingIgnoreCoolingPenalty += heatingComponent.getRawHeatingIgnoreCoolingPenalty(simulate);
                        cluster.effectiveHeating += heatingComponent.getEffectiveHeating(simulate);
                        cluster.effectiveHeatingIgnoreCoolingPenalty += heatingComponent.getEffectiveHeatingIgnoreCoolingPenalty(simulate);
                    }
                }
                if (component instanceof IFissionCoolingComponent coolingComponent) {
                    cluster.cooling += coolingComponent.getCooling(simulate);
                }
            }
        }
    }

    @Override
    public void refreshReactorStats(boolean simulate) {
        super.refreshReactorStats(simulate);

        for (FissionCluster cluster : getClusterMap().values()) {
            multiblock.usefulPartCount += cluster.componentCount;
            multiblock.fuelComponentCount += cluster.fuelComponentCount;
            multiblock.cooling += cluster.cooling;
            multiblock.rawHeating += cluster.rawHeating;
            // effectiveHeating += cluster.effectiveHeating;
            multiblock.totalHeatMult += cluster.totalHeatMult;
            multiblock.totalEfficiency += cluster.totalEfficiency;
        }

        multiblock.usefulPartCount += multiblock.passiveModeratorCache.size() + multiblock.activeModeratorCache.size() + multiblock.activeReflectorCache.size();
        double usefulPartRatio = (double) multiblock.usefulPartCount / (double) multiblock.getInteriorVolume();
        multiblock.sparsityEfficiencyMult = usefulPartRatio >= fission_sparsity_penalty_params[1] ? 1D : (1D - fission_sparsity_penalty_params[0]) * Math.sin(usefulPartRatio * Math.PI / (2D * fission_sparsity_penalty_params[1])) + fission_sparsity_penalty_params[0];
        // effectiveHeating *= sparsityEfficiencyMult;
        multiblock.totalEfficiency *= multiblock.sparsityEfficiencyMult;
        multiblock.meanHeatMult = multiblock.fuelComponentCount == 0 ? 0D : (double) multiblock.totalHeatMult / (double) multiblock.fuelComponentCount;
        multiblock.meanEfficiency = multiblock.fuelComponentCount == 0 ? 0D : multiblock.totalEfficiency / multiblock.fuelComponentCount;

        for (FissionCluster cluster : getClusterMap().values()) {
            cluster.meanHeatingSpeedMultiplier = cluster.totalHeatingSpeedMultiplier = 0D;
            int clusterHeaters = 0;
            for (IFissionComponent component : cluster.getComponentMap().values()) {
                if (component instanceof SaltFissionHeaterEntity heater) {
                    heater.heatingSpeedMultiplier = cluster.meanEfficiency * multiblock.sparsityEfficiencyMult * (cluster.rawHeating >= cluster.cooling ? 1D : (double) cluster.rawHeating / (double) cluster.cooling);
                    cluster.totalHeatingSpeedMultiplier += heater.heatingSpeedMultiplier;
                    ++clusterHeaters;
                }
            }
            cluster.meanHeatingSpeedMultiplier = clusterHeaters == 0 ? 0D : cluster.totalHeatingSpeedMultiplier / clusterHeaters;
            totalHeatingSpeedMultiplier += cluster.meanHeatingSpeedMultiplier;
        }
        meanHeatingSpeedMultiplier = getClusterMap().isEmpty() ? 0D : totalHeatingSpeedMultiplier / getClusterMap().size();
    }

    // Server

    @Override
    public boolean onUpdateServer() {
        if (!multiblock.isSimulation) {
            if (heatBuffer.isFull() && fission_overheat) {
                heatBuffer.setHeatStored(0L);
                casingMeltdown();
                return true;
            }

            for (FissionCluster cluster : getClusterMap().values()) {
                long netHeating = cluster.getNetHeating();
                if (netHeating > 0 && cluster.connectedToWall) {
                    heatBuffer.changeHeatStored(netHeating);
                } else {
                    cluster.heatBuffer.changeHeatStored(netHeating);
                }

                if (cluster.heatBuffer.isFull() && fission_overheat) {
                    cluster.heatBuffer.setHeatStored(0L);
                    clusterMeltdown(cluster);
                    return true;
                }
            }
        }

        updateEmergencyCooling();

        updateSounds();

        return super.onUpdateServer();
    }

    public void updateEmergencyCooling() {
        if (!multiblock.isReactorOn && !heatBuffer.isEmpty()) {
            refreshRecipe();
            if (canProcessInputs()) {
                produceProducts();
            }
        }
    }

    public void updateSounds() {
        if (multiblock.isReactorOn) {
            playFuelComponentSounds(SaltFissionVesselEntity.class);
        }
    }

    public void refreshRecipe() {
        emergencyCoolingRecipeInfo = NCRecipes.fission_emergency_cooling.getRecipeInfoFromInputs(getWorld(), Collections.emptyList(), tanks.subList(0, 1));
    }

    public boolean canProcessInputs() {
        if (!setRecipeStats()) {
            return false;
        }
        return canProduceProducts();
    }

    public boolean setRecipeStats() {
        if (emergencyCoolingRecipeInfo == null) {
            return false;
        }
        return true;
    }

    public boolean canProduceProducts() {
        BasicRecipe recipe = emergencyCoolingRecipeInfo.recipe;
        SizedChanceFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
        int productSize = fluidProduct.amount();
        if (productSize <= 0 || fluidProduct.ingredient().hasNoFluids()) {
            return false;
        }

        Tank outputTank = tanks.get(1);
        return outputTank.isEmpty() || fluidProduct.test(outputTank.getFluid());
    }

    public void produceProducts() {
        Tank inputTank = tanks.get(0), outputTank = tanks.get(1);

        FissionEmergencyCoolingRecipe recipe = emergencyCoolingRecipeInfo.recipe;
        int usedInput = NCMath.toInt(Math.min(inputTank.getFluidAmount() / recipe.getEmergencyCoolingHeatPerInputMB(), Math.min(heatBuffer.getHeatStored(), (long) FissionReactor.BASE_TANK_CAPACITY * getPartCount(FissionVentEntity.class))));

        inputTank.changeFluidAmount(-usedInput);
        if (inputTank.getFluidAmount() <= 0) {
            inputTank.setFluidStored(FluidStack.EMPTY);
        }

        SizedChanceFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
        if (fluidProduct.amount() > 0) {
            if (outputTank.isEmpty()) {
                outputTank.setFluidStored(fluidProduct.getStack());
                outputTank.setFluidAmount(usedInput);
            } else if (fluidProduct.test(outputTank.getFluid())) {
                outputTank.changeFluidAmount(usedInput);
            }
        }

        heatBuffer.changeHeatStored((long) (-usedInput * recipe.getEmergencyCoolingHeatPerInputMB()));
    }

    public long getNetClusterHeating() {
        return multiblock.rawHeating - multiblock.cooling;
    }

    @Override
    public void clusterMeltdown(FissionCluster cluster) {
        final Iterator<IFissionComponent> componentIterator = cluster.getComponentMap().values().iterator();
        while (componentIterator.hasNext()) {
            IFissionComponent component = componentIterator.next();
            component.onClusterMeltdown(componentIterator);
        }
        super.clusterMeltdown(cluster);
    }

    // Component Logic

    @Override
    public void distributeFluxFromFuelComponent(IFissionFuelComponent fuelComponent, final ObjectSet<IFissionFuelComponent> fluxSearchCache, final Long2ObjectMap<IFissionComponent> lineFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {
        fuelComponent.defaultDistributeFlux(fluxSearchCache, lineFailCache, assumedValidCache, simulate);
    }

    @Override
    public IFissionFuelComponent getNextFuelComponent(IFissionFuelComponent fuelComponent, BlockPos pos) {
        return multiblock.getPartMap(SaltFissionVesselEntity.class).get(pos.asLong());
    }

    @Override
    public void refreshFuelComponentLocal(IFissionFuelComponent fuelComponent, boolean simulate) {
        fuelComponent.defaultRefreshLocal(simulate);
    }

    @Override
    public void refreshFuelComponentModerators(IFissionFuelComponent fuelComponent, final Long2ObjectMap<IFissionComponent> currentComponentFailCache, final Long2ObjectMap<IFissionComponent> currentAssumedValidCache, boolean simulate) {
        fuelComponent.defaultRefreshModerators(componentFailCache, assumedValidCache, simulate);
    }

    @Override
    public boolean isShieldActiveModerator(FissionShieldEntity shield, boolean activeModeratorPos) {
        return super.isShieldActiveModerator(shield, activeModeratorPos);
    }

    @Override
    public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
        return multiblock.isAssembled() ? tanks : backupTanks;
    }

    // NBT

    @Override
    public void writeToLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        super.writeToLogicTag(data, registries, syncReason);
        writeTanks(tanks, data, registries, "tanks");
        data.putInt("heaterCount", heaterCount);
        data.putDouble("meanHeatingSpeedMultiplier", meanHeatingSpeedMultiplier);
        data.putDouble("totalHeatingSpeedMultiplier", totalHeatingSpeedMultiplier);
    }

    @Override
    public void readFromLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        super.readFromLogicTag(data, registries, syncReason);
        readTanks(tanks, data, registries, "tanks");
        heaterCount = data.getInt("heaterCount");
        meanHeatingSpeedMultiplier = data.getDouble("meanHeatingSpeedMultiplier");
        totalHeatingSpeedMultiplier = data.getDouble("totalHeatingSpeedMultiplier");
    }

    // Packets

    @Override
    public SaltFissionUpdatePacket getMultiblockUpdatePacket() {
        return new SaltFissionUpdatePacket(multiblock.controller.getTilePos(), multiblock.isReactorOn, multiblock.heatBuffer, multiblock.clusterCount, multiblock.cooling, multiblock.rawHeating, multiblock.totalHeatMult, multiblock.meanHeatMult, multiblock.fuelComponentCount, multiblock.usefulPartCount, multiblock.totalEfficiency, multiblock.meanEfficiency, multiblock.sparsityEfficiencyMult, meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier);
    }

    @Override
    public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
        super.onMultiblockUpdatePacket(message);
        if (message instanceof SaltFissionUpdatePacket packet) {
            meanHeatingSpeedMultiplier = packet.meanHeatingSpeedMultiplier;
            totalHeatingSpeedMultiplier = packet.totalHeatingSpeedMultiplier;
        }
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
        super.clearAllMaterial();
        for (Tank tank : tanks) {
            tank.setFluidStored(FluidStack.EMPTY);
        }
    }
}