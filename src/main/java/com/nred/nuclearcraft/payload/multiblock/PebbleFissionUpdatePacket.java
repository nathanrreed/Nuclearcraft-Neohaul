package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.fission.PebbleFissionControllerEntity;
import com.nred.nuclearcraft.block_entity.internal.heat.HeatBuffer;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class PebbleFissionUpdatePacket extends FissionUpdatePacket {
    public static final CustomPacketPayload.Type<PebbleFissionUpdatePacket> TYPE = new CustomPacketPayload.Type<>(ncLoc("pebble_fission_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PebbleFissionUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            PebbleFissionUpdatePacket::toBytes, PebbleFissionUpdatePacket::fromBytes
    );
    public double meanHeatingSpeedMultiplier;

    public PebbleFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, double meanHeatMult, long usefulPartCount, double meanEfficiency, double sparsityEfficiencyMult, double meanHeatingSpeedMultiplier) {
        super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, meanHeatMult, usefulPartCount, meanEfficiency, sparsityEfficiencyMult);
        this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
    }

    public PebbleFissionUpdatePacket(FissionUpdatePacket fissionUpdatePacket, double meanHeatingSpeedMultiplier) {
        super(fissionUpdatePacket);
        this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PebbleFissionUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        FissionUpdatePacket fissionUpdatePacket = FissionUpdatePacket.fromBytes(buf);
        double meanHeatingSpeedMultiplier = buf.readDouble();
        return new PebbleFissionUpdatePacket(fissionUpdatePacket, meanHeatingSpeedMultiplier);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(meanHeatingSpeedMultiplier);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<FissionReactor, FissionUpdatePacket, PebbleFissionControllerEntity, BlockEntityMenuInfo<PebbleFissionControllerEntity>, PebbleFissionUpdatePacket> {
        public static void handleOnClient(PebbleFissionUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof PebbleFissionControllerEntity entity) {
                    Optional<FissionReactor> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(fissionReactor -> onPacket(payload, fissionReactor));
                }
            });
        }

        protected static void onPacket(PebbleFissionUpdatePacket message, FissionReactor multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}
