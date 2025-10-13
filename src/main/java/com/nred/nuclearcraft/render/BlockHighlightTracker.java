package com.nred.nuclearcraft.render;

import com.nred.nuclearcraft.payload.render.BlockHighlightUpdatePacket;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class BlockHighlightTracker {
    private final Long2LongMap highlightMap = new Long2LongOpenHashMap();

    public void highlightBlock(long posLong, long highlightTimeMillis) {
        highlightMap.put(posLong, System.currentTimeMillis() + highlightTimeMillis);
    }

    public Long2LongMap getHighlightMap() {
        return highlightMap;
    }

    public static void sendPacket(ServerPlayer player, BlockPos pos, long highlightTimeMillis) {
        new BlockHighlightUpdatePacket(pos, highlightTimeMillis).sendTo(player);
    }

    public static void sendPacket(ServerPlayer player, long posLong, long highlightTimeMillis) {
        sendPacket(player, BlockPos.of(posLong), highlightTimeMillis);
    }

    public static void sendPacketToAll(BlockPos pos, long highlightTimeMillis) {
        new BlockHighlightUpdatePacket(pos, highlightTimeMillis).sendToAll();
    }

    public static void sendPacketToAll(long posLong, long highlightTimeMillis) {
        sendPacketToAll(BlockPos.of(posLong), highlightTimeMillis);
    }
}