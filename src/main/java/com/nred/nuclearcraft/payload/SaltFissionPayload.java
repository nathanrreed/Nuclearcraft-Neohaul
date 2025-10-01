package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.fission.SaltFissionControllerEntity;
import com.nred.nuclearcraft.helpers.HeatBuffer;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record SaltFissionPayload(FissionUpdatePayload fissionUpdatePayload, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) implements CustomPacketPayload {
    public SaltFissionPayload(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult,
                              int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) {
        this(new FissionUpdatePayload(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult), effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat);
    }

    public static final Type<SaltFissionPayload> TYPE = new Type<>(ncLoc("salt_fission_update_server_to_client"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SaltFissionPayload> STREAM_CODEC = StreamCodec.composite(
            FissionUpdatePayload.STREAM_CODEC, SaltFissionPayload::fissionUpdatePayload,
            ByteBufCodecs.DOUBLE, SaltFissionPayload::effectiveHeating,
            ByteBufCodecs.DOUBLE, SaltFissionPayload::heatingOutputRateFP,
            ByteBufCodecs.DOUBLE, SaltFissionPayload::reservedEffectiveHeat,
            SaltFissionPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(final SaltFissionPayload payload, final IPayloadContext context) {
        FissionReactor reactor = ((SaltFissionControllerEntity) Objects.requireNonNull(context.player().level().getBlockEntity(payload.fissionUpdatePayload.pos()))).getMultiblockController().orElse(null);
        if (reactor != null) {
//            reactor.effectiveHeating = payload.effectiveHeating; TODO
//            reactor.heatingOutputRateFP = payload.heatingOutputRateFP;
//            reactor.reservedEffectiveHeat = payload.reservedEffectiveHeat;
            FissionUpdatePayload.handleOnClient(reactor, payload.fissionUpdatePayload, context);
        }
    }
}
