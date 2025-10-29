package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.config.NCConfig;
import com.nred.nuclearcraft.util.LambdaHelper;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongRBTreeSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HeatExchangerTubeNetwork {

    public final HeatExchangerLogic logic;

    public final LongSet tubePosLongSet = new LongOpenHashSet();
    public final LongSet inletPosLongSet = new LongRBTreeSet();
    public final LongSet outletPosLongSet = new LongOpenHashSet();

    public @Nullable HeatExchangerInletEntity masterInlet = null;

    public int usefulTubeCount = 0;
    public Vec3 tubeFlow = Vec3.ZERO, shellFlow = Vec3.ZERO;
    public double flowCosine = 0D, baseHeatingMultiplier = 0D, baseCoolingMultiplier = 0D;

    public HeatExchangerTubeNetwork(HeatExchangerLogic logic) {
        this.logic = logic;
    }

    public List<Tank> getTanks() {
        return masterInlet == null ? Collections.emptyList() : masterInlet.masterTanks;
    }

    public void setTubeFlows(Map<Long, HeatExchangerTubeEntity> tubeMap) {
        Long2ObjectMap<ObjectSet<Vec3>> flowMap = HeatExchangerFlowHelper.getFlowMap(
                inletPosLongSet,
                outletPosLongSet,
                x -> LambdaHelper.let(x.asLong(), y -> tubePosLongSet.contains(y) ? tubeMap.get(y).settings : null),
                HeatExchangerTubeSetting::isOpen,
                (x, y) -> tubePosLongSet.contains(x.asLong()),
                x -> outletPosLongSet.contains(x.asLong())
        );

        for (Long2ObjectMap.Entry<ObjectSet<Vec3>> entry : flowMap.long2ObjectEntrySet()) {
            HeatExchangerTubeEntity tube = tubeMap.get(entry.getLongKey());
            tube.tubeFlow = entry.getValue().stream().reduce(Vec3.ZERO, Vec3::add).normalize();
        }
    }

    public void setFlowStats(Map<Long, HeatExchangerTubeEntity> tubeMap) {
        usefulTubeCount = 0;
        tubeFlow = Vec3.ZERO;
        shellFlow = Vec3.ZERO;
        flowCosine = 0D;
        baseHeatingMultiplier = 0D;
        baseCoolingMultiplier = 0D;

        for (long tubePosLong : tubePosLongSet) {
            HeatExchangerTubeEntity tube = tubeMap.get(tubePosLong);
            if (tube.tubeFlow != null && (tube.shellFlow != null || logic.isCondenser())) {
                ++usefulTubeCount;
                tubeFlow = tubeFlow.add(tube.tubeFlow);
                if (tube.shellFlow != null) {
                    shellFlow = shellFlow.add(tube.shellFlow);
                    flowCosine += tube.tubeFlow.dot(tube.shellFlow);
                }
                baseHeatingMultiplier += tube.tubeType.getHeatTransferCoefficient() * tube.tubeType.getHeatRetentionMultiplier();
                baseCoolingMultiplier += tube.tubeType.getHeatTransferCoefficient();
            }
        }

        if (usefulTubeCount > 0) {
            tubeFlow = tubeFlow.scale(1D / usefulTubeCount);
            shellFlow = shellFlow.scale(1D / usefulTubeCount);
            flowCosine /= usefulTubeCount;
        }
    }

    public boolean isContraflow() {
        return flowCosine < 1D / NCConfig.heat_exchanger_max_size;
    }
}