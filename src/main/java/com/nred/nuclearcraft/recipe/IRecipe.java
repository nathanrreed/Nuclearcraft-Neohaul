package com.nred.nuclearcraft.recipe;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public interface IRecipe {
    List<SizedIngredient> getItemIngredients();

    List<SizedFluidIngredient> getFluidIngredients();

    List<SizedIngredient> getItemProducts();

    List<SizedFluidIngredient> getFluidProducts();

    default SizedIngredient getItemIngredient() {
        return getItemIngredients().getFirst();
    }

    default SizedIngredient getItemProduct() {
        return getItemProducts().getFirst();
    }

    default SizedFluidIngredient getFluidIngredient() {
        return getFluidIngredients().getFirst();
    }

    default SizedFluidIngredient getFluidProduct() {
        return getFluidProducts().getFirst();
    }
}