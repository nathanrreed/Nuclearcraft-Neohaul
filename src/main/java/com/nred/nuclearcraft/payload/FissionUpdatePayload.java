package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.helpers.HeatBuffer;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.util.StreamCodecsHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.HeatBuffer.HEAT_BUFFER_STREAM_CODEC;

public record FissionUpdatePayload(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult,
                                   int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult) {
    public static final StreamCodec<RegistryFriendlyByteBuf, FissionUpdatePayload> STREAM_CODEC = StreamCodecsHelper.composite(
            BlockPos.STREAM_CODEC, FissionUpdatePayload::pos,
            ByteBufCodecs.BOOL, FissionUpdatePayload::isReactorOn,
            HEAT_BUFFER_STREAM_CODEC, FissionUpdatePayload::heatBuffer,
            ByteBufCodecs.INT, FissionUpdatePayload::clusterCount,
            ByteBufCodecs.VAR_LONG, FissionUpdatePayload::cooling,
            ByteBufCodecs.VAR_LONG, FissionUpdatePayload::rawHeating,
            ByteBufCodecs.VAR_LONG, FissionUpdatePayload::totalHeatMult,
            ByteBufCodecs.DOUBLE, FissionUpdatePayload::meanHeatMult,
            ByteBufCodecs.INT, FissionUpdatePayload::fuelComponentCount,
            ByteBufCodecs.VAR_LONG, FissionUpdatePayload::usefulPartCount,
            ByteBufCodecs.DOUBLE, FissionUpdatePayload::totalEfficiency,
            ByteBufCodecs.DOUBLE, FissionUpdatePayload::meanEfficiency,
            ByteBufCodecs.DOUBLE, FissionUpdatePayload::sparsityEfficiencyMult,
            FissionUpdatePayload::new);

    public static void handleOnClient(FissionReactor reactor, final FissionUpdatePayload payload, final IPayloadContext context) {
        if (reactor != null) {
            reactor.isReactorOn = payload.isReactorOn;
            reactor.clusterCount = payload.clusterCount;
            reactor.cooling = payload.cooling;
            reactor.rawHeating = payload.rawHeating;
            reactor.totalHeatMult = payload.totalHeatMult;
            reactor.meanHeatMult = payload.meanHeatMult;
            reactor.fuelComponentCount = payload.fuelComponentCount;
            reactor.usefulPartCount = payload.usefulPartCount;
            reactor.totalEfficiency = payload.totalEfficiency;
            reactor.meanEfficiency = payload.meanEfficiency;
            reactor.sparsityEfficiencyMult = payload.sparsityEfficiencyMult;

            // TODO was logic
            reactor.logic.heatBuffer.setHeatStored(payload.heatBuffer.getHeatStored());
            reactor.logic.heatBuffer.setHeatCapacity(payload.heatBuffer.getHeatCapacity());
        }
    }
}

