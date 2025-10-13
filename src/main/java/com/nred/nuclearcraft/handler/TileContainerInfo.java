package com.nred.nuclearcraft.handler;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class TileContainerInfo<TILE extends BlockEntity> implements MenuProvider {

    //    public final String modId;
    public final String name;
//    public final DeferredBlock<Block> block;
//    public final DeferredHolder<MenuType<?>, MenuType<TurbineControllerMenu>> menuType; // TODO FIX

    public final Class<TILE> tileClass;

//    protected final MenuFunction containerFunction;
//    protected final GuiFunction<TILE> guiFunction;
//
//    public final int guiId;

    public TileContainerInfo(String name, Class<TILE> tileClass) { // , ContainerFunction<TILE> containerFunction, GuiFunction<TILE> guiFunction  // TODO check if this is needed
//        this.modId = modId;
        this.name = name;
//        this.block = block;

        this.tileClass = tileClass;

//        this.containerFunction = containerFunction;
//        this.guiFunction = guiFunction;
//
//        guiId = GuiHandler.getGuiId(name);
//        this.menuType = menuType;
    }
//    @Override
//    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
//        return new containerFunction.apply(containerId, playerInventory);

    /// /        ElectrolyzerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition));
//    }
//
    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(MODID + ".menu.title." + name);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return null;
    }

//  TODO  writeClientSideData

//    public TileContainerInfo(String modId, String name, Class<TILE> tileClass, ContainerFunction<TILE> containerFunction, GuiInfoTileFunction<TILE> guiFunction) {
//        this(modId, name, tileClass, containerFunction, GuiFunction.of(modId, name, containerFunction, guiFunction));
//    }
//
//    public Object getNewContainer(int id, Player player, TILE tile) {
//        return containerFunction.apply(player, tile);
//    }

//    @Override
//    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
//        return new TurbineControllerMenu(containerId, playerInventory, ContainerLevelAccess.create(level, worldPosition), this);
//    }

//
//    public Object getNewGui(int id, Player player, TILE tile) {
//        return guiFunction.apply(player, tile);
//    }
//
//    public int getGuiId() {
//        return guiId;
//    }
}
