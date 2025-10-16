package com.nred.nuclearcraft.block_entity.fission;


import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.ILogicMultiblockController;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Iterator;

public interface IFissionController<CONTROLLER extends BlockEntity & IFissionController<CONTROLLER>> extends IFissionPart, ILogicMultiblockController<FissionReactor, FissionUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>> {
    void doMeltdown(Iterator<IFissionController<?>> controllerIterator);
}