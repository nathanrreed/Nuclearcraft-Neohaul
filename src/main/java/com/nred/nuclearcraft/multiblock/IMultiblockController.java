package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public interface IMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & IMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>> extends IMultiblockGuiPart<MULTIBLOCK, PACKET, CONTROLLER, INFO> {
    @Override
    default PACKET getTileUpdatePacket() {
        Optional<MULTIBLOCK> multiblock = getMultiblockController();
        return multiblock.map(IPacketMultiblock::getMultiblockUpdatePacket).orElse(null);
    }

    @Override
    default void onTileUpdatePacket(PACKET message) {
        Optional<MULTIBLOCK> multiblock = getMultiblockController();
        multiblock.ifPresent(value -> value.onMultiblockUpdatePacket(message));
    }
}