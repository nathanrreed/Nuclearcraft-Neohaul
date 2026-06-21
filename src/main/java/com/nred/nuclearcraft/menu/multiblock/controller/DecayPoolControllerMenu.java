package com.nred.nuclearcraft.menu.multiblock.controller;


import com.nred.nuclearcraft.block_entity.machine.DecayPoolControllerEntity;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.multiblock.machine.Machine;
import com.nred.nuclearcraft.payload.multiblock.MachineUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.DECAY_POOL_CONTROLLER_MENU_TYPE;

public class DecayPoolControllerMenu extends MultiblockControllerMenu<Machine, MachineUpdatePacket, DecayPoolControllerEntity, BlockEntityMenuInfo<DecayPoolControllerEntity>> {
    public DecayPoolControllerMenu(int containerId, Inventory inventory, final DecayPoolControllerEntity controller) {
        super(DECAY_POOL_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public DecayPoolControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}