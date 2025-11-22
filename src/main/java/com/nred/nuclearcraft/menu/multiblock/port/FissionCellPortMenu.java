package com.nred.nuclearcraft.menu.multiblock.port;

import com.nred.nuclearcraft.block_entity.fission.SolidFissionCellEntity;
import com.nred.nuclearcraft.block_entity.fission.port.FissionCellPortEntity;
import com.nred.nuclearcraft.handler.BasicRecipeHandler;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.slot.FilteredSlot;
import com.nred.nuclearcraft.menu.slot.ResultSlot;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactor;
import com.nred.nuclearcraft.multiblock.fisson.FissionReactorLogic;
import com.nred.nuclearcraft.payload.multiblock.port.ItemPortUpdatePacket;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import static com.nred.nuclearcraft.registration.MenuRegistration.FISSION_CELL_PORT_MENU_TYPE;

public class FissionCellPortMenu extends PortMenu<FissionReactor, FissionReactorLogic, FissionCellPortEntity, SolidFissionCellEntity, ItemPortUpdatePacket, TileContainerInfo<FissionCellPortEntity>> {
    public FissionCellPortMenu(int containerId, Inventory inventory, FissionCellPortEntity tile) {
        super(FISSION_CELL_PORT_MENU_TYPE.get(), containerId, inventory, tile);

        addSlot(new FilteredSlot.ProcessorInput(tile, NCRecipes.solid_fission, 0, 44, 35));
        addSlot(new ResultSlot(inventory.player, tile, 1, 116, 35));

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
    public FissionCellPortMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    protected BasicRecipeHandler<?> getRecipeHandler() {
        return NCRecipes.solid_fission;
    }
}