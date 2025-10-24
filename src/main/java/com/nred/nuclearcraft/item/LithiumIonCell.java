package com.nred.nuclearcraft.item;

import static com.nred.nuclearcraft.config.Config.LITHIUM_ION_CELL_CAPACITY;
import static com.nred.nuclearcraft.config.Config.LITHIUM_ION_CELL_TRANSFER;

public class LithiumIonCell extends EnergyItem { // TODO REDO
    public LithiumIonCell(Properties properties) {
        super(properties, () -> LITHIUM_ION_CELL_CAPACITY, () -> LITHIUM_ION_CELL_TRANSFER);
    }
}