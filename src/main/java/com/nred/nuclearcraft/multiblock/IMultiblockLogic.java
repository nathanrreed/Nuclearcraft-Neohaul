package com.nred.nuclearcraft.multiblock;

import net.minecraft.world.level.Level;

public interface IMultiblockLogic<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>> {
    String getID();

    Level getLevel();
}