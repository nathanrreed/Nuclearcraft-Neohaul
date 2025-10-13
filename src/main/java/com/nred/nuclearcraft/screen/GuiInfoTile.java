package com.nred.nuclearcraft.screen;

import com.nred.nuclearcraft.block.ITileGui;
import com.nred.nuclearcraft.handler.TileContainerInfo;
import com.nred.nuclearcraft.menu.ContainerInfoTile;
import com.nred.nuclearcraft.payload.NCPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

// TODO fix MENU
public abstract class GuiInfoTile<TILE extends BlockEntity & ITileGui<TILE, PACKET, INFO>, PACKET extends NCPacket, INFO extends TileContainerInfo<TILE>, MENU extends ContainerInfoTile<TILE, PACKET, INFO>> extends NCGui<MENU> {
//    protected final Player player;

    protected final TILE tile;
    protected final INFO info;

//    protected final ResourceLocation guiTextures;

//    protected final Lazy<String> guiName;
//    protected final Lazy.LazyInt nameWidth;

    public GuiInfoTile(MENU menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

//        this.player = menu;

        this.tile = (TILE) menu.tile;
        info = tile.getContainerInfo();

//        guiTextures = textureLocation;

//        guiName = new Lazy<>(() -> tile.getDisplayName().getUnformattedText());
//        nameWidth = new Lazy.LazyInt(() -> fontRenderer.getStringWidth(guiName.get()));
    }

//	protected void defaultStateAndBind() {
//		GlStateManager.color(1F, 1F, 1F, 1F);
//		mc.getTextureManager().bindTexture(guiTextures);
//	}
}