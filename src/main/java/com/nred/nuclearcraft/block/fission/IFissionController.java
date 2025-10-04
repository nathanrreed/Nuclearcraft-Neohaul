package com.nred.nuclearcraft.block.fission;


import com.nred.nuclearcraft.multiblock.ILogicMultiblockController;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Iterator;

public interface IFissionController<CONTROLLER extends BlockEntity & IFissionController<CONTROLLER>> extends ILogicMultiblockController<FissionReactor> {
    void doMeltdown(Iterator<IFissionController<?>> controllerIterator);

    void setActiveState(boolean value);

    BlockPos getPos();
}
