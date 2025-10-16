package com.nred.nuclearcraft.block_entity;

import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;

public interface ITilePort<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, PORT extends ITilePort<MULTIBLOCK, LOGIC, PORT, TARGET>, TARGET extends ITilePortTarget<MULTIBLOCK, LOGIC, PORT, TARGET>> extends ITileLogicMultiblockPart<MULTIBLOCK, LOGIC> {
    ObjectSet<TARGET> getTargets();

    BlockPos getMasterPortPos();

    void setMasterPortPos(BlockPos pos);

    void clearMasterPort();

    void refreshMasterPort();

    void refreshTargets();

    void setRefreshTargetsFlag(boolean refreshTargetsFlag);

    default int getInventoryStackLimitPerConnection() {
        return 2;
    }

    void setInventoryStackLimit(int stackLimit);

    int getTankCapacityPerConnection();

    int getTankBaseCapacity();

    void setTankCapacity(int capacity);
}