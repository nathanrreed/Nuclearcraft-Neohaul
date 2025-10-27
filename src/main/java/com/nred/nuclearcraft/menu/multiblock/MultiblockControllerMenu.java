package com.nred.nuclearcraft.menu.multiblock;

import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.InfoTileMenu;
import com.nred.nuclearcraft.multiblock.IMultiblockGuiPart;
import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
import com.nred.nuclearcraft.multiblock.Multiblock;
import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class MultiblockControllerMenu<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, GUITILE extends BlockEntity & IMultiblockGuiPart<MULTIBLOCK, PACKET, GUITILE, INFO>, INFO extends TileContainerInfo<GUITILE>> extends InfoTileMenu<GUITILE, PACKET, INFO> {
    public final GUITILE tile;

    public MultiblockControllerMenu(MenuType<?> menuType, int containerId, Inventory inventory, GUITILE tile) {
        super(menuType, containerId, inventory, tile);
        this.tile = tile;

        tile.addTileUpdatePacketListener(inventory.player);
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        tile.addTileUpdatePacketListener(inventory.player);
    }
}