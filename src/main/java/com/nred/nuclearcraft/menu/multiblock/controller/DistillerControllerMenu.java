package com.nred.nuclearcraft.menu.multiblock.controller;

import com.nred.nuclearcraft.block_entity.machine.DistillerControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.DISTILLER_CONTROLLER_MENU_TYPE;

public class DistillerControllerMenu extends MultiblockControllerMenu<Machine, MachineUpdatePacket, DistillerControllerEntity, TileContainerInfo<DistillerControllerEntity>> {
    public DistillerControllerMenu(int containerId, Inventory inventory, final DistillerControllerEntity controller) {
        super(DISTILLER_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public DistillerControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}