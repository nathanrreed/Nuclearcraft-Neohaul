package com.nred.nuclearcraft.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

@FunctionalInterface
public interface MenuFunction<TILE extends BlockEntity> {
    AbstractContainerMenu apply(int containerId, Inventory inventory, TILE tile);
}