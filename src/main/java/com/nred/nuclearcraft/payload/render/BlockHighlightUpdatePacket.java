package com.nred.nuclearcraft.payload.render;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class BlockHighlightUpdatePacket extends NCPacket {
    public static final Type<BlockHighlightUpdatePacket> TYPE = new Type<>(ncLoc("block_highlight_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockHighlightUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            BlockHighlightUpdatePacket::toBytes, BlockHighlightUpdatePacket::fromBytes
    );
    protected long posLong, highlightTimeMillis;

    public BlockHighlightUpdatePacket(BlockPos pos, long highlightTimeMillis) {
        this.posLong = pos.asLong();
        this.highlightTimeMillis = highlightTimeMillis;
    }

    public BlockHighlightUpdatePacket(long posLong, long highlightTimeMillis) {
        this.posLong = posLong;
        this.highlightTimeMillis = highlightTimeMillis;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static BlockHighlightUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        long posLong = buf.readLong();
        long highlightTimeMillis = buf.readLong();
        return new BlockHighlightUpdatePacket(posLong, highlightTimeMillis);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeLong(posLong);
        buf.writeLong(highlightTimeMillis);
    }

    public static class Handler {
        public static void handleOnClient(BlockHighlightUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                NuclearcraftNeohaul.blockOverlayTracker.highlightBlock(payload.posLong, payload.highlightTimeMillis);
            });
        }
    }
}
