package com.nred.nuclearcraft.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public abstract class NCContainer extends AbstractContainerMenu {
    public final Inventory inventory;
    public BlockEntity tile;

    public NCContainer(MenuType<?> menuType, int containerId, Inventory inventory, BlockEntity tile) {
        super(menuType, containerId);
        this.inventory = inventory;
        this.tile = tile;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(Objects.requireNonNull(this.tile.getLevel()), this.tile.getBlockPos()), player, this.tile.getBlockState().getBlock());
    }

    public ItemStack transferPlayerStackDefault(Player player, int index, int invStart, int invEnd, ItemStack stack) {
        if (index >= invStart && index < invEnd - 9) {
            if (!moveItemStackTo(stack, invEnd - 9, invEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= invEnd - 9 && index < invEnd && !moveItemStackTo(stack, invStart, invEnd - 9, false)) {
            return ItemStack.EMPTY;
        }
        return null;
    }
}