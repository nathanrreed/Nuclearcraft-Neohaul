package com.nred.nuclearcraft.recipe;

import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.fluids.FluidStack;

public interface IRecipeHandler {
    boolean isValidFluidInput(FluidStack stack, RecipeManager recipeManager);

    int getFluidInputSize();

    int getFluidOutputSize();
}