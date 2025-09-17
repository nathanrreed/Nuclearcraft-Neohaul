package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.helpers.MenuHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractControllerMenu extends AbstractContainerMenu {
    public ContainerLevelAccess access;
    public final Inventory inventory;

    public AbstractControllerMenu(MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(menuType, containerId);

        this.inventory = inventory;
        this.access = access;
    }

//public class ContainerMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, GUITILE extends TileEntity & IMultiblockGuiPart<MULTIBLOCK, T, PACKET, GUITILE, INFO>, INFO extends TileContainerInfo<GUITILE>> extends ContainerInfoTile<GUITILE, PACKET, INFO> {

//    protected final GUITILE tile;
//
//    public AbstractControllerMenu(EntityPlayer player, GUITILE tile) {
//        super(tile);
//        this.tile = tile;
//
//        tile.addTileUpdatePacketListener(player);
//    }
//
//    @Override
//    public boolean canInteractWith(EntityPlayer player) {
//        return tile.isUsableByPlayer(player);
//    }
//
//    @Override
//    public void putStackInSlot(int slot, ItemStack stack) {
//
//    }
//
//    @Override
//    public void onContainerClosed(EntityPlayer player) {
//        super.onContainerClosed(player);
//        tile.removeTileUpdatePacketListener(player);
//    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return MenuHelper.quickMoveStack(player, index, slots, this::moveItemStackTo, 2);
    }
}
