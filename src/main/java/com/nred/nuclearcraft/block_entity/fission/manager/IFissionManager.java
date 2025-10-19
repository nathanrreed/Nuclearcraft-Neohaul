package com.nred.nuclearcraft.block_entity.fission.manager;

import com.nred.nuclearcraft.block_entity.ITileManager;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;

public interface IFissionManager<MANAGER extends IFissionManager<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends ITileManager<FissionReactor, FissionReactorLogic, MANAGER, LISTENER> {
    @Override
    default void refreshMultiblock() {
        getMultiblockController().ifPresent(reactor -> reactor.refreshFlag = true);
    }
}