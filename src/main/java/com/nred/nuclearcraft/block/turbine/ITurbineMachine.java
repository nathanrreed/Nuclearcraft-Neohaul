package com.nred.nuclearcraft.block.turbine;

import it.zerono.mods.zerocore.lib.IActivableMachine;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockMachine;

public interface ITurbineMachine extends IMultiblockMachine, IActivableMachine {

    IMultiblockTurbineVariant getVariant();

    // TODO REMOVE OR ADD
//    IFluidContainer getFluidContainer();

    void performOutputCycle();

    void performInputCycle();
}