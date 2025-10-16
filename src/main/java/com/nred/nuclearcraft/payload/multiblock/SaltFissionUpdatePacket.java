package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.block_entity.internal.heat.HeatBuffer;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class SaltFissionUpdatePacket extends FissionUpdatePacket {
    public static final CustomPacketPayload.Type<SaltFissionUpdatePacket> TYPE = new CustomPacketPayload.Type<>(ncLoc("salt_fission_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SaltFissionUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            SaltFissionUpdatePacket::toBytes, SaltFissionUpdatePacket::fromBytes
    );
    public double meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier;

    public SaltFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double meanHeatingSpeedMultiplier, double totalHeatingSpeedMultiplier) {
        super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult);
        this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
        this.totalHeatingSpeedMultiplier = totalHeatingSpeedMultiplier;
    }

    public SaltFissionUpdatePacket(FissionUpdatePacket fissionUpdatePacket, double meanHeatingSpeedMultiplier, double totalHeatingSpeedMultiplier) {
        super(fissionUpdatePacket);
        this.meanHeatingSpeedMultiplier = meanHeatingSpeedMultiplier;
        this.totalHeatingSpeedMultiplier = totalHeatingSpeedMultiplier;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static SaltFissionUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        FissionUpdatePacket fissionUpdatePacket = FissionUpdatePacket.fromBytes(buf);
        double meanHeatingSpeedMultiplier = buf.readDouble();
        double totalHeatingSpeedMultiplier = buf.readDouble();
        return new SaltFissionUpdatePacket(fissionUpdatePacket, meanHeatingSpeedMultiplier, totalHeatingSpeedMultiplier);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(meanHeatingSpeedMultiplier);
        buf.writeDouble(totalHeatingSpeedMultiplier);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<FissionReactor, FissionUpdatePacket, SaltFissionControllerEntity, TileContainerInfo<SaltFissionControllerEntity>, SaltFissionUpdatePacket> {
        public static void handleOnClient(SaltFissionUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof SaltFissionControllerEntity entity) {
                    Optional<FissionReactor> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(fissionReactor -> onPacket(payload, fissionReactor));
                }
            });
        }

        protected static void onPacket(SaltFissionUpdatePacket message, FissionReactor multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}