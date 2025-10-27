package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfoImpl;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.helpers.MenuHelper;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class InfoTileMenu<TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends NCPacket, INFO extends TileContainerInfo<TILE>> extends NCContainer {
    public final INFO info;

    protected InfoTileMenu(MenuType<?> menuType, int containerId, Inventory inventory, TILE tile) {
        super(menuType, containerId, inventory, tile);
        info = tile.getContainerInfo();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        if (info instanceof ProcessorMenuInfoImpl.BasicProcessorMenuInfo<?, ?> basicProcessorContainerInfo) {
            return MenuHelper.quickMoveStack(player, index, slots, this::moveItemStackTo, basicProcessorContainerInfo.itemInputSize + basicProcessorContainerInfo.itemOutputSize);
        }
        return MenuHelper.quickMoveStack(player, index, slots, this::moveItemStackTo, 0);
    }
}