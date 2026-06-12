package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block_entity.hx.*;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity.InletProcessorElement;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.TankOutputSetting;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.multiblock.IPacketMultiblockLogic;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerUpdatePacket;
import com.nred.nuclearcraft.recipe.BasicRecipe;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.recipe.exchanger.HeatExchangerRecipe;
import com.nred.nuclearcraft.util.LambdaHelper;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.zerono.mods.zerocore.lib.data.nbt.ISyncableEntity;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCConfig.heat_exchanger_max_size;
import static com.nred.nuclearcraft.config.NCConfig.heat_exchanger_min_size;

public class HeatExchangerLogic extends MultiblockLogic<HeatExchanger, HeatExchangerLogic> implements IPacketMultiblockLogic<HeatExchanger, HeatExchangerLogic, HeatExchangerUpdatePacket> {
    public HeatExchangerLogic(HeatExchanger exchanger) {
        super(exchanger);
    }

    public HeatExchangerLogic(HeatExchangerLogic oldLogic) {
        super(oldLogic.multiblock);
    }

    @Override
    public String getID() {
        return "heat_exchanger";
    }

    protected boolean isHX() {
        return getID().equals("heat_exchanger");
    }

    protected int getShellInputTankDensity() {
        return HeatExchanger.BASE_MAX_INPUT;
    }

    protected int getShellOutputTankDensity() {
        return HeatExchanger.BASE_MAX_OUTPUT;
    }

    protected int getTubeInputTankDensity() {
        return HeatExchanger.BASE_MAX_INPUT;
    }

    protected int getTubeOutputTankDensity() {
        return HeatExchanger.BASE_MAX_OUTPUT;
    }

    protected Set<ResourceLocation> getShellValidFluids() {
        return NCRecipes.heat_exchanger.getValidFluids(getWorld(), 0);
    }

    protected Set<ResourceLocation> getTubeValidFluids() {
        return NCRecipes.heat_exchanger.getValidFluids(getWorld(), 0);
    }

    // Multiblock Size Limits

    @Override
    public int getMinimumInteriorLength() {
        return heat_exchanger_min_size;
    }

    @Override
    public int getMaximumInteriorLength() {
        return heat_exchanger_max_size;
    }

    // Multiblock Methods

    @Override
    public void onMachineAssembled() {
        onExchangerFormed();
    }

    @Override
    public void onMachineRestored() {
        onExchangerFormed();
    }

    protected void onExchangerFormed() {
        for (IHeatExchangerController<?> contr : getParts(IHeatExchangerController.class)) {
            multiblock.controller = contr;
            break;
        }

        if (!getWorld().isClientSide()) {
            setupExchanger();
            refreshAll();
            setIsExchangerOn();
        }
    }

    protected void setupExchanger() {
        int volume = multiblock.getExteriorVolume();
        multiblock.shellTanks.get(0).setCapacity(getShellInputTankDensity() * volume);
        multiblock.shellTanks.get(1).setCapacity(getShellOutputTankDensity() * volume);

        multiblock.shellTanks.get(0).setAllowedFluids(getShellValidFluids());

        Map<Long, HeatExchangerTubeEntity> tubeMap = getPartMap(HeatExchangerTubeEntity.class);
        Map<Long, HeatExchangerInletEntity> inletMap = getPartMap(HeatExchangerInletEntity.class);

        Set<ResourceLocation> tubeValidFluids = getTubeValidFluids();

        for (HeatExchangerTubeNetwork network : multiblock.networks) {
            for (long inletPosLong : network.inletPosLongSet) {
                network.masterInlet = inletMap.get(inletPosLong);
                network.masterInlet.isMasterInlet = true;
                break;
            }

            List<Tank> tanks = network.getTanks();
            int capacityMult = network.tubePosLongSet.size() + 2;
            tanks.get(0).setCapacity(getTubeInputTankDensity() * capacityMult);
            tanks.get(1).setCapacity(getTubeOutputTankDensity() * capacityMult);

            tanks.get(0).setAllowedFluids(tubeValidFluids);

            network.setFlowStats(tubeMap);
        }

        for (HeatExchangerInletEntity inlet : inletMap.values()) {
            inlet.markDirtyAndNotify(true);
        }

        multiblock.totalNetworkCount = multiblock.networks.size();
    }

    @Override
    public void onMachinePaused() {
        onExchangerBroken();
    }

    @Override
    public void onMachineDisassembled() {
        onExchangerBroken();
    }

    public void onExchangerBroken() {
        multiblock.masterShellInlet = null;
        multiblock.networks.clear();

        multiblock.shellRecipe = Fluids.EMPTY;

        multiblock.totalNetworkCount = multiblock.activeNetworkCount = 0;
        multiblock.activeTubeCount = multiblock.activeContactCount = 0;
        multiblock.shellSpeedMultiplier = 0D;
        multiblock.tubeInputRate = multiblock.tubeInputRateFP = 0D;
        multiblock.shellInputRate = multiblock.shellInputRateFP = 0D;
        multiblock.heatTransferRate = multiblock.heatTransferRateFP = 0D;
        multiblock.totalTempDiff = 0D;

        setIsExchangerOn();
    }

    @Override
    public boolean isMachineWhole() {
        if (containsBlacklistedPart()) {
            return false;
        }

        multiblock.masterShellInlet = null;
        multiblock.networks.clear();

        multiblock.shellRecipe = Fluids.EMPTY;

        multiblock.totalNetworkCount = 0;

        Map<Long, HeatExchangerTubeEntity> tubeMap = getPartMap(HeatExchangerTubeEntity.class);
        Map<Long, HeatExchangerBaffleEntity> baffleMap = getPartMap(HeatExchangerBaffleEntity.class);

        for (HeatExchangerTubeEntity tube : tubeMap.values()) {
            tube.tubeFlow = null;
            tube.shellFlow = null;
        }

        Map<Long, HeatExchangerInletEntity> inletMap = getPartMap(HeatExchangerInletEntity.class);
        Map<Long, HeatExchangerOutletEntity> outletMap = getPartMap(HeatExchangerOutletEntity.class);

        for (HeatExchangerInletEntity inlet : inletMap.values()) {
            inlet.isMasterInlet = false;
            inlet.network = null;
        }

        for (HeatExchangerOutletEntity outlet : outletMap.values()) {
            outlet.network = null;
        }

        LongSet tubeInletPosLongSet = new LongOpenHashSet();
        LongSet tubeOutletPosLongSet = new LongOpenHashSet();

        LongSet visitedTubePosLongSet = new LongOpenHashSet();

        for (long tubePosLong : tubeMap.keySet()) {
            if (!visitedTubePosLongSet.contains(tubePosLong)) {
                HeatExchangerTubeNetwork network = new HeatExchangerTubeNetwork(this);

                LongList tubePosLongStack = new LongArrayList();
                LongSupplier popTubePosLong = () -> tubePosLongStack.removeLong(tubePosLongStack.size() - 1);

                visitedTubePosLongSet.add(tubePosLong);
                tubePosLongStack.add(tubePosLong);

                while (!tubePosLongStack.isEmpty()) {
                    long nextPosLong = popTubePosLong.getAsLong();
                    BlockPos nextPos = BlockPos.of(nextPosLong);

                    HeatExchangerTubeEntity tube = tubeMap.get(nextPosLong);
                    HeatExchangerTubeSetting[] tubeSettings = tube.settings;

                    for (int i = 0; i < 6; ++i) {
                        if (tubeSettings[i].isOpen()) {
                            Direction dir = Direction.values()[i];
                            long offsetPosLong = nextPos.relative(dir).asLong();

                            HeatExchangerInletEntity inlet = inletMap.get(offsetPosLong);
                            if (inlet != null) {
                                tubeInletPosLongSet.add(offsetPosLong);
                                network.inletPosLongSet.add(offsetPosLong);
                                inlet.network = network;
                                continue;
                            }

                            HeatExchangerOutletEntity outlet = outletMap.get(offsetPosLong);
                            if (outlet != null) {
                                tubeOutletPosLongSet.add(offsetPosLong);
                                network.outletPosLongSet.add(offsetPosLong);
                                outlet.network = network;
                                continue;
                            }

                            HeatExchangerTubeEntity other = tubeMap.get(offsetPosLong);
                            if (other != null && other.getTubeSetting(dir.getOpposite()).isOpen()) {
                                if (!visitedTubePosLongSet.contains(offsetPosLong)) {
                                    visitedTubePosLongSet.add(offsetPosLong);
                                    tubePosLongStack.add(offsetPosLong);
                                }
                                continue;
                            }

                            multiblock.setLastError(nextPos, MODID + ".multiblock_validation.heat_exchanger.dangling_tube");
                            return false;
                        }
                    }

                    network.tubePosLongSet.add(nextPosLong);
                }

                multiblock.networks.add(network);
            }
        }

        for (HeatExchangerTubeNetwork network : multiblock.networks) {
            if (network.inletPosLongSet.isEmpty() || network.outletPosLongSet.isEmpty()) {
                for (long posLong : network.tubePosLongSet) {
                    multiblock.setLastError(BlockPos.of(posLong), MODID + ".multiblock_validation.heat_exchanger.invalid_network");
                    return false;
                }
            }
        }

        LongSet shellInletPosLongSet = new LongRBTreeSet(), shellOutletPosLongSet = new LongOpenHashSet();

        for (long inletPosLong : inletMap.keySet()) {
            if (!tubeInletPosLongSet.contains(inletPosLong)) {
                shellInletPosLongSet.add(inletPosLong);
            }
        }

        for (long outletPosLong : outletMap.keySet()) {
            if (!tubeOutletPosLongSet.contains(outletPosLong)) {
                shellOutletPosLongSet.add(outletPosLong);
            }
        }

        for (HeatExchangerTubeNetwork network : multiblock.networks) {
            network.setTubeFlows(tubeMap);
        }

        if (shellInletPosLongSet.isEmpty() || shellOutletPosLongSet.isEmpty()) {
            multiblock.setLastError(MODID + ".multiblock_validation.heat_exchanger.invalid_shell");
            return false;
        }

        LongSet shellPosLongSet = new LongOpenHashSet();
        LongList shellPosLongStack = new LongArrayList();
        LongSupplier popShellPosLong = () -> shellPosLongStack.removeLong(shellPosLongStack.size() - 1);

        for (long inletPosLong : shellInletPosLongSet) {
            BlockPos inletPos = BlockPos.of(inletPosLong);
            long clampedPosLong = multiblock.getClampedInteriorCoord(inletPos).asLong();
            if (baffleMap.containsKey(clampedPosLong)) {
                multiblock.setLastError(inletPos, MODID + ".multiblock_validation.heat_exchanger.blocked_inlet");
                return false;
            }

            shellPosLongSet.add(clampedPosLong);
            shellPosLongStack.add(clampedPosLong);

            if (multiblock.masterShellInlet == null) {
                multiblock.masterShellInlet = inletMap.get(inletPosLong);
                multiblock.masterShellInlet.isMasterInlet = true;
            }
        }

        for (long outletPosLong : shellOutletPosLongSet) {
            BlockPos outletPos = BlockPos.of(outletPosLong);
            if (baffleMap.containsKey(multiblock.getClampedInteriorCoord(outletPos).asLong())) {
                multiblock.setLastError(outletPos, MODID + ".multiblock_validation.heat_exchanger.blocked_outlet");
                return false;
            }
        }

        while (!shellPosLongStack.isEmpty()) {
            long nextPosLong = popShellPosLong.getAsLong();
            BlockPos nextPos = BlockPos.of(nextPosLong);

            if (!tubeMap.containsKey(nextPosLong) && !getWorld().getBlockState(nextPos).isAir()) {
                multiblock.setLastError("zerocore.api.nc.multiblock.validation.invalid_part_for_interior", nextPos, nextPos.getX(), nextPos.getY(), nextPos.getZ());
                return false;
            }

            HeatExchangerTubeSetting[] tubeSettings = tubeMap.containsKey(nextPosLong) ? tubeMap.get(nextPosLong).settings : null;

            for (int i = 0; i < 6; ++i) {
                if (tubeSettings == null || !tubeSettings[i].isBaffle()) {
                    Direction dir = Direction.values()[i];
                    BlockPos offsetPos = nextPos.relative(dir);
                    long offsetPosLong = offsetPos.asLong();

                    if (multiblock.isInInterior(offsetPos) && !shellPosLongSet.contains(offsetPosLong) && (!baffleMap.containsKey(offsetPosLong) || (tubeMap.containsKey(offsetPosLong) && !tubeMap.get(offsetPosLong).getTubeSetting(dir.getOpposite()).isBaffle()))) {
                        shellPosLongSet.add(offsetPosLong);
                        shellPosLongStack.add(offsetPosLong);
                    }
                }
            }
        }

        if (shellPosLongSet.size() + baffleMap.size() != multiblock.getInteriorVolume()) {
            multiblock.setLastError(MODID + ".multiblock_validation.heat_exchanger.blocked_shell");
            return false;
        }

        Long2ObjectMap<ObjectSet<Vec3>> flowMap = HeatExchangerFlowHelper.getFlowMap(shellInletPosLongSet, shellOutletPosLongSet, x -> LambdaHelper.let(x.asLong(), y -> tubeMap.containsKey(y) ? tubeMap.get(y).settings : null), x -> !x.isBaffle(), (x, y) -> {
            long posLong = x.asLong();
            return shellPosLongSet.contains(posLong) && (!tubeMap.containsKey(posLong) || !tubeMap.get(posLong).getTubeSetting(y.getOpposite()).isBaffle());
        }, x -> shellOutletPosLongSet.contains(x.asLong()));

        for (Long2ObjectMap.Entry<ObjectSet<Vec3>> entry : flowMap.long2ObjectEntrySet()) {
            long posLong = entry.getLongKey();
            if (tubeMap.containsKey(posLong)) {
                HeatExchangerTubeEntity tube = tubeMap.get(posLong);
                if (tube.tubeFlow != null) {
                    tube.shellFlow = entry.getValue().stream().reduce(Vec3.ZERO, Vec3::add).normalize();
                }
            }
        }

        for (IHeatExchangerController<?> controller : getParts(IHeatExchangerController.class)) {
            controller.setIsRenderer(false);
        }
        for (IHeatExchangerController<?> controller : getParts(IHeatExchangerController.class)) {
            if (multiblock.shouldSpecialRender) {
                controller.setIsRenderer(true);
            }
            break;
        }

        return true;
    }

    @Override
    public List<Pair<Class<? extends IMultiblockPart<HeatExchanger>>, String>> getPartBlacklist() {
        return Collections.emptyList();
    }

    @Override
    public void onAssimilate(IMultiblockController<HeatExchanger> assimilated) {
    }

    @Override
    public void onAssimilated(IMultiblockController<HeatExchanger> assimilator) {
    }

    // Server

    @Override
    public boolean onUpdateServer() {
        multiblock.refreshFlag = false;
        multiblock.packetFlag = 0;

        boolean shouldUpdate = onUpdateServerInternal();

        if (shouldUpdate) {
            refreshAll();
        }

        if (multiblock.packetFlag > 1) {
            multiblock.sendMultiblockUpdatePacketToAll();
        } else if (multiblock.packetFlag > 0) {
            multiblock.sendMultiblockUpdatePacketToListeners();
        }

        if (multiblock.controller != null) {
            multiblock.sendRenderPacketToAll();
        }

        return shouldUpdate;
    }

    protected boolean onUpdateServerInternal() {
        multiblock.activeNetworkCount = 0;
        multiblock.activeTubeCount = 0;
        multiblock.activeContactCount = 0;
        multiblock.shellSpeedMultiplier = 0D;
        multiblock.tubeInputRate = 0D;
        multiblock.shellInputRate = 0D;
        multiblock.heatTransferRate = 0D;
        multiblock.totalTempDiff = 0D;

        if (isHX()) {
            prepareTransferProposals();
        }

        int[] inletUpdates = multiblock.getMasterInlets().mapToInt(x -> x.processor.onTick(getWorld()) ? 1 : 0).toArray();
        boolean shouldUpdate = multiblock.refreshFlag || Arrays.stream(inletUpdates).anyMatch(x -> x != 0);

        multiblock.tubeInputRateFP = multiblock.tubeInputRateTracker.update(multiblock.tubeInputRate);
        multiblock.shellInputRateFP = multiblock.shellInputRateTracker.update(multiblock.shellInputRate);
        multiblock.heatTransferRateFP = multiblock.heatTransferRateTracker.update(multiblock.heatTransferRate);

        return shouldUpdate;
    }

    public void setActivity(boolean isExchangerOn) {
        multiblock.controller.setActivity(isExchangerOn);
    }

    public void setIsExchangerOn() {
        boolean oldIsExchangerOn = multiblock.isExchangerOn;
        multiblock.isExchangerOn = (isRedstonePowered() || multiblock.computerActivated) && multiblock.isAssembled();
        if (multiblock.isExchangerOn != oldIsExchangerOn) {
            if (multiblock.controller != null) {
                setActivity(multiblock.isExchangerOn);
                multiblock.sendMultiblockUpdatePacketToAll();
            }
        }
    }

    protected boolean isRedstonePowered() {
        return Stream.concat(Stream.of(multiblock.controller), getParts(HeatExchangerRedstonePortEntity.class).stream()).anyMatch(x -> x != null && x.getIsRedstonePowered());
    }

    public void refreshRecipe() {
        multiblock.getMasterInlets().forEach(x -> x.processor.refreshRecipe(getWorld()));
    }

    public void refreshActivity() {
        multiblock.getMasterInlets().forEach(x -> x.processor.refreshActivity());
    }

    public void refreshAll() {
        multiblock.getMasterInlets().forEach(x -> x.processor.refreshAll(getWorld()));
    }

    public @Nonnull List<Tank> getInletTanks(HeatExchangerTubeNetwork network) {
        List<Tank> tanks = network != null ? network.getTanks() : (multiblock.isAssembled() ? multiblock.shellTanks : Collections.emptyList());
        return tanks.isEmpty() ? Collections.emptyList() : tanks.subList(0, 1);
    }

    public @Nonnull List<Tank> getOutletTanks(HeatExchangerTubeNetwork network) {
        List<Tank> tanks = network != null ? network.getTanks() : (multiblock.isAssembled() ? multiblock.shellTanks : Collections.emptyList());
        return tanks.size() < 2 ? Collections.emptyList() : tanks.subList(1, 2);
    }

    public void setNetworkTubeFlows(HeatExchangerTubeNetwork network, Map<Long, HeatExchangerTubeEntity> tubeMap) {
        Long2ObjectMap<ObjectSet<Vec3>> flowMap = HeatExchangerFlowHelper.getFlowMap(network.inletPosLongSet, network.outletPosLongSet, x -> LambdaHelper.let(x.asLong(), y -> network.tubePosLongSet.contains(y) ? tubeMap.get(y).settings : null), HeatExchangerTubeSetting::isOpen, (x, y) -> network.tubePosLongSet.contains(x.asLong()), x -> network.outletPosLongSet.contains(x.asLong()));

        for (Long2ObjectMap.Entry<ObjectSet<Vec3>> entry : flowMap.long2ObjectEntrySet()) {
            HeatExchangerTubeEntity tube = tubeMap.get(entry.getLongKey());
            tube.tubeFlow = entry.getValue().stream().reduce(Vec3.ZERO, Vec3::add).normalize();
        }
    }

    public void setNetworkFlowStats(HeatExchangerTubeNetwork network, Map<Long, HeatExchangerTubeEntity> tubeMap) {
        network.usefulTubeCount = 0;
        network.tubeFlow = Vec3.ZERO;
        network.shellFlow = Vec3.ZERO;
        network.flowCosine = 0D;
        network.baseHeatingMultiplier = 0D;
        network.baseCoolingMultiplier = 0D;

        for (long tubePosLong : network.tubePosLongSet) {
            HeatExchangerTubeEntity tube = tubeMap.get(tubePosLong);
            if (tube.tubeFlow != null && (tube.shellFlow != null || !isHX())) {
                ++network.usefulTubeCount;
                network.tubeFlow = network.tubeFlow.add(tube.tubeFlow);
                if (tube.shellFlow != null) {
                    network.shellFlow = network.shellFlow.add(tube.shellFlow);
                    network.flowCosine += tube.tubeFlow.dot(tube.shellFlow);
                }
                network.baseHeatingMultiplier += tube.tubeType.getHeatTransferCoefficient() * tube.tubeType.getHeatRetentionMultiplier();
                network.baseCoolingMultiplier += tube.tubeType.getHeatTransferCoefficient();
            }
        }

        if (network.usefulTubeCount > 0) {
            network.tubeFlow = network.tubeFlow.scale(1D / network.usefulTubeCount);
            network.shellFlow = network.shellFlow.scale(1D / network.usefulTubeCount);
            network.flowCosine /= network.usefulTubeCount;
        }
    }

    public BasicRecipeHandler getInletRecipeHandler(HeatExchangerInletEntity inlet) {
        return NCRecipes.heat_exchanger;
    }

    public void setInletRecipeStats(HeatExchangerInletEntity inlet, @Nullable BasicRecipe basicRecipe) {
        if (basicRecipe instanceof HeatExchangerRecipe recipe) {
            inlet.processor.baseProcessTime = recipe.getHeatExchangerHeatDifference();
            inlet.inputTemperature = recipe.getHeatExchangerInputTemperature();
            inlet.outputTemperature = recipe.getHeatExchangerOutputTemperature();
            inlet.isHeating = recipe.getHeatExchangerIsHeating();
        } else {
            inlet.processor.baseProcessTime = 1D;
            inlet.inputTemperature = 300;
            inlet.outputTemperature = 300;
            inlet.isHeating = false;
        }
    }

    public double getInletSpeedMultiplier(HeatExchangerInletEntity inlet) {
        return inlet.speedMultiplierProposal;
    }

    public void inletProcess(HeatExchangerInletEntity inlet) {
        InletProcessorElement processor = inlet.processor;

        double speedMultiplier = inlet.speedMultiplierProposal;
        double maxProcessCount = processor.baseProcessTime <= 0D ? 0D : speedMultiplier / processor.baseProcessTime;

        processor.time += speedMultiplier;

        int processCount = 0;
        while (processor.time >= processor.baseProcessTime) {
            processor.finishProcess(inlet.getLevel());
            ++processCount;
        }

        multiblock.heatTransferRate += inlet.heatTransferRateProposal * (processCount == 0 || maxProcessCount <= 0D ? 1D : processCount / maxProcessCount);
    }

    protected void prepareTransferProposals() {
        multiblock.getMasterInlets().forEach(x -> x.speedMultiplierProposal = x.heatTransferRateProposal = 0D);

        HeatExchangerInletEntity shellInlet = multiblock.masterShellInlet;
        if (shellInlet == null || shellInlet.processor.recipeInfo == null || !shellInlet.processor.readyToProcess()) {
            return;
        }

        List<HeatExchangerTransferProposal> proposals = new ArrayList<>();
        for (HeatExchangerTubeNetwork network : multiblock.networks) {
            HeatExchangerInletEntity tubeInlet = network.masterInlet;
            if (tubeInlet == null || tubeInlet.processor.recipeInfo == null || !tubeInlet.processor.readyToProcess()) {
                continue;
            }

            HeatExchangerTransferProposal proposal = getInletTransferProposal(tubeInlet);
            if (proposal == null) {
                continue;
            }

            double maxTubeTransferRate = getInletMaxLatentTransferRate(tubeInlet);
            if (maxTubeTransferRate <= 0D || proposal.tubeRecipeRateMultiplier <= 0D) {
                continue;
            }

            double cappedSharedTransferRate = Math.min(proposal.sharedTransferRate, maxTubeTransferRate / proposal.tubeRecipeRateMultiplier);
            if (cappedSharedTransferRate <= 0D) {
                continue;
            }

            proposals.add(proposal.withSharedTransferRate(cappedSharedTransferRate));
        }

        if (proposals.isEmpty()) {
            return;
        }

        double maxShellTransferRate = getInletMaxLatentTransferRate(shellInlet);
        if (maxShellTransferRate <= 0D) {
            return;
        }

        double totalShellDemandRate = 0D;
        for (HeatExchangerTransferProposal proposal : proposals) {
            totalShellDemandRate += proposal.sharedTransferRate * proposal.shellRecipeRateMultiplier;
        }
        if (totalShellDemandRate <= 0D) {
            return;
        }

        double shellRateFactor = Math.min(1D, maxShellTransferRate / totalShellDemandRate);
        for (HeatExchangerTransferProposal proposal : proposals) {
            double sharedTransferProposal = proposal.sharedTransferRate * shellRateFactor;
            if (sharedTransferProposal <= 0D) {
                continue;
            }

            proposal.inlet.speedMultiplierProposal = sharedTransferProposal * proposal.tubeRecipeRateMultiplier;
            proposal.inlet.heatTransferRateProposal = sharedTransferProposal * proposal.heatTransferRateMultiplier;
            shellInlet.speedMultiplierProposal += sharedTransferProposal * proposal.shellRecipeRateMultiplier;

            multiblock.totalTempDiff += proposal.absMeanTempDiff * proposal.usefulTubeCount;
            multiblock.activeContactCount += proposal.usefulTubeCount;
            ++multiblock.activeNetworkCount;
            multiblock.activeTubeCount += proposal.usefulTubeCount;
        }
    }

    protected double getInletMaxLatentTransferRate(HeatExchangerInletEntity inlet) {
        InletProcessorElement processor = inlet.processor;
        if (processor.recipeInfo == null || !processor.readyToProcess() || processor.baseProcessTime <= 0D) {
            return 0D;
        }

        int maxRecipeRate = getInletMaxRecipeRate(inlet);
        if (maxRecipeRate <= 0) {
            return 0D;
        }

        return Math.max(0D, maxRecipeRate * processor.baseProcessTime - processor.time);
    }

    protected int getInletMaxRecipeRate(HeatExchangerInletEntity inlet) {
        InletProcessorElement processor = inlet.processor;
        if (processor.recipeInfo == null || !processor.readyToProcess()) {
            return 0;
        }

        List<Tank> tanks = processor.getTanks();
        if (processor.getFluidInputSize() != 1 || processor.getFluidOutputSize() != 1 || tanks.size() < 2) {
            return 0;
        }

        int inputIndex = processor.getFluidInputTank(0);
        int outputIndex = processor.getFluidOutputTank(0);
        Tank inputTank = tanks.get(inputIndex);
        Tank outputTank = tanks.get(outputIndex);

        int recipeInputSize = 0;
        if (processor.hasConsumed && !processor.consumedTanks.isEmpty() && !processor.consumedTanks.get(0).isEmpty()) {
            recipeInputSize = processor.consumedTanks.get(0).getFluidAmount();
        }
        if (recipeInputSize <= 0) {
            SizedChanceFluidIngredient ingredient = processor.getFluidIngredients().get(0);
            recipeInputSize = ingredient.amount();
        }
        if (recipeInputSize <= 0) {
            return 0;
        }

        int consumedInputSize = processor.hasConsumed && !processor.consumedTanks.isEmpty() && !processor.consumedTanks.get(0).isEmpty() ? processor.consumedTanks.get(0).getFluidAmount() : 0;
        int maxInputRecipeRate = (consumedInputSize + inputTank.getFluidAmount()) / recipeInputSize;
        if (maxInputRecipeRate <= 0) {
            return 0;
        }

        if (processor.getTankOutputSetting(outputIndex) == TankOutputSetting.VOID) {
            return maxInputRecipeRate;
        }

        SizedChanceFluidIngredient product = processor.getFluidProducts().get(0);
        int recipeOutputSize = product.amount();
        if (recipeOutputSize <= 0) {
            return maxInputRecipeRate;
        }

        FluidStack productStack = product.getStack();
        if (productStack == null) {
            return 0;
        }

        int maxOutputRecipeRate;
        if (outputTank.isEmpty()) {
            maxOutputRecipeRate = processor.getFluidProductCapacity(outputTank, productStack) / recipeOutputSize;
        } else if (!FluidStack.isSameFluid(outputTank.getFluid(), productStack)) {
            return 0;
        } else {
            maxOutputRecipeRate = (processor.getFluidProductCapacity(outputTank, productStack) - outputTank.getFluidAmount()) / recipeOutputSize;
        }

        return Math.min(maxInputRecipeRate, maxOutputRecipeRate);
    }

    public @Nullable HeatExchangerTransferProposal getInletTransferProposal(HeatExchangerInletEntity inlet) {
        if (inlet.isMasterShellInlet() || inlet.network == null || inlet.processor.recipeInfo == null || multiblock.masterShellInlet == null || multiblock.masterShellInlet.processor.recipeInfo == null) {
            return null;
        }

        HeatExchangerRecipe shellRecipe = (HeatExchangerRecipe) multiblock.masterShellInlet.processor.recipeInfo.recipe;
        boolean shellIsHeating = shellRecipe.getHeatExchangerIsHeating();
        if (inlet.isHeating == shellIsHeating) {
            return null;
        }

        int shellInputTemperature = shellRecipe.getHeatExchangerInputTemperature();
        int shellOutputTemperature = shellRecipe.getHeatExchangerOutputTemperature();

        boolean contraflow = inlet.network.isContraflow();
        int inputEndShellTemperature = contraflow ? shellOutputTemperature : shellInputTemperature;
        int outputEndShellTemperature = contraflow ? shellInputTemperature : shellOutputTemperature;

        int inletTemperatureDiff = inlet.inputTemperature - inputEndShellTemperature;
        int outletTemperatureDiff = inlet.outputTemperature - outputEndShellTemperature;
        int sumTempDiff = inletTemperatureDiff + outletTemperatureDiff;
        if (sumTempDiff == 0) {
            return null;
        }

        boolean heating = sumTempDiff < 0;
        if (inlet.isHeating != heating) {
            return null;
        }

        if ((inletTemperatureDiff > 0 && outletTemperatureDiff < 0) || (inletTemperatureDiff < 0 && outletTemperatureDiff > 0)) {
            return null;
        }

        double absMeanTempDiff = HeatExchanger.getAbsMeanTempDiff(inletTemperatureDiff, outletTemperatureDiff);

        double tubeFlowDirectionMultiplier = ((HeatExchangerRecipe) inlet.processor.recipeInfo.recipe).getHeatExchangerFlowDirectionMultiplier(inlet.network.tubeFlow);
        double shellFlowDirectionMultiplier = shellRecipe.getHeatExchangerFlowDirectionMultiplier(inlet.network.shellFlow);
        double sharedTransferRate = absMeanTempDiff * tubeFlowDirectionMultiplier * shellFlowDirectionMultiplier;
        if (sharedTransferRate <= 0D) {
            return null;
        }

        double tubeRecipeRateMultiplier = heating ? inlet.network.baseHeatingMultiplier : inlet.network.baseCoolingMultiplier;
        double shellRecipeRateMultiplier = heating ? inlet.network.baseCoolingMultiplier : inlet.network.baseHeatingMultiplier;
        double heatTransferRateMultiplier = inlet.network.baseCoolingMultiplier;

        return new HeatExchangerTransferProposal(inlet, sharedTransferRate, tubeRecipeRateMultiplier, shellRecipeRateMultiplier, heatTransferRateMultiplier, absMeanTempDiff, inlet.network.usefulTubeCount);
    }

    // Client

    @Override
    public void onUpdateClient() {
    }

    // NBT

    @Override
    public void writeToLogicTag(CompoundTag data, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
    }

    @Override
    public void readFromLogicTag(CompoundTag data, HolderLookup.Provider registries, ISyncableEntity.SyncReason syncReason) {
    }

    // Packets

    @Override
    public HeatExchangerUpdatePacket getMultiblockUpdatePacket() {
        return new HeatExchangerUpdatePacket(multiblock.controller.getTilePos(), multiblock.isExchangerOn, multiblock.totalNetworkCount, multiblock.activeNetworkCount, multiblock.activeTubeCount, multiblock.activeContactCount, multiblock.tubeInputRateFP, multiblock.shellInputRateFP, multiblock.heatTransferRateFP, multiblock.totalTempDiff);
    }

    @Override
    public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
        multiblock.isExchangerOn = message.isExchangerOn;
        multiblock.totalNetworkCount = message.totalNetworkCount;
        multiblock.activeNetworkCount = message.activeNetworkCount;
        multiblock.activeTubeCount = message.activeTubeCount;
        multiblock.activeContactCount = message.activeContactCount;
        multiblock.tubeInputRateFP = message.tubeInputRateFP;
        multiblock.shellInputRateFP = message.shellInputRateFP;
        multiblock.heatTransferRateFP = message.heatTransferRateFP;
        multiblock.totalTempDiff = message.totalTempDiff;
    }

    public HeatExchangerRenderPacket getRenderPacket() {
        return new HeatExchangerRenderPacket(multiblock.controller.getTilePos(), multiblock.shellTanks);
    }

    public void onRenderPacket(HeatExchangerRenderPacket message) {
        TankInfo.readInfoList(message.shellTankInfos, multiblock.shellTanks);
    }

    // Multiblock Validators

    @Override
    public boolean isBlockGoodForInterior(Level level, int x, int y, int z) {
        return true;
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
    }
}