package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public interface IMultiblockGuiPart<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, GUITILE extends BlockEntity & IMultiblockGuiPart<MULTIBLOCK, PACKET, GUITILE, INFO>, INFO extends TileContainerInfo<GUITILE>> extends IMultiblockPart<MULTIBLOCK>, ITileGui<GUITILE, PACKET, INFO> {
    default void sendTileUpdatePacketToPlayer(Player player) {
        if (!getCurrentWorld().isClientSide && getMultiblockController().isPresent()) {
            getMultiblockController().get().sendMultiblockUpdatePacketToPlayer(player);
        }
    }

    default void sendTileUpdatePacketToAll() {
        if (getMultiblockController().isPresent()) {
            getMultiblockController().get().sendMultiblockUpdatePacketToAll();
        }
    }

    default Set<Player> getTileUpdatePacketListeners() {
        return getMultiblockController().isEmpty() ? null : getMultiblockController().get().getMultiblockUpdatePacketListeners();
    }

    default void addBEUpdatePacketListener(Player player) {
        if (getMultiblockController().isPresent()) {
            getTileUpdatePacketListeners().add(player);
            sendTileUpdatePacketToPlayer(player);
        }
    }

    default void removeTileUpdatePacketListener(Player player) {
        if (getMultiblockController().isPresent()) {
            getTileUpdatePacketListeners().remove(player);
        }
    }

    default void sendTileUpdatePacketToListeners() {
        if (getMultiblockController().isPresent()) {
            getMultiblockController().get().sendMultiblockUpdatePacketToListeners();
        }
    }
}