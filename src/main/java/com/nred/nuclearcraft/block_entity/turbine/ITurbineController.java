package com.nred.nuclearcraft.block_entity.turbine;


import com.nred.nuclearcraft.multiblock.turbine.ITurbinePart;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.ILogicMultiblockController;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.payload.multiblock.TurbineUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITurbineController<CONTROLLER extends BlockEntity & ITurbineController<CONTROLLER>> extends ITurbinePart, ILogicMultiblockController<Turbine, TurbineUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>> {
    boolean isRenderer();

    void setIsRenderer(boolean isRenderer);
}