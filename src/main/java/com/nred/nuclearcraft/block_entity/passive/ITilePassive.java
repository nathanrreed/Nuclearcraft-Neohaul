package com.nred.nuclearcraft.block_entity.passive;

import com.nred.nuclearcraft.block_entity.ITickable;
import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.fluid.ITileFluid;
import com.nred.nuclearcraft.block_entity.inventory.ITileInventory;

public interface ITilePassive extends ITickable, ITileEnergy, ITileInventory, ITileFluid {

    double getEnergyRate();

    double getItemRate();

    double getFluidRate();

    boolean canPushEnergyTo();

    boolean canPushItemsTo();

    boolean canPushFluidsTo();
}
