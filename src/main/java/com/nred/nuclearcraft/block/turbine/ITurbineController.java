package com.nred.nuclearcraft.block.turbine;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITurbineController<CONTROLLER extends BlockEntity & ITurbineController<CONTROLLER>> {

    boolean isRenderer();

    void setIsRenderer(boolean isRenderer);

    BlockPos getPos();

    void setActiveState(boolean value);
}
