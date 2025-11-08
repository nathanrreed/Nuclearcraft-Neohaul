package com.nred.nuclearcraft.menu.multiblock.controller;

import com.nred.nuclearcraft.block_entity.machine.ElectrolyzerControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.ELECTROLYZER_CONTROLLER_MENU_TYPE;

public class ElectrolyzerControllerMenu extends MultiblockControllerMenu<Machine, MachineUpdatePacket, ElectrolyzerControllerEntity, TileContainerInfo<ElectrolyzerControllerEntity>> {
    public ElectrolyzerControllerMenu(int containerId, Inventory inventory, final ElectrolyzerControllerEntity controller) {
        super(ELECTROLYZER_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public ElectrolyzerControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}