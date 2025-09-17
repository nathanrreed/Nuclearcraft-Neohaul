//package com.nred.nuclearcraft.screen;
//
//import com.google.common.collect.Lists;
//import it.zerono.mods.zerocore.lib.multiblock.AbstractMultiblockController;
//import nc.gui.GuiInfoTile;
//import nc.multiblock.*;
//import nc.network.multiblock.MultiblockUpdatePacket;
//import nc.tile.TileContainerInfo;
//import nc.tile.multiblock.*;
//import net.minecraft.ChatFormatting;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//
//import java.util.List;
//
//public abstract class MultiblockControllerScreen<MULTIBLOCK extends AbstractMultiblockController<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, CONTROLLER extends TileEntity & IMultiblockGuiPart<MULTIBLOCK, T, PACKET, CONTROLLER, INFO>, INFO extends TileContainerInfo<CONTROLLER>> extends GuiInfoTile<CONTROLLER, PACKET, INFO> {
//
//    protected final MULTIBLOCK multiblock;
//
//    public GuiMultiblockController(Inventory inventory, Player player, CONTROLLER controller, String textureLocation) {
//        super(inventory, player, controller, textureLocation);
//
//        this.multiblock = controller.getMultiblock();
//    }
//
//    protected abstract ResourceLocation getGuiTexture();
//
//    public List<Component> clearAllInfo() {
//        return Lists.newArrayList(Component.translatable("tooltip.shift_clear_multiblock").withStyle(ChatFormatting.ITALIC));
//    }
//}
