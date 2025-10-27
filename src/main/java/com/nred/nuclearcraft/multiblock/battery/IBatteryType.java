package com.nred.nuclearcraft.multiblock.battery;

import java.util.function.Supplier;

public interface IBatteryType {
    Supplier<Integer> getCapacity();

    Supplier<Integer> getMaxTransfer();
}