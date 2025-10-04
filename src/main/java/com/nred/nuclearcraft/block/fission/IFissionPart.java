package com.nred.nuclearcraft.block.fission;

import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;

import java.util.Optional;

public interface IFissionPart extends IMultiblockPart<FissionReactor> {
    default boolean isSimulation() {
        Optional<FissionReactor> reactor = getMultiblockController();
        return reactor.isPresent() && reactor.get().isSimulation;
    }

    default FissionReactorLogic getLogic() {
        return getMultiblockController().get().logic;
    }
}
