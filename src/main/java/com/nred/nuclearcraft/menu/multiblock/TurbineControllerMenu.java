package com.nred.nuclearcraft.menu.multiblock;

import com.nred.nuclearcraft.block_entity.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import com.nred.nuclearcraft.payload.multiblock.TurbineUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.TURBINE_CONTROLLER_MENU_TYPE;

public class TurbineControllerMenu extends MultiblockControllerMenu<Turbine, TurbineUpdatePacket, TurbineControllerEntity, TileContainerInfo<TurbineControllerEntity>> {
    public TurbineControllerMenu(int containerId, Inventory inventory, final TurbineControllerEntity controller) {
        super(TURBINE_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public TurbineControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}