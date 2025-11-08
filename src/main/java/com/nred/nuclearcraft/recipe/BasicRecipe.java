package com.nred.nuclearcraft.recipe;

import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import com.nred.nuclearcraft.handler.SizedChanceItemIngredient;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class BasicRecipe implements IRecipe, Recipe<BasicRecipeInput> {
    public List<SizedChanceItemIngredient> itemIngredients, itemProducts;
    public List<SizedChanceFluidIngredient> fluidIngredients, fluidProducts;

    public BasicRecipe(List<SizedChanceItemIngredient> itemIngredients, List<SizedChanceFluidIngredient> fluidIngredients, List<SizedChanceItemIngredient> itemProducts, List<SizedChanceFluidIngredient> fluidProducts) {
        this.itemIngredients = itemIngredients;
        this.fluidIngredients = fluidIngredients;
        this.itemProducts = itemProducts;
        this.fluidProducts = fluidProducts;
    }

    @Override
    public List<SizedChanceItemIngredient> getItemIngredients() {
        return itemIngredients;
    }

    @Override
    public List<SizedChanceFluidIngredient> getFluidIngredients() {
        return fluidIngredients;
    }

    @Override
    public List<SizedChanceItemIngredient> getItemProducts() {
        return itemProducts;
    }

    @Override
    public List<SizedChanceFluidIngredient> getFluidProducts() {
        return fluidProducts;
    }

    @Override
    public boolean matches(BasicRecipeInput input, Level level) {
        return RecipeHelper.matchIngredients(IngredientSorption.INPUT, itemIngredients, fluidIngredients, input.itemIngredients(), input.fluidIngredients()).isMatch;
    }

    @Override
    public ItemStack assemble(BasicRecipeInput input, HolderLookup.Provider registries) {
        return itemProducts.isEmpty() ? ItemStack.EMPTY : itemProducts.getFirst().getStack().copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return itemProducts.isEmpty() ? ItemStack.EMPTY : itemProducts.getFirst().getStack().copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        throw new RuntimeException("Basic Recipe Serializer Used!");
    }

    @Override
    public RecipeType<?> getType() {
        throw new RuntimeException("Basic Recipe Type Used!");
    }
}