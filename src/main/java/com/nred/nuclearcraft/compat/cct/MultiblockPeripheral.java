package com.nred.nuclearcraft.compat.cct;

import com.nred.nuclearcraft.block.TilePartAbstract;
import com.nred.nuclearcraft.multiblock.Multiblock;
import dan200.computercraft.api.lua.LuaFunction;

public class MultiblockPeripheral<MULTIBLOCK extends Multiblock<MULTIBLOCK>> {
    public final TilePartAbstract<MULTIBLOCK> computerPort;

    public MultiblockPeripheral(TilePartAbstract<MULTIBLOCK> computerPort) {
        this.computerPort = computerPort;
    }

    protected boolean test() {
        return computerPort.isMachineAssembled() && computerPort.getMultiblockController().isPresent();
    }

    @LuaFunction(mainThread = true)
    public int getLengthX() {
        if (!test()) return 0;
        return computerPort.getMultiblockController().get().getInteriorLengthX();
    }

    @LuaFunction(mainThread = true)
    public int getLengthY() {
        if (!test()) return 0;
        return computerPort.getMultiblockController().get().getInteriorLengthY();
    }

    @LuaFunction(mainThread = true)
    public int getLengthZ() {
        if (!test()) return 0;
        return computerPort.getMultiblockController().get().getInteriorLengthZ();
    }


    @LuaFunction(mainThread = true)
    public void clearAllMaterial() {
        if (test()) {
            computerPort.getMultiblockController().get().clearAllMaterial();
        }
    }

    protected MULTIBLOCK getMultiblock() {
        return computerPort.getMultiblockController().get();
    }
}