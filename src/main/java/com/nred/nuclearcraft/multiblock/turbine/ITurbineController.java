package com.nred.nuclearcraft.multiblock.turbine;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITurbineController<CONTROLLER extends BlockEntity & ITurbineController<CONTROLLER>> {

    boolean isRenderer();

    void setIsRenderer(boolean isRenderer);

    BlockPos getPos();
}
