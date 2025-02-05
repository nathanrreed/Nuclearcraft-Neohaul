package com.nred.nuclearcraft.block.collector;

import java.util.Objects;

public enum MACHINE_LEVEL {
    BASE, COMPACT, DENSE;

    @Override
    public String toString() {
        return Objects.equals(super.toString(), "BASE") ? "" : super.toString();
    }
}
