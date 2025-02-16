package com.nred.nuclearcraft.recipe.base_types;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public record ProcessorRecipeInput(List<ItemStack> stacks, List<FluidStack> fluids) implements RecipeInput {
    public ProcessorRecipeInput(List<ItemStack> stacks) {
        this(stacks, List.of());
    }

    @Override
    public ItemStack getItem(int index) {
        return stacks.get(index);
    }

    public FluidStack getFluid(int index) {
        return fluids.get(index);
    }

    @Override
    public int size() {
        return stacks.size();
    }
}
