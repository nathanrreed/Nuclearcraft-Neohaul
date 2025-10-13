package com.nred.nuclearcraft.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Arrays;
import java.util.List;

public record BasicRecipeInput(List<SizedIngredient> itemIngredients, List<SizedFluidIngredient> fluidIngredients) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return Arrays.stream(itemIngredients.get(index).getItems()).findFirst().orElse(ItemStack.EMPTY);
    }

    public FluidStack getFluid(int index) {
        return Arrays.stream(fluidIngredients.get(index).getFluids()).findFirst().orElse(FluidStack.EMPTY);
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