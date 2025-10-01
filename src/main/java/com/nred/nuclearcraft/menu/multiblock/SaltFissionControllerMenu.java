package com.nred.nuclearcraft.menu.multiblock;

import com.nred.nuclearcraft.block.fission.SaltFissionControllerEntity;
import com.nred.nuclearcraft.menu.AbstractControllerMenu;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

import static com.nred.nuclearcraft.registration.BlockRegistration.FISSION_REACTOR_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.SALT_FISSION_CONTROLLER_MENU_TYPE;

public class SaltFissionControllerMenu extends AbstractControllerMenu<SaltFissionControllerEntity, FissionReactor> {
    public SaltFissionControllerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, final SaltFissionControllerEntity controller) {
        super(SALT_FISSION_CONTROLLER_MENU_TYPE.get(), controller, containerId, inventory, access);
    }

    // Client Constructor
    public SaltFissionControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, FISSION_REACTOR_MAP.get("salt_fuel_fission_controller").get());
    }
}