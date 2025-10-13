package com.nred.nuclearcraft.screen.multiblock.controller;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.multiblock.ContainerMultiblockController;
import com.nred.nuclearcraft.multiblock.*;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class GuiLogicMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK> & ILogicMultiblock<MULTIBLOCK, LOGIC> & IPacketMultiblock<MULTIBLOCK, PACKET>, LOGIC extends MultiblockLogic<MULTIBLOCK, LOGIC>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends BlockEntity & IMultiblockGuiPart<MULTIBLOCK, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>, L extends LOGIC, MENU extends ContainerMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO>> extends GuiMultiblockController<MULTIBLOCK, PACKET, CONTROLLER, INFO, MENU> {
    protected final LOGIC logic;

    public GuiLogicMultiblockController(MENU menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        logic = multiblock.getLogic();
    }

    @SuppressWarnings("unchecked")
    protected L getLogic() {
        return (L) logic;
    }
}