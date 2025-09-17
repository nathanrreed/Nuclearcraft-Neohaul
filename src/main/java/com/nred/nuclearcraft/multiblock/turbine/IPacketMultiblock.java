package com.nred.nuclearcraft.multiblock.turbine;

import com.nred.nuclearcraft.multiblock.IMultiblock;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IPacketMultiblock extends IMultiblock {

    CustomPacketPayload getMultiblockUpdatePacket();

    default void sendMultiblockUpdatePacketToAll() {
        if (getLevel().isClientSide) {
            return;
        }
        PacketDistributor.sendToAllPlayers(getMultiblockUpdatePacket());
    }
}