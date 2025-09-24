package com.nred.nuclearcraft.menu.turbine;

import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.menu.AbstractControllerMenu;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.TURBINE_CONTROLLER_MENU_TYPE;

public class TurbineControllerMenu extends AbstractControllerMenu {
    public final TurbineControllerEntity controller;

    public TurbineControllerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, final TurbineControllerEntity controller) {
        super(TURBINE_CONTROLLER_MENU_TYPE.get(), containerId, inventory, access);
        this.controller = controller;

        controller.addTileUpdatePacketListener(inventory.player);
    }

    // Client Constructor
    public TurbineControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, TURBINE_MAP.get("turbine_controller").get());
    }

    @Override
    public void removed(Player player) {
        controller.addTileUpdatePacketListener(inventory.player);
    }
}