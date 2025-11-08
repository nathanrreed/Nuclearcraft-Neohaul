package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record BasicRecipeInput(List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return itemIngredients.get(index).getStack();
    }

    public FluidStack getFluid(int index) {
        return fluidIngredients.get(index).getStack();
    }

    @Override
    public int size() {
        return itemIngredients.size();
    }

    public int fluid_size() {
        return fluidIngredients.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }

        for (int i = 0; i < this.fluid_size(); i++) {
            if (!this.getFluid(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }
}