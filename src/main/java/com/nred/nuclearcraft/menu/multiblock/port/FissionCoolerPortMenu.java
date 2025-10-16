package com.nred.nuclearcraft.menu.multiblock.port;

import com.nred.nuclearcraft.block_entity.fission.FissionCoolerEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCoolerPortEntity;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.payload.multiblock.port.FluidPortUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import static com.nred.nuclearcraft.registration.MenuRegistration.FISSION_COOLER_PORT_MENU_TYPE;

public class FissionCoolerPortMenu extends ContainerPort<FissionReactor, FissionReactorLogic, FissionCoolerPortEntity, FissionCoolerEntity, FluidPortUpdatePacket, TileContainerInfo<FissionCoolerPortEntity>> {

    public FissionCoolerPortMenu(int containerId, Inventory inventory, FissionCoolerPortEntity tile) {
        super(FISSION_COOLER_PORT_MENU_TYPE.get(), containerId, inventory, tile);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + 9 * i + 9, 8 + 18 * j, 84 + 18 * i));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(inventory, i, 8 + 18 * i, 142));
        }
    }

    // Client Constructor
    public FissionCoolerPortMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    protected BasicRecipeHandler getRecipeHandler() {
        return NCRecipes.fission_emergency_cooling;
    }
}