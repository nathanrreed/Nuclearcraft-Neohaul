package com.nred.nuclearcraft.menu.multiblock;

import com.nred.nuclearcraft.block.fission.SolidFissionControllerEntity;
import com.nred.nuclearcraft.menu.AbstractControllerMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.solid.SolidFuelFissionLogic;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.SOLID_FISSION_CONTROLLER_MENU_TYPE;

public class SolidFissionControllerMenu extends AbstractControllerMenu<SolidFissionControllerEntity, FissionReactor> {
    public final SolidFuelFissionLogic logic = (SolidFuelFissionLogic) this.controller.getLogic();

    public SolidFissionControllerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, final SolidFissionControllerEntity controller) {
        super(SOLID_FISSION_CONTROLLER_MENU_TYPE.get(), controller, containerId, inventory, access);
    }

    // Client Constructor
    public SolidFissionControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, FISSION_REACTOR_MAP.get("solid_fuel_fission_controller").get());
    }
}