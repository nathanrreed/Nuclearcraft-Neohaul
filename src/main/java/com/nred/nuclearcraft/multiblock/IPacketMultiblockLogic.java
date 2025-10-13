package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;

public interface IPacketMultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, PACKET extends MultiblockUpdatePacket> extends IMultiblockLogic<MULTIBLOCK, LOGIC> {
    PACKET getMultiblockUpdatePacket();

    void onMultiblockUpdatePacket(PACKET message);
}