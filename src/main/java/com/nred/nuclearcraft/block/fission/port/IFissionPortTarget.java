package com.nred.nuclearcraft.block.fission.port;

import com.nred.nuclearcraft.block.ITilePortTarget;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;

public interface IFissionPortTarget<PORT extends IFissionPort<PORT, TARGET>, TARGET extends IFissionPortTarget<PORT, TARGET>> extends ITilePortTarget<FissionReactor, FissionReactorLogic, PORT, TARGET> {
}