package com.nred.nuclearcraft.block.fission;


import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Iterator;

public interface IFissionController<CONTROLLER extends BlockEntity & IFissionController<CONTROLLER>> {
    void doMeltdown(Iterator<IFissionController<?>> controllerIterator);

    CustomPacketPayload getMultiblockUpdatePacket();
}
