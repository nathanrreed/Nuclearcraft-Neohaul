package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & IMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends BlockEntityMenuInfo<CONTROLLER>> extends IMultiblockScreenPart<MULTIBLOCK, PACKET, CONTROLLER, INFO> {
    @Override
    default PACKET getTileUpdatePacket() {
        return getMultiblockController().map(IPacketMultiblock::getMultiblockUpdatePacket).orElse(null);
    }

    @Override
    default void onTileUpdatePacket(PACKET message) {
        getMultiblockController().ifPresent(value -> value.onMultiblockUpdatePacket(message));
    }
}