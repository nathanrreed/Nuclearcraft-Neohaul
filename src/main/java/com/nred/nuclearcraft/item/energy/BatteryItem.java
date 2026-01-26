package com.nred.nuclearcraft.item.energy;

import com.nred.nuclearcraft.block_entity.internal.energy.EnergyConnection;

import java.util.function.Supplier;

public class BatteryItem extends EnergyItem {
    public BatteryItem(Properties properties, Supplier<Integer> capacity, Supplier<Integer> rate) {
        super(properties, capacity, rate, EnergyConnection.BOTH);
    }
}