package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.multiblock.ITileCuboidalLogicMultiblockPart;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.multiblock.machine.MachineLogic;

public interface IMachinePart extends ITileCuboidalLogicMultiblockPart<Machine, MachineLogic> {
    default void refreshMachineRecipe() {
        MachineLogic logic = getLogic();
        if (logic != null) {
            logic.refreshRecipe(getTileWorld());
        }
    }

    default void refreshMachineActivity() {
        MachineLogic logic = getLogic();
        if (logic != null) {
            logic.refreshActivity();
        }
    }
}