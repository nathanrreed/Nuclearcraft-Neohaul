package com.nred.nuclearcraft.block_entity.fission;

public interface IFissionCoolingComponent extends IFissionComponent {
    long getCooling(boolean simulate);
}