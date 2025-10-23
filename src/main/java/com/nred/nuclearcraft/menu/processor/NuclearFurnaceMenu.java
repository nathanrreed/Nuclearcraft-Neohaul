package com.nred.nuclearcraft.menu.processor;

import com.nred.nuclearcraft.block_entity.processor.NuclearFurnaceEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import static com.nred.nuclearcraft.registration.MenuRegistration.NUCLEAR_FURNACE_MENU_TYPE;

public class NuclearFurnaceMenu extends AbstractFurnaceMenu {
    public NuclearFurnaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        super(NUCLEAR_FURNACE_MENU_TYPE.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, containerId, playerInventory);
    }

    public NuclearFurnaceMenu(int containerId, Inventory playerInventory, Container furnaceContainer, ContainerData furnaceData) {
        super(NUCLEAR_FURNACE_MENU_TYPE.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, containerId, playerInventory, furnaceContainer, furnaceData);
    }

    @Override
    protected boolean isFuel(ItemStack stack) {
        return NuclearFurnaceEntity.burnDuration(stack) > 0;
    }

    @Override
    protected boolean canSmelt(ItemStack stack) {
        return super.canSmelt(stack) && !(this.slots.get(0).getItem().getCount() == this.slots.get(0).getItem().getMaxStackSize()); // Allows quick move to fuel stack
    }
}