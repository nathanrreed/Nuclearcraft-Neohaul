package com.nred.nuclearcraft.multiblock;

import com.nred.nuclearcraft.block.ITileLogicMultiblockPart;

public interface ITileCuboidalLogicMultiblockPart<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC> {
}