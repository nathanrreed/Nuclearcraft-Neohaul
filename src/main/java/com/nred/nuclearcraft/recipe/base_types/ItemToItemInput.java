package com.nred.nuclearcraft.recipe.base_types;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record ItemToItemInput(List<ItemStack> stacks) implements RecipeInput {
    public ItemToItemInput(ItemStack stack) {
        this(List.of(stack));
    }

    @Override
    public ItemStack getItem(int index) {
        return stacks.get(index);
    }

    @Override
    public int size() {
        return stacks.size();
    }
}
