package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.block.ITilePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileUpdatePacket extends NCPacket {
    public BlockPos pos;

    public TileUpdatePacket(BlockPos pos) {
        this.pos = pos;
    }

    public TileUpdatePacket(TileUpdatePacket tileUpdatePacket) {
        this.pos = tileUpdatePacket.pos;
    }

    public static TileUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        return new TileUpdatePacket(BlockPos.STREAM_CODEC.decode(buf));
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        super.toBytes(buf);
        BlockPos.STREAM_CODEC.encode(buf, pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        throw new RuntimeException("Raw use of TileUpdatePacket");
    }

    public static abstract class Handler<MESSAGE extends TileUpdatePacket, TILE extends ITilePacket<MESSAGE>> {
        @SuppressWarnings("unchecked")
        public static void handleOnClient(TileUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                BlockEntity tile = context.player().level().getBlockEntity(payload.pos);
                if (tile instanceof ITilePacket) {
                    ((ITilePacket<TileUpdatePacket>) tile).onTileUpdatePacket(payload);
                }
            });
        }
    }
}
