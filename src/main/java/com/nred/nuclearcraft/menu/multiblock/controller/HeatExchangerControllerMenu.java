package com.nred.nuclearcraft.menu.multiblock.controller;

import com.nred.nuclearcraft.block_entity.hx.HeatExchangerControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.hx.HeatExchanger;
import com.nred.nuclearcraft.payload.multiblock.HeatExchangerUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.HX_CONTROLLER_MENU_TYPE;

public class HeatExchangerControllerMenu extends MultiblockControllerMenu<HeatExchanger, HeatExchangerUpdatePacket, HeatExchangerControllerEntity, TileContainerInfo<HeatExchangerControllerEntity>> {
    public HeatExchangerControllerMenu(int containerId, Inventory inventory, final HeatExchangerControllerEntity controller) {
        super(HX_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public HeatExchangerControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}