package com.nred.nuclearcraft.block_entity.hx;

import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.multiblock.ILogicMultiblockController;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IHeatExchangerController<CONTROLLER extends BlockEntity & IHeatExchangerController<CONTROLLER>> extends IHeatExchangerPart, ILogicMultiblockController<HeatExchanger, HeatExchangerUpdatePacket, CONTROLLER, BlockEntityMenuInfo<CONTROLLER>> {
    boolean isRenderer();

    void setIsRenderer(boolean isRenderer);
}