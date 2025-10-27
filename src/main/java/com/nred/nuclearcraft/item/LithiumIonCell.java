package com.nred.nuclearcraft.item;

import static com.nred.nuclearcraft.config.NCConfig.*;

public class LithiumIonCell extends EnergyItem { // TODO REDO
    public LithiumIonCell(Properties properties) {
        super(properties, () -> battery_item_capacity[0], () -> battery_item_max_transfer[0]);
    }
}