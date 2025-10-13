package com.nred.nuclearcraft.block;

import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import net.minecraft.core.BlockPos;

public interface ITilePortTarget<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, PORT extends ITilePort<MULTIBLOCK, LOGIC, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC> {

    BlockPos getMasterPortPos();

    void setMasterPortPos(BlockPos pos);

    void clearMasterPort();

    void refreshMasterPort();

    void onPortRefresh();
}