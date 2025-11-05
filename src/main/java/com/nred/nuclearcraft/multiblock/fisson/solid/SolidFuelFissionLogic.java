package com.nred.nuclearcraft.multiblock.fisson.solid;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.block_entity.fission.*;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCellPortEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.multiblock.fisson.FissionCluster;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.SolidFissionUpdatePacket;
import com.nred.nuclearcraft.recipe.RecipeInfo;
import com.nred.nuclearcraft.recipe.fission.FissionHeatingRecipe;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.fission_overheat;
import static com.nred.nuclearcraft.config.NCConfig.fission_sparsity_penalty_params;

public class SolidFuelFissionLogic extends FissionReactorLogic {
    public List<Tank> tanks = Lists.newArrayList(new Tank(FissionReactor.BASE_TANK_CAPACITY, NCRecipes.fission_heating.validFluids.get(0)), new Tank(FissionReactor.BASE_TANK_CAPACITY, null));

    public RecipeInfo<FissionHeatingRecipe> heatingRecipeInfo;

    public int heatingOutputRate = 0;
    public double effectiveHeating = 0D, reservedEffectiveHeat = 0D, heatingRecipeRate = 0D, heatingOutputRateFP = 0D;

    public SolidFuelFissionLogic(FissionReactorLogic oldLogic) {
        super(oldLogic.multiblock);
        if (oldLogic instanceof SolidFuelFissionLogic oldSolidFuelLogic) {
            heatingOutputRate = oldSolidFuelLogic.heatingOutputRate;
            effectiveHeating = oldSolidFuelLogic.effectiveHeating;
            reservedEffectiveHeat = oldSolidFuelLogic.reservedEffectiveHeat;
            heatingRecipeRate = oldSolidFuelLogic.heatingRecipeRate;
            heatingOutputRateFP = oldSolidFuelLogic.heatingOutputRateFP;
        }
    }

    @Override
    public String getID() {
        return "solid_fuel";
    }

    @Override
    public void onResetStats() {
        heatingOutputRate = 0;
        effectiveHeating = heatingRecipeRate = heatingOutputRateFP = 0D;
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

    public static final List<Pair<Class<? extends IMultiblockPart<FissionReactor>>, String>> SOLID_FUEL_PART_BLACKLIST = Lists.newArrayList(Pair.of(SaltFissionVesselEntity.class, NuclearcraftNeohaul.MODID + ".multiblock_validation.fission_reactor.prohibit_vessels"), Pair.of(SaltFissionHeaterEntity.class, NuclearcraftNeohaul.MODID + ".multiblock_validation.fission_reactor.prohibit_heaters"));

    @Override
    public List<Pair<Class<? extends IMultiblockPart<FissionReactor>>, String>> getPartBlacklist() {
        return SOLID_FUEL_PART_BLACKLIST;
    }

    @Override
    public void refreshConnections() {
        super.refreshConnections();
        refreshFilteredPorts(FissionCellPortEntity.class, SolidFissionCellEntity.class);
    }

    @Override
    public void refreshAllFuelComponentModerators(boolean simulate) {
        for (SolidFissionCellEntity cell : multiblock.getParts(SolidFissionCellEntity.class)) {
            refreshFuelComponentModerators(cell, componentFailCache, assumedValidCache, simulate);
        }
    }

    @Override
    public void refreshReactorStats(boolean simulate) {
        super.refreshReactorStats(simulate);

        for (FissionCluster cluster : multiblock.getClusterMap().values()) {
            if (cluster.connectedToWall) {
                multiblock.usefulPartCount += cluster.componentCount;
                multiblock.fuelComponentCount += cluster.fuelComponentCount;
                multiblock.cooling += cluster.cooling;
                multiblock.rawHeating += cluster.rawHeating;
                effectiveHeating += cluster.effectiveHeating;
                multiblock.totalHeatMult += cluster.totalHeatMult;
                multiblock.totalEfficiency += cluster.totalEfficiency;
            }
        }

        multiblock.usefulPartCount += multiblock.passiveModeratorCache.size() + multiblock.activeModeratorCache.size() + multiblock.activeReflectorCache.size();
        double usefulPartRatio = (double) multiblock.usefulPartCount / (double) multiblock.getInteriorVolume();
        multiblock.sparsityEfficiencyMult = usefulPartRatio >= fission_sparsity_penalty_params[1] ? 1D : (1D - fission_sparsity_penalty_params[0]) * Math.sin(usefulPartRatio * Math.PI / (2D * fission_sparsity_penalty_params[1])) + fission_sparsity_penalty_params[0];
        effectiveHeating *= multiblock.sparsityEfficiencyMult;
        multiblock.totalEfficiency *= multiblock.sparsityEfficiencyMult;
        multiblock.meanHeatMult = multiblock.fuelComponentCount == 0 ? 0D : (double) multiblock.totalHeatMult / (double) multiblock.fuelComponentCount;
        multiblock.meanEfficiency = multiblock.fuelComponentCount == 0 ? 0D : multiblock.totalEfficiency / multiblock.fuelComponentCount;
    }

    // Server

    @Override
    public boolean onUpdateServer() {
        if (!multiblock.isSimulation) {
            heatBuffer.changeHeatStored(multiblock.rawHeating);

            if (heatBuffer.isFull() && fission_overheat) {
                heatBuffer.setHeatStored(0L);
                reservedEffectiveHeat = 0D;
                casingMeltdown();
                return true;
            }

            for (FissionCluster cluster : multiblock.getClusterMap().values()) {
                cluster.heatBuffer.changeHeatStored(cluster.getNetHeating());
                if (cluster.heatBuffer.isFull() && fission_overheat) {
                    cluster.heatBuffer.setHeatStored(0L);
                    clusterMeltdown(cluster);
                    return true;
                }
            }
        }

        updateFluidHeating();

        updateSounds();

        return super.onUpdateServer();
    }

    public void updateFluidHeating() {
        if (multiblock.isReactorOn && getEffectiveHeat() > 0D) {
            refreshRecipe();
            if (canProcessInputs()) {
                produceProducts();
                return;
            }
        }

        heatingOutputRate = 0;
        heatingRecipeRate = heatingOutputRateFP = 0D;

        if (multiblock.isSimulation) {
            refreshRecipe();
            if (heatingRecipeInfo != null) {
                FissionHeatingRecipe recipe = heatingRecipeInfo.recipe;
                heatingOutputRateFP = recipe.getFluidProducts().get(0).amount() * effectiveHeating / recipe.getFissionHeatingHeatPerInputMB();
            }
        }
    }

    public void updateSounds() {
        if (multiblock.isReactorOn) {
            playFuelComponentSounds(SolidFissionCellEntity.class);
        }
    }

    public void refreshRecipe() {
        heatingRecipeInfo = NCRecipes.fission_heating.getRecipeInfoFromInputs(getWorld(), Collections.emptyList(), tanks.subList(0, 1));
    }

    public boolean canProcessInputs() {
        if (!setRecipeStats()) {
            return false;
        }
        return canProduceProducts();
    }

    public boolean setRecipeStats() {
        if (heatingRecipeInfo == null) {
            heatingOutputRate = 0;
            heatingRecipeRate = heatingOutputRateFP = 0D;
            return false;
        }
        return true;
    }

    public boolean canProduceProducts() {
        FissionHeatingRecipe recipe = heatingRecipeInfo.recipe;
        SizedChanceFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
        int productSize = fluidProduct.amount();
        if (productSize <= 0 || fluidProduct.ingredient().hasNoFluids()) {
            return false;
        }

        int heatPerMB = recipe.getFissionHeatingHeatPerInputMB();
        int inputSize = recipe.getFluidIngredients().get(0).amount();

        Tank inputTank = tanks.get(0), outputTank = tanks.get(1);

        double usedInput = Math.min(inputTank.getFluidAmount(), getEffectiveHeat() / heatPerMB);
        heatingRecipeRate = heatingOutputRateFP = NCMath.toInt(Math.min((double) (outputTank.getCapacity() - outputTank.getFluidAmount()) / productSize, usedInput / inputSize));
        reservedEffectiveHeat += (heatingRecipeRate - NCMath.toInt(heatingRecipeRate)) * inputSize * heatPerMB;

        int extraRecipeRate = NCMath.toInt(Math.min(Integer.MAX_VALUE - heatingRecipeRate, reservedEffectiveHeat / (heatPerMB * inputSize)));
        heatingRecipeRate += extraRecipeRate;
        reservedEffectiveHeat -= extraRecipeRate * inputSize * heatPerMB;

        return outputTank.isEmpty() || fluidProduct.test(outputTank.getFluid());
    }

    public void produceProducts() {
        FissionHeatingRecipe recipe = heatingRecipeInfo.recipe;
        int inputSize = recipe.getFluidIngredients().get(0).amount();
        int heatingRecipeRateInt = NCMath.toInt(heatingRecipeRate);

        Tank inputTank = tanks.get(0), outputTank = tanks.get(1);

        if (heatingRecipeRateInt * inputSize > 0) {
            inputTank.changeFluidAmount(-heatingRecipeRateInt * inputSize);
        }
        if (inputTank.getFluidAmount() <= 0) {
            inputTank.setFluidStored(FluidStack.EMPTY);
        }

        SizedChanceFluidIngredient fluidProduct = recipe.getFluidProducts().get(0);
        if (fluidProduct.amount() > 0) {
            int stackSize = 0;
            if (outputTank.isEmpty()) {
                outputTank.setFluidStored(Arrays.stream(fluidProduct.getFluids()).findFirst().orElse(FluidStack.EMPTY));
                stackSize = outputTank.getFluidAmount();
                heatingOutputRate = heatingRecipeRateInt * stackSize;
                outputTank.setFluidAmount(heatingOutputRate);
            } else if (fluidProduct.test(outputTank.getFluid())) {
                stackSize = fluidProduct.amount();
                heatingOutputRate = heatingRecipeRateInt * stackSize;
                outputTank.changeFluidAmount(heatingOutputRate);
            }
            heatingOutputRateFP *= stackSize;
            if (heatingOutputRateFP > stackSize) {
                heatingOutputRateFP = Math.round(heatingOutputRateFP);
            }
        }

        long heatRemoval = (long) (multiblock.rawHeating / effectiveHeating * heatingRecipeRate * inputSize * recipe.getFissionHeatingHeatPerInputMB());
        heatBuffer.changeHeatStored(-heatRemoval);
    }

    public double getEffectiveHeat() {
        return multiblock.rawHeating == 0L ? 0D : effectiveHeating / multiblock.rawHeating * heatBuffer.getHeatStored();
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
        return multiblock.getPartMap(SolidFissionCellEntity.class).get(pos.asLong());
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
    public @Nonnull List<Tank> getVentTanks(List<Tank> backupTanks) {
        return multiblock.isAssembled() ? tanks : backupTanks;
    }

    // Client

    @Override
    public void onUpdateClient() {
        super.onUpdateClient();
    }

    // NBT

    @Override
    public void writeToLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        super.writeToLogicTag(data, registries, syncReason);
        writeTanks(tanks, data, registries, "tanks");
        data.putInt("heatingOutputRate", heatingOutputRate);
        data.putDouble("effectiveHeating", effectiveHeating);
        data.putDouble("reservedEffectiveHeat", reservedEffectiveHeat);
        data.putDouble("heatingOutputRateFP", heatingOutputRateFP);
    }

    @Override
    public void readFromLogicTag(CompoundTag data, HolderLookup.Provider registries, SyncReason syncReason) {
        super.readFromLogicTag(data, registries, syncReason);
        readTanks(tanks, data, registries, "tanks");
        heatingOutputRate = data.getInt("heatingOutputRate");
        effectiveHeating = data.getDouble("effectiveHeating");
        reservedEffectiveHeat = data.getDouble("reservedEffectiveHeat");
        heatingOutputRateFP = data.getDouble("heatingOutputRateFP");
    }

    // Packets
    @Override
    public SolidFissionUpdatePacket getMultiblockUpdatePacket() {
        return new SolidFissionUpdatePacket(multiblock.controller.getTilePos(), multiblock.isReactorOn, heatBuffer, multiblock.clusterCount, multiblock.cooling, multiblock.rawHeating, multiblock.totalHeatMult, multiblock.meanHeatMult, multiblock.fuelComponentCount, multiblock.usefulPartCount, multiblock.totalEfficiency, multiblock.meanEfficiency, multiblock.sparsityEfficiencyMult, effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat);
    }

    @Override
    public void onMultiblockUpdatePacket(FissionUpdatePacket message) {
        super.onMultiblockUpdatePacket(message);
        if (message instanceof SolidFissionUpdatePacket packet) {
            effectiveHeating = packet.effectiveHeating;
            heatingOutputRateFP = packet.heatingOutputRateFP;
            reservedEffectiveHeat = packet.reservedEffectiveHeat;
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
