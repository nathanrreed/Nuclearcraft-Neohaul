package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block_entity.hx.IHeatExchangerController;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerOutletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.payload.multiblock.CondenserRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.CondenserUpdatePacket;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerRenderPacket;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerUpdatePacket;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class CondenserLogic extends HeatExchangerLogic {
    public static final int BASE_MAX_INPUT = 32000, BASE_MAX_OUTPUT = 8000;

    public CondenserLogic(HeatExchanger exchanger) {
        super(exchanger);
    }

    public CondenserLogic(HeatExchangerLogic oldLogic) {
        super(oldLogic);
    }

    @Override
    public String getID() {
        return "condenser";
    }

    @Override
    public boolean isCondenser() {
        return true;
    }

    @Override
    protected int getTubeInputTankDensity() {
        return BASE_MAX_INPUT;
    }

    @Override
    protected int getTubeOutputTankDensity() {
        return BASE_MAX_OUTPUT;
    }

    protected Set<ResourceLocation> getShellValidFluids() {
        return BuiltInRegistries.FLUID.entrySet().stream().filter(x -> x.getValue().getFluidType().getTemperature() <= 300).map(x -> x.getKey().location()).collect(Collectors.toSet());
    }

    protected Set<ResourceLocation> getTubeValidFluids() {
        return NCRecipes.condenser.validFluids.get(0);
    }

    // Multiblock Methods

    @Override
    protected void onExchangerFormed() {
        super.onExchangerFormed();
    }

    @Override
    public void onExchangerBroken() {
        super.onExchangerBroken();
    }

    @Override
    public boolean isMachineWhole() {
        if (containsBlacklistedPart()) {
            return false;
        }

        multiblock.masterShellInlet = null;
        multiblock.networks.clear();

        multiblock.shellRecipe = null;

        multiblock.totalNetworkCount = 0;

        Map<Long, HeatExchangerTubeEntity> tubeMap = getPartMap(HeatExchangerTubeEntity.class);

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

        for (int x = multiblock.getMinInteriorX(), maxX = multiblock.getMaxInteriorX(); x <= maxX; ++x) {
            for (int y = multiblock.getMinInteriorY(), maxY = multiblock.getMaxInteriorY(); y <= maxY; ++y) {
                for (int z = multiblock.getMinInteriorZ(), maxZ = multiblock.getMaxInteriorZ(); z <= maxZ; ++z) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!tubeMap.containsKey(pos.asLong()) && !getWorld().getBlockState(pos).isAir()) {
                        multiblock.setLastError(pos, MODID + ".multiblock_validation.condenser.blocked_shell");
                        return false;
                    }
                }
            }
        }

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
                                network.inletPosLongSet.add(offsetPosLong);
                                inlet.network = network;
                                continue;
                            }

                            HeatExchangerOutletEntity outlet = outletMap.get(offsetPosLong);
                            if (outlet != null) {
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

                            multiblock.setLastError(nextPos, MODID + ".multiblock_validation.condenser.dangling_tube");
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
                    multiblock.setLastError(BlockPos.of(posLong), MODID + ".multiblock_validation.condenser.invalid_network");
                    return false;
                }
            }
        }

        for (HeatExchangerTubeNetwork network : multiblock.networks) {
            network.setTubeFlows(tubeMap);
        }

        for (IHeatExchangerController<?> controller : getParts(IHeatExchangerController.class)) {
            controller.setIsRenderer(false);
        }
        for (IHeatExchangerController<?> controller : getParts(IHeatExchangerController.class)) {
            controller.setIsRenderer(true);
            break;
        }

        return true;
    }

    @Override
    public List<Pair<Class<? extends IMultiblockPart<HeatExchanger>>, String>> getPartBlacklist() {
        return Collections.emptyList();
    }

    @Override
    public @Nonnull List<Tank> getOutletTanks(HeatExchangerTubeNetwork network) {
        return network == null ? getInletTanks(network) : super.getOutletTanks(network);
    }

    // Packets

    @Override
    public CondenserUpdatePacket getMultiblockUpdatePacket() {
        return new CondenserUpdatePacket(multiblock.controller.getTilePos(), multiblock.isExchangerOn, multiblock.totalNetworkCount, multiblock.activeNetworkCount, multiblock.activeTubeCount, multiblock.activeContactCount, multiblock.tubeInputRateFP, multiblock.shellInputRateFP, multiblock.heatTransferRateFP, multiblock.totalTempDiff);
    }

    @Override
    public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
        super.onMultiblockUpdatePacket(message);
    }

    public CondenserRenderPacket getRenderPacket() {
        return new CondenserRenderPacket(multiblock.controller.getTilePos(), multiblock.shellTanks);
    }

    public void onRenderPacket(HeatExchangerRenderPacket message) {
        super.onRenderPacket(message);
    }

    // Clear Material

    @Override
    public void clearAllMaterial() {
        super.clearAllMaterial();
    }
}