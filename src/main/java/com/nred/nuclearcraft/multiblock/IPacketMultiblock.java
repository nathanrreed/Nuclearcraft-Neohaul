package com.nred.nuclearcraft.multiblock;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

public interface IPacketMultiblock extends IMultiblock {
    Set<Player> getMultiblockUpdatePacketListeners();

    CustomPacketPayload getMultiblockUpdatePacket();

    CustomPacketPayload getMultiblockRenderPacket();

    default void sendMultiblockUpdatePacketToAll() {
        if (getLevel().isClientSide) {
            return;
        }
        PacketDistributor.sendToAllPlayers(getMultiblockUpdatePacket());
    }

    default void sendMultiblockUpdatePacketToListeners() {
        if (getLevel().isClientSide) {
            return;
        }

        for (Player player : getMultiblockUpdatePacketListeners()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, getMultiblockUpdatePacket());
        }
    }

    default void sendMultiblockUpdatePacketToPlayer(Player player) {
        if (getLevel().isClientSide) {
            return;
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player, getMultiblockUpdatePacket());
    }

    default void sendRenderPacketToAll() {
        if (getLevel().isClientSide) {
            return;
        }

        PacketDistributor.sendToAllPlayers(getMultiblockRenderPacket());
    }
}