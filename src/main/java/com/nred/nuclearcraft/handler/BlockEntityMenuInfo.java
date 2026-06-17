package com.nred.nuclearcraft.handler;

import com.nred.nuclearcraft.menu.MenuFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;

public class BlockEntityMenuInfo<TILE extends BlockEntity> {
    public final String name;

    protected final MenuFunction<TILE> menuFunction;

    public BlockEntityMenuInfo(String name, Class<TILE> tileClass, MenuFunction<TILE> menuFunction) {
        this.name = name;
        this.menuFunction = menuFunction;
    }

    public ResourceLocation getFrontTexture() {
        if (name.contains(":")){
           ResourceLocation location = ResourceLocation.parse( name);
           return ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "block/" + location.getPath());
        }else {
            return ncLoc("block/processor/" + name + "_front");
        }
    }

    public @NotNull Component getDisplayName() {
        return Component.translatable(MODID + ".menu.title." + name);
    }

    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, TILE entity) {
        return menuFunction.apply(containerId, playerInventory, entity);
    }
}