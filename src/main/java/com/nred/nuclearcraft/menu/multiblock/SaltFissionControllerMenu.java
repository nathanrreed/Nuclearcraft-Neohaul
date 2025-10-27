package com.nred.nuclearcraft.menu.multiblock;

import com.nred.nuclearcraft.block_entity.fission.SaltFissionControllerEntity;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.molten_salt.SaltFissionLogic;
import com.nred.nuclearcraft.payload.multiblock.FissionUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

import static com.nred.nuclearcraft.registration.MenuRegistration.SALT_FISSION_CONTROLLER_MENU_TYPE;

public class SaltFissionControllerMenu extends MultiblockControllerMenu<FissionReactor, FissionUpdatePacket, SaltFissionControllerEntity, TileContainerInfo<SaltFissionControllerEntity>> {
    public final SaltFissionLogic logic = (SaltFissionLogic) this.tile.getLogic();

    public SaltFissionControllerMenu(int containerId, Inventory inventory, final SaltFissionControllerEntity controller) {
        super(SALT_FISSION_CONTROLLER_MENU_TYPE.get(), containerId, inventory, controller);
    }

    // Client Constructor
    public SaltFissionControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }
}