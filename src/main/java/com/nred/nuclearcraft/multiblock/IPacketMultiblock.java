package com.nred.nuclearcraft.multiblock;

import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Set;

public interface IPacketMultiblock<MULTIBLOCK extends Multiblock<MULTIBLOCK>> extends IMultiblockController<MULTIBLOCK> {
    Set<Player> getMultiblockUpdatePacketListeners();

    CustomPacketPayload getMultiblockUpdatePacket();

    CustomPacketPayload getMultiblockRenderPacket();

    default void sendMultiblockUpdatePacketToAll() {
        if (getWorld().isClientSide) {
            return;
        }
        PacketDistributor.sendToAllPlayers(getMultiblockUpdatePacket());
    }

    default void sendMultiblockUpdatePacketToListeners() {
        if (getWorld().isClientSide) {
            return;
        }

        for (Player player : getMultiblockUpdatePacketListeners()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, getMultiblockUpdatePacket());
        }
    }

    default void sendMultiblockUpdatePacketToPlayer(Player player) {
        if (getWorld().isClientSide) {
            return;
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player, getMultiblockUpdatePacket());
    }

    default void sendRenderPacketToAll() {
        if (getWorld().isClientSide) {
            return;
        }

        PacketDistributor.sendToAllPlayers(getMultiblockRenderPacket());
    }
}