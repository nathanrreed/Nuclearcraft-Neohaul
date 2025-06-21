package com.nred.nuclearcraft.recipe.base_types;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record FluidRecipeInput(List<FluidStack> fluids) implements RecipeInput {
    FluidRecipeInput(FluidStack fluid) {
        this(List.of(fluid));
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    public FluidStack getFluid(int index) {
        return fluids.get(index);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }

        for (int i = 0; i < this.fluids.size(); i++) {
            if (!this.getFluid(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
