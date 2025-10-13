package com.nred.nuclearcraft.menu.multiblock.port;

import com.nred.nuclearcraft.block.fission.SaltFissionHeaterEntity;
import com.nred.nuclearcraft.block.fission.port.FissionHeaterPortEntity;
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

import static com.nred.nuclearcraft.registration.MenuRegistration.FISSION_HEATER_PORT_MENU_TYPE;

public class FissionHeaterPortMenu extends ContainerPort<FissionReactor, FissionReactorLogic, FissionHeaterPortEntity, SaltFissionHeaterEntity, FluidPortUpdatePacket, TileContainerInfo<FissionHeaterPortEntity>> {

    public FissionHeaterPortMenu(int containerId, Inventory inventory, FissionHeaterPortEntity tile) {
        super(FISSION_HEATER_PORT_MENU_TYPE.get(), containerId, inventory, tile);

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
    public FissionHeaterPortMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    protected BasicRecipeHandler<?> getRecipeHandler() { //TODO
        return NCRecipes.coolant_heater;
    }
}