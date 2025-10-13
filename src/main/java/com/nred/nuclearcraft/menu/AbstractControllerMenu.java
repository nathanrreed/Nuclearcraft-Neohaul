//package com.nred.nuclearcraft.menu;
//
//import com.nred.nuclearcraft.handler.TileContainerInfo;
//import com.nred.nuclearcraft.multiblock.IMultiblockGuiPart;
//import com.nred.nuclearcraft.multiblock.IPacketMultiblock;
//import com.nred.nuclearcraft.multiblock.Multiblock;
//import com.nred.nuclearcraft.payload.multiblock.MultiblockUpdatePacket;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.MenuType;
//import net.minecraft.world.level.block.entity.BlockEntity;
//
//public abstract class AbstractControllerMenu<MULTIBLOCK extends Multiblock<MULTIBLOCK> & IPacketMultiblock<MULTIBLOCK, PACKET>, PACKET extends MultiblockUpdatePacket, GUITILE extends BlockEntity & IMultiblockGuiPart<MULTIBLOCK, PACKET, GUITILE, INFO>, INFO extends TileContainerInfo<GUITILE>> extends AbstractContainerMenu {
//    public final Inventory inventory;
//    public GUITILE guitile;
//    protected final INFO info;
//
//    public AbstractControllerMenu(MenuType<?> menuType, int containerId, Inventory inventory, GUITILE guitile) {
//        super(menuType, containerId);
//        this.inventory = inventory;
//        this.guitile = guitile;
//        info = this.guitile.getContainerInfo();
//        this.guitile.addBEUpdatePacketListener(inventory.player);
//    }
//}