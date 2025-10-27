package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.menu.MenuFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public class TileContainerInfo<TILE extends BlockEntity> {
    public final String name;

    protected final MenuFunction<TILE> menuFunction;

    public TileContainerInfo(String name, Class<TILE> tileClass, MenuFunction<TILE> menuFunction) {
        this.name = name;
        this.menuFunction = menuFunction;
    }

    public @NotNull Component getDisplayName() {
        return Component.translatable(MODID + ".menu.title." + name);
    }

    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, TILE entity) {
        return menuFunction.apply(containerId, playerInventory, entity);
    }
}