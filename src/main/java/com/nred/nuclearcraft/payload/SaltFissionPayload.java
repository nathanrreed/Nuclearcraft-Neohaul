package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.fission.SaltFissionControllerEntity;
import com.nred.nuclearcraft.block.internal.heat.HeatBuffer;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.SaltFissionLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public record SaltFissionPayload(FissionUpdatePayload fissionUpdatePayload, double meanHeatingSpeedMultiplier, double totalHeatingSpeedMultiplier) implements CustomPacketPayload {
    public SaltFissionPayload(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult,
                              int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double meanHeatingSpeedMultiplier, double totalHeatingSpeedMultiplier) {
        this(new FissionUpdatePayload(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult), meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier);
    }

    public static final Type<SaltFissionPayload> TYPE = new Type<>(ncLoc("salt_fission_update_server_to_client"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SaltFissionPayload> STREAM_CODEC = StreamCodec.composite(
            FissionUpdatePayload.STREAM_CODEC, SaltFissionPayload::fissionUpdatePayload,
            ByteBufCodecs.DOUBLE, SaltFissionPayload::meanHeatingSpeedMultiplier,
            ByteBufCodecs.DOUBLE, SaltFissionPayload::totalHeatingSpeedMultiplier,
            SaltFissionPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(final SaltFissionPayload payload, final IPayloadContext context) {
        FissionReactor reactor = ((SaltFissionControllerEntity) Objects.requireNonNull(context.player().level().getBlockEntity(payload.fissionUpdatePayload.pos()))).getMultiblockController().orElse(null);
        if (reactor != null) {
            SaltFissionLogic logic = (SaltFissionLogic) reactor.logic;
            logic.meanHeatingSpeedMultiplier = payload.meanHeatingSpeedMultiplier;
            logic.totalHeatingSpeedMultiplier = payload.totalHeatingSpeedMultiplier;
            FissionUpdatePayload.handleOnClient(reactor, payload.fissionUpdatePayload, context);
        }
    }
}
