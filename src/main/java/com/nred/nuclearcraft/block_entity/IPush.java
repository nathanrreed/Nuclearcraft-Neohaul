package com.nred.nuclearcraft.block_entity;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;

import java.util.Collection;

public interface IPush {
    default Collection<Direction> getDirections() {
        return Direction.allShuffled(RandomSource.create());
    }

    void onCapInvalidate();
}