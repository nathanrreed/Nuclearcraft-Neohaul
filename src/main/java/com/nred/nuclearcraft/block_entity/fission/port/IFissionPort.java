package com.nred.nuclearcraft.block_entity.fission.port;

import com.nred.nuclearcraft.block_entity.fission.IFissionSpecialPart;
import com.nred.nuclearcraft.block_entity.multiblock.port.ITilePort;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;

public interface IFissionPort<PORT extends IFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends ITilePort<FissionReactor, FissionReactorLogic, PORT, TARGET>, IFissionSpecialPart {
    default void postClusterSearch(boolean simulate) {
        if (!simulate) {
            refreshTargets();
        }
    }
}