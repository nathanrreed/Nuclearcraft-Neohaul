package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block.fission.SolidFissionControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.block.internal.heat.HeatBuffer;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class SolidFissionUpdatePacket extends FissionUpdatePacket {
    public static final CustomPacketPayload.Type<SolidFissionUpdatePacket> TYPE = new CustomPacketPayload.Type<>(ncLoc("solid_fission_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SolidFissionUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            SolidFissionUpdatePacket::toBytes, SolidFissionUpdatePacket::fromBytes
    );
    public double effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat;

    public SolidFissionUpdatePacket(BlockPos pos, boolean isReactorOn, HeatBuffer heatBuffer, int clusterCount, long cooling, long rawHeating, long totalHeatMult, double meanHeatMult, int fuelComponentCount, long usefulPartCount, double totalEfficiency, double meanEfficiency, double sparsityEfficiencyMult, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) {
        super(pos, isReactorOn, heatBuffer, clusterCount, cooling, rawHeating, totalHeatMult, meanHeatMult, fuelComponentCount, usefulPartCount, totalEfficiency, meanEfficiency, sparsityEfficiencyMult);
        this.effectiveHeating = effectiveHeating;
        this.heatingOutputRateFP = heatingOutputRateFP;
        this.reservedEffectiveHeat = reservedEffectiveHeat;
    }

    public SolidFissionUpdatePacket(FissionUpdatePacket fissionUpdatePacket, double effectiveHeating, double heatingOutputRateFP, double reservedEffectiveHeat) {
        super(fissionUpdatePacket);
        this.effectiveHeating = effectiveHeating;
        this.heatingOutputRateFP = heatingOutputRateFP;
        this.reservedEffectiveHeat = reservedEffectiveHeat;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static SolidFissionUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        FissionUpdatePacket fissionUpdatePacket = FissionUpdatePacket.fromBytes(buf);
        double effectiveHeating = buf.readDouble();
        double heatingOutputRateFP = buf.readDouble();
        double reservedEffectiveHeat = buf.readDouble();

        return new SolidFissionUpdatePacket(fissionUpdatePacket, effectiveHeating, heatingOutputRateFP, reservedEffectiveHeat);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeDouble(effectiveHeating);
        buf.writeDouble(heatingOutputRateFP);
        buf.writeDouble(reservedEffectiveHeat);
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<FissionReactor, FissionUpdatePacket, SolidFissionControllerEntity, TileContainerInfo<SolidFissionControllerEntity>, SolidFissionUpdatePacket> {
        public static void handleOnClient(SolidFissionUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof SolidFissionControllerEntity entity) {
                    Optional<FissionReactor> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(fissionReactor -> onPacket(payload, fissionReactor));
                }
            });
        }

        protected static void onPacket(SolidFissionUpdatePacket message, FissionReactor multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}