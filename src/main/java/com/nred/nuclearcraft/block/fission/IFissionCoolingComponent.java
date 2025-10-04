package com.nred.nuclearcraft.block.fission;

public interface IFissionCoolingComponent extends IFissionComponent {
    long getCooling(boolean simulate);
}