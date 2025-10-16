package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.world.entity.player.Player;

public interface ITilePacket<PACKET extends NCPacket> extends ITile {
    PACKET getTileUpdatePacket();

    void onTileUpdatePacket(PACKET message);

    default void sendTileUpdatePacketToPlayer(Player player) {
        if (!getTileWorld().isClientSide) {
            getTileUpdatePacket().sendTo(player);
        }
    }

    default void sendTileUpdatePacketToAll() {
        getTileUpdatePacket().sendToAll();
    }
}
