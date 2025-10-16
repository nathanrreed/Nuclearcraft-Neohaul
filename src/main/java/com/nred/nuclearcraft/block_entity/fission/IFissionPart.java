package com.nred.nuclearcraft.block_entity.fission;

import com.nred.nuclearcraft.multiblock.ITileCuboidalLogicMultiblockPart;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;

import java.util.Optional;

public interface IFissionPart extends ITileCuboidalLogicMultiblockPart<FissionReactor, FissionReactorLogic> {
    default boolean isSimulation() {
        Optional<FissionReactor> reactor = getMultiblockController();
        return reactor.isPresent() && reactor.get().isSimulation;
    }
}