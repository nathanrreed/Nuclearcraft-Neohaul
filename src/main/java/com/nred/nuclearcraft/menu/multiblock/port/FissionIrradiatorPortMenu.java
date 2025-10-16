package com.nred.nuclearcraft.menu.multiblock.port;

import com.nred.nuclearcraft.block_entity.fission.FissionIrradiatorEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionIrradiatorPortEntity;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.handler.NCRecipes;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.slot.SlotFiltered;
import com.nred.nuclearcraft.menu.slot.SlotFurnace;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.payload.multiblock.port.ItemPortUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import static com.nred.nuclearcraft.registration.MenuRegistration.FISSION_IRRADIATOR_PORT_MENU_TYPE;

public class FissionIrradiatorPortMenu extends ContainerPort<FissionReactor, FissionReactorLogic, FissionIrradiatorPortEntity, FissionIrradiatorEntity, ItemPortUpdatePacket, TileContainerInfo<FissionIrradiatorPortEntity>> {
    public FissionIrradiatorPortMenu(int containerId, Inventory inventory, FissionIrradiatorPortEntity tile) {
        super(FISSION_IRRADIATOR_PORT_MENU_TYPE.get(), containerId, inventory, tile);

        addSlot(new SlotFiltered.ProcessorInput(tile, NCRecipes.fission_irradiator, 0, 44, 35));
        addSlot(new SlotFurnace(inventory.player, tile, 1, 116, 35));

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
    public FissionIrradiatorPortMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    protected BasicRecipeHandler getRecipeHandler() {
        return NCRecipes.fission_irradiator;
    }
}
