package com.nred.nuclearcraft.menu.multiblock.controller;

import com.nred.nuclearcraft.block_entity.fission.SolidFissionControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.SolidFuelFissionLogic;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.SOLID_FISSION_CONTROLLER_MENU_TYPE;

public class SolidFissionControllerMenu extends MultiblockControllerMenu<FissionReactor, FissionUpdatePacket, SolidFissionControllerEntity, TileContainerInfo<SolidFissionControllerEntity>> {
    public final SolidFuelFissionLogic logic = (SolidFuelFissionLogic) this.tile.getLogic();

    public SolidFissionControllerMenu(int containerId, Inventory inventory, SolidFissionControllerEntity controller) {
        super(SOLID_FISSION_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public SolidFissionControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}