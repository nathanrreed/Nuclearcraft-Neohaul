package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.helpers.MenuHelper;
import com.nred.nuclearcraft.multiblock.IMultiblockGuiPart;
import com.nred.nuclearcraft.multiblock.MachineMultiblock;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractControllerMenu<T extends IMultiblockGuiPart<MULTIBLOCK>, MULTIBLOCK extends MachineMultiblock<MULTIBLOCK>> extends AbstractContainerMenu {
    public ContainerLevelAccess access;
    public final Inventory inventory;
    public T controller;

    public AbstractControllerMenu(MenuType<?> menuType, T controller, int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(menuType, containerId);

        this.inventory = inventory;
        this.access = access;
        this.controller = controller;
        this.controller.addBEUpdatePacketListener(inventory.player);
    }

    @Override
    public void removed(Player player) {
        controller.addBEUpdatePacketListener(inventory.player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return MenuHelper.quickMoveStack(player, index, slots, this::moveItemStackTo, 2);
    }
}
