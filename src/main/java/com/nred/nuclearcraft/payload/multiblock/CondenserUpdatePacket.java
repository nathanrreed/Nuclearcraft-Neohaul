package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.hx.CondenserControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class CondenserUpdatePacket extends HeatExchangerUpdatePacket {
    public static final Type<CondenserUpdatePacket> TYPE = new Type<>(ncLoc("condenser_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CondenserUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            CondenserUpdatePacket::toBytes, CondenserUpdatePacket::fromBytes
    );

    public CondenserUpdatePacket(BlockPos pos, boolean isExchangerOn, int totalNetworkCount, int activeNetworkCount, int activeTubeCount, int activeContactCount, double tubeInputRateFP, double shellInputRateFP, double heatTransferRateFP, double totalTempDiff) {
        super(pos, isExchangerOn, totalNetworkCount, activeNetworkCount, activeTubeCount, activeContactCount, tubeInputRateFP, shellInputRateFP, heatTransferRateFP, totalTempDiff);
    }

    public CondenserUpdatePacket(HeatExchangerUpdatePacket hx) {
        super(hx.pos, hx.isExchangerOn, hx.totalNetworkCount, hx.activeNetworkCount, hx.activeTubeCount, hx.activeContactCount, hx.tubeInputRateFP, hx.shellInputRateFP, hx.heatTransferRateFP, hx.totalTempDiff);
    }

    public static CondenserUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        return new CondenserUpdatePacket(HeatExchangerUpdatePacket.fromBytes(buf));
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, HeatExchangerUpdatePacket, CondenserControllerEntity, TileContainerInfo<CondenserControllerEntity>, CondenserUpdatePacket> {
        public static void handleOnClient(CondenserUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof CondenserControllerEntity entity) {
                    Optional<HeatExchanger> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(condenser -> onPacket(payload, condenser));
                }
            });
        }

        protected static void onPacket(CondenserUpdatePacket message, HeatExchanger multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}