package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.block_entity.ITileGui;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.InfoTileMenu;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class GuiInfoTile<MENU extends InfoTileMenu<TILE, PACKET, INFO>, TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends NCPacket, INFO extends TileContainerInfo<TILE>> extends NCGui<MENU> {
    protected final TILE tile;
    protected final INFO info;

    protected final ResourceLocation guiTextures;

    public GuiInfoTile(MENU menu, Inventory inventory, Component title, ResourceLocation textureLocation) {
        super(menu, inventory, title);

        this.tile = (TILE) menu.tile;
        info = tile.getContainerInfo();

        guiTextures = textureLocation;
    }
}