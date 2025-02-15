package com.nred.nuclearcraft.recipe.base_types;

import com.nred.nuclearcraft.recipe.SizedItemIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class ItemToItemRecipe implements Recipe<ItemToItemInput> {
    public final List<SizedItemIngredient> itemInputs;
    public final List<SizedItemIngredient> itemResults;
    private final double timeModifier;
    private final double powerModifier;

    public ItemToItemRecipe(List<SizedItemIngredient> itemInputs, List<SizedItemIngredient> itemResults, double timeModifier, double powerModifier) {
        this.itemInputs = itemInputs;
        this.itemResults = itemResults;
        this.timeModifier = timeModifier;
        this.powerModifier = powerModifier;
    }

    @Override
    public boolean matches(ItemToItemInput input, Level level) {
        for (int i = 0; i < itemInputs.size(); i++) {
            if (!itemInputs.get(i).test(input.stacks())) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(ItemToItemInput input, HolderLookup.Provider registries) {
        return itemResults.getFirst().getItems()[0];
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return itemResults.getFirst().getItems()[0];
    }

    public List<SizedItemIngredient> getItemInputs() {
        return itemInputs;
    }

    public List<SizedItemIngredient> getItemResults() {
        return itemResults;
    }

    public double getTimeModifier() {
        return timeModifier;
    }

    public double getPowerModifier() {
        return powerModifier;
    }
}