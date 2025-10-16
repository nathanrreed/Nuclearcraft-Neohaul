package com.nred.nuclearcraft.block_entity;

public interface ITileFiltered extends ITile {
    boolean canModifyFilter(int slot);

    void onFilterChanged(int slot);

    Object getFilterKey();
}