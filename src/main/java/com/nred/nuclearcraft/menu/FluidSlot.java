package com.nred.nuclearcraft.menu;

import com.nred.nuclearcraft.helpers.CustomFluidStackHandler;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidSlot extends Slot {
    private static Container emptyInventory = new SimpleContainer(0);
    private final CustomFluidStackHandler fluidHandler;
    protected final int index;
    public final int size;

    public FluidSlot(CustomFluidStackHandler fluidHandler, int index, int x, int y) {
        this(fluidHandler, index, x, y, 16);
    }

    public FluidSlot(CustomFluidStackHandler fluidHandler, int index, int x, int y, int size) {
        super(emptyInventory, index, x, y);
        this.fluidHandler = fluidHandler;
        this.index = index;
        this.size = size;
    }

    public FluidStack getFluid() {
        return this.getFluidHandler().getFluidInTank(index);
    }

    public int getIndex() {
        return index;
    }

    public int getFluidCapacity() {
        return this.getFluidHandler().getTankCapacity(index);
    }

    public void empty() {
        this.getFluidHandler().setFluid(index, FluidStack.EMPTY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void set(ItemStack stack) {
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean isHighlightable() {
        return false;
    }

    public CustomFluidStackHandler getFluidHandler() {
        return fluidHandler;
    }
}