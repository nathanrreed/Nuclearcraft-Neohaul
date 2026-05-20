package com.nred.nuclearcraft.menu.multiblock.controller;

import com.nred.nuclearcraft.block_entity.fission.PebbleFissionControllerEntity;
import com.nred.nuclearcraft.handler.BlockEntityMenuInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.PEBBLE_FISSION_CONTROLLER_MENU_TYPE;

public class PebbleFissionControllerMenu extends MultiblockControllerMenu<FissionReactor, FissionUpdatePacket, PebbleFissionControllerEntity, BlockEntityMenuInfo<PebbleFissionControllerEntity>> {
    public PebbleFissionControllerMenu(int containerId, Inventory inventory, PebbleFissionControllerEntity controller) {
        super(PEBBLE_FISSION_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public PebbleFissionControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}