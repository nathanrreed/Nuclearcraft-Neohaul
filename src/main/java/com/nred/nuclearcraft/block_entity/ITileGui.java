package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public interface ITileGui<TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends NCPacket, INFO extends TileContainerInfo<TILE>> extends ITilePacket<PACKET>, MenuProvider {
    INFO getContainerInfo();

    Set<Player> getTileUpdatePacketListeners();

    default void addTileUpdatePacketListener(Player player) {
        getTileUpdatePacketListeners().add(player);
        sendTileUpdatePacketToPlayer(player);
    }

    default void removeTileUpdatePacketListener(Player player) {
        getTileUpdatePacketListeners().remove(player);
    }

    default void sendTileUpdatePacketToListeners() {
        getTileUpdatePacket().sendTo(getTileUpdatePacketListeners());
    }
}