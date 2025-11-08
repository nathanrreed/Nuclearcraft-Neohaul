package com.nred.nuclearcraft.block_entity.machine;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.ILogicMultiblockController;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMachineController<CONTROLLER extends BlockEntity & IMachineController<CONTROLLER>> extends IMachinePart, ILogicMultiblockController<Machine, MachineUpdatePacket, CONTROLLER, TileContainerInfo<CONTROLLER>> {
    boolean isRenderer();

    void setIsRenderer(boolean isRenderer);
}