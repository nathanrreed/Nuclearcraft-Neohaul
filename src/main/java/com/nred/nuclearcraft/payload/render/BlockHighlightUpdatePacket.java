package com.nred.nuclearcraft.payload.render;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.NuclearcraftNeohaul;
import com.nred.nuclearcraft.payload.NCPacket;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Collection;
import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class BlockHighlightUpdatePacket extends NCPacket {
    public static final Type<BlockHighlightUpdatePacket> TYPE = new Type<>(ncLoc("block_highlight_update_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockHighlightUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(
            BlockHighlightUpdatePacket::toBytes, BlockHighlightUpdatePacket::fromBytes
    );

    protected LongCollection posLongCollection;
    protected long highlightTimeMillis;

    public BlockHighlightUpdatePacket(LongCollection posLongCollection, long highlightTimeMillis) {
        this.posLongCollection = posLongCollection == null ? LongSets.EMPTY_SET : posLongCollection;
        this.highlightTimeMillis = highlightTimeMillis;
    }

    public BlockHighlightUpdatePacket(long posLong, long highlightTimeMillis) {
        this(LongSets.singleton(posLong), highlightTimeMillis);
    }

    public BlockHighlightUpdatePacket(Collection<BlockPos> posCollection, long highlightTimeMillis) {
        this(new LongOpenHashSet(posCollection.stream().filter(Objects::nonNull).mapToLong(BlockPos::asLong).iterator()), highlightTimeMillis);
    }

    public BlockHighlightUpdatePacket(BlockPos pos, long highlightTimeMillis) {
        this(pos == null ? null : Lists.newArrayList(pos), highlightTimeMillis);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static BlockHighlightUpdatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        LongCollection posLongCollection = readLongs(buf);
        long highlightTimeMillis = buf.readLong();
        return new BlockHighlightUpdatePacket(posLongCollection, highlightTimeMillis);
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        writeLongs(buf, posLongCollection);
        buf.writeLong(highlightTimeMillis);
    }

    public static class Handler {
        public static void handleOnClient(BlockHighlightUpdatePacket payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                NuclearcraftNeohaul.blockOverlayTracker.highlightBlocks(payload.posLongCollection, payload.highlightTimeMillis);
            });
        }
    }
}
