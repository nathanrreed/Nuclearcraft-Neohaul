package com.nred.nuclearcraft.block_entity.multiblock;

import com.nred.nuclearcraft.multiblock.Multiblock;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockPart;

import java.util.Arrays;

public interface ITileSorptionPart<MULTIBLOCK extends Multiblock<MULTIBLOCK>> extends IMultiblockPart<MULTIBLOCK> {
    boolean canReceive();

    boolean canExtract();

    default boolean canConnect() {
        return !canReceive() && !canExtract();
    }

    SorptionKey getSorptionKey();

    class SorptionKey {
        public final Object[] properties;

        public SorptionKey(Object... properties) {
            this.properties = properties == null ? new Object[]{} : properties;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof SorptionKey other && Arrays.deepEquals(properties, other.properties);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(properties);
        }
    }
}