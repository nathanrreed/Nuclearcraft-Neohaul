package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ILogicMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & ILogicMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends BlockEntityMenuInfo<CONTROLLER>> extends IMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO> {
    String getLogicID();
}