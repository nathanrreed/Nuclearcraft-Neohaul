package com.nred.nuclearcraft.block.turbine;


import net.minecraft.core.BlockPos;

public interface ITurbineController<CONTROLLER extends AbstractTurbineEntity & ITurbineController<CONTROLLER>> {

    boolean isRenderer();

    void setIsRenderer(boolean isRenderer);

    BlockPos getPos();

    void setActiveState(boolean value);
}
