package com.nred.nuclearcraft.property;

import net.minecraft.util.StringRepresentable;

public enum MachinePortSorption implements StringRepresentable {
    ITEM_IN("item_in"),
    FLUID_IN("fluid_in"),
    ITEM_OUT("item_out"),
    FLUID_OUT("fluid_out");

    private final String name;

    MachinePortSorption(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}