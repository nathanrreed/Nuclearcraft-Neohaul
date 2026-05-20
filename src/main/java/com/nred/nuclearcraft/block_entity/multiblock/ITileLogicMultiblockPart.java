package com.nred.nuclearcraft.block_entity.multiblock;

import com.nred.nuclearcraft.multiblock.ILogicMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.multiblock.MultiblockLogic;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;

public interface ITileLogicMultiblockPart<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>> extends IMultiblockPart<MULTIBLOCK>, IMultitoolLogic {
    default LOGIC getLogic() {
        return getMultiblockController().map(ILogicMultiblock::getLogic).orElse(null);
    }
}