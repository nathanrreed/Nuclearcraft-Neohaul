package com.nred.nuclearcraft.block.passive;

import com.nred.nuclearcraft.block.ITickable;
import com.nred.nuclearcraft.block.energy.ITileEnergy;
import com.nred.nuclearcraft.block.fluid.ITileFluid;
import com.nred.nuclearcraft.block.inventory.ITileInventory;

public interface ITilePassive extends ITickable, ITileEnergy, ITileInventory, ITileFluid {

    double getEnergyRate();

    double getItemRate();

    double getFluidRate();

    boolean canPushEnergyTo();

    boolean canPushItemsTo();

    boolean canPushFluidsTo();
}
