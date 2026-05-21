package com.nred.nuclearcraft.multiblock.hx;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerInletEntity;
import com.nred.nuclearcraft.block_entity.hx.HeatExchangerTubeEntity;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank;
import com.nred.nuclearcraft.config.NCConfig;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongRBTreeSet;
import it.unimi.dsi.fastutil.longs.LongSet;
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
        logic.setNetworkTubeFlows(this, tubeMap);
    }

    public void setFlowStats(Map<Long, HeatExchangerTubeEntity> tubeMap) {
        logic.setNetworkFlowStats(this, tubeMap);
    }

    public boolean isContraflow() {
        return flowCosine < 1D / NCConfig.heat_exchanger_max_size;
    }
}