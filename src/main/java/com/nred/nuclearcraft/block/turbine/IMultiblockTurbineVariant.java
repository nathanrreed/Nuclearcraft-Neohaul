package com.nred.nuclearcraft.block.turbine;

import it.zerono.mods.zerocore.lib.multiblock.variant.IMultiblockDimensionVariant;

public interface IMultiblockTurbineVariant extends IMultiblockDimensionVariant {
    int getPartFluidCapacity();

    int getMaxFluidCapacity();
}