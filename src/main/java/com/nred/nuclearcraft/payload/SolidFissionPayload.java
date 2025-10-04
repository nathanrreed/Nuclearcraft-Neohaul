package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.fission.SolidFissionControllerEntity;
import com.nred.nuclearcraft.helpers.HeatBuffer;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.SolidFuelFissionLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record SolidFissionPayload(FissionUpdatePayload fissionUpdatePayload, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) implements CustomPacketPayload {
    public SolidFissionPayload(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult,
                               int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) {
        this(new FissionUpdatePayload(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult), effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat);
    }

    public static final Type<SolidFissionPayload> TYPE = new Type<>(ncLoc("solid_fission_update_server_to_client"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SolidFissionPayload> STREAM_CODEC = StreamCodec.composite(
            FissionUpdatePayload.STREAM_CODEC, SolidFissionPayload::fissionUpdatePayload,
            ByteBufCodecs.DOUBLE, SolidFissionPayload::effectiveHeating,
            ByteBufCodecs.DOUBLE, SolidFissionPayload::heatingOutputRateFP,
            ByteBufCodecs.DOUBLE, SolidFissionPayload::reservedEffectiveHeat,
            SolidFissionPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(final SolidFissionPayload payload, final IPayloadContext context) {
        FissionReactor reactor = ((SolidFissionControllerEntity) Objects.requireNonNull(context.player().level().getBlockEntity(payload.fissionUpdatePayload.pos()))).getMultiblockController().orElse(null);
        if (reactor != null) {
            SolidFuelFissionLogic logic = (SolidFuelFissionLogic) reactor.logic;
            logic.effectiveHeating = payload.effectiveHeating;
            logic.heatingOutputRateFP = payload.heatingOutputRateFP;
            logic.reservedEffectiveHeat = payload.reservedEffectiveHeat;
            FissionUpdatePayload.handleOnClient(reactor, payload.fissionUpdatePayload, context);
        }
    }
}
