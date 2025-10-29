package com.nred.nuclearcraft.payload.multiblock;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
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

public class HeatExchangerUpdatePacket extends MultiblockUpdatePacket {
    public static final Type<HeatExchangerUpdatePacket> TYPE = new Type<>(ncLoc("heat_exchanger_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HeatExchangerUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            HeatExchangerUpdatePacket::toBytes, HeatExchangerUpdatePacket::fromBytes
    );

    public boolean isExchangerOn;
    public int totalNetworkCount;
    public int activeNetworkCount;
    public int activeTubeCount;
    public int activeContactCount;
    public double tubeInputRateFP;
    public double shellInputRateFP;
    public double heatTransferRateFP;
    public double totalTempDiff;

    public HeatExchangerUpdatePacket(BlockPos pos, boolean isExchangerOn, int totalNetworkCount, int activeNetworkCount, int activeTubeCount, int activeContactCount, double tubeInputRateFP, double shellInputRateFP, double heatTransferRateFP, double totalTempDiff) {
        super(pos);
        this.isExchangerOn = isExchangerOn;
        this.totalNetworkCount = totalNetworkCount;
        this.activeNetworkCount = activeNetworkCount;
        this.activeTubeCount = activeTubeCount;
        this.activeContactCount = activeContactCount;
        this.tubeInputRateFP = tubeInputRateFP;
        this.shellInputRateFP = shellInputRateFP;
        this.heatTransferRateFP = heatTransferRateFP;
        this.totalTempDiff = totalTempDiff;
    }

    public HeatExchangerUpdatePacket(MultiblockUpdatePacket multiblockUpdatePacket, boolean isExchangerOn, int totalNetworkCount, int activeNetworkCount, int activeTubeCount, int activeContactCount, double tubeInputRateFP, double shellInputRateFP, double heatTransferRateFP, double totalTempDiff) {
        super(multiblockUpdatePacket);
        this.isExchangerOn = isExchangerOn;
        this.totalNetworkCount = totalNetworkCount;
        this.activeNetworkCount = activeNetworkCount;
        this.activeTubeCount = activeTubeCount;
        this.activeContactCount = activeContactCount;
        this.tubeInputRateFP = tubeInputRateFP;
        this.shellInputRateFP = shellInputRateFP;
        this.heatTransferRateFP = heatTransferRateFP;
        this.totalTempDiff = totalTempDiff;
    }

    public static HeatExchangerUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        MultiblockUpdatePacket multiblockUpdatePacket = MultiblockUpdatePacket.fromBytes(buf);
        boolean isExchangerOn = buf.readBoolean();
        int totalNetworkCount = buf.readInt();
        int activeNetworkCount = buf.readInt();
        int activeTubeCount = buf.readInt();
        int activeContactCount = buf.readInt();
        double tubeInputRateFP = buf.readDouble();
        double shellInputRateFP = buf.readDouble();
        double heatTransferRateFP = buf.readDouble();
        double totalTempDiff = buf.readDouble();

        return new HeatExchangerUpdatePacket(multiblockUpdatePacket, isExchangerOn, totalNetworkCount, activeNetworkCount, activeTubeCount, activeContactCount, tubeInputRateFP, shellInputRateFP, heatTransferRateFP, totalTempDiff);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isExchangerOn);
        buf.writeInt(totalNetworkCount);
        buf.writeInt(activeNetworkCount);
        buf.writeInt(activeTubeCount);
        buf.writeInt(activeContactCount);
        buf.writeDouble(tubeInputRateFP);
        buf.writeDouble(shellInputRateFP);
        buf.writeDouble(heatTransferRateFP);
        buf.writeDouble(totalTempDiff);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler extends MultiblockUpdatePacket.Handler<HeatExchanger, HeatExchangerUpdatePacket, HeatExchangerControllerEntity, TileContainerInfo<HeatExchangerControllerEntity>, HeatExchangerUpdatePacket> {
        public static void handleOnClient(HeatExchangerUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof HeatExchangerControllerEntity entity) {
                    Optional<HeatExchanger> multiblock = entity.getMultiblockController();
                    multiblock.ifPresent(heatExchanger -> onPacket(payload, heatExchanger));
                }
            });
        }

        protected static void onPacket(HeatExchangerUpdatePacket message, HeatExchanger multiblock) {
            multiblock.onMultiblockUpdatePacket(message);
        }
    }
}